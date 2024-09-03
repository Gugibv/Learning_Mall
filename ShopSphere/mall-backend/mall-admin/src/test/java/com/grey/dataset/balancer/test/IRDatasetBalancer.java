package com.grey.dataset.balancer.test;

import com.grey.dataset.balancer.test.bean.dataset;
import com.grey.dataset.balancer.test.bean.ddetailEntity;
import com.grey.dataset.balancer.test.bean.dfileEntitiy;
import com.grey.dataset.balancer.test.dao.DatasetDao;
import com.grey.dataset.balancer.test.utils.PercentileCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 需求描述：
 * IR dataset balance功能做两点调整
 *
 * -  只针对training部分进行balance，validation的部分不做补充（因为对training没有意义）
 * -  增加一个配置参数，用来控制不足数量的图片补齐到多少，目前都是按照最大值进行补齐。新增加的参数可以设置从20%分位数的图片数量到最大值的图片数量，默认放到中位数的位置。
 *
 *  把category的图片数量从小到大排列，比如：2，4，6，8
 *
 *  默认是对齐到中位数的category的图片数量，
 *
 *
 *  用户可以设置对齐到20%分位数的category的图片数量一直到最大值category的图片数量）
 */
import java.util.*;

public class IRDatasetBalancer {

    public static void main(String[] args) {

        DatasetDao dao = new DatasetDao();

        // 获取原始数据
        List<ddetailEntity> originalDetails = dao.getDdetailEntities();
        List<dfileEntitiy> originalFiles = dao.getDfileEntities();
        List<dataset> originalDatasets = dao.getDatasets();

        // Balancing the dataset
        double balancePercentile = 1; // 默认对齐到中位数 0.5
        List<ddetailEntity> balancedDetails = balanceDataset(originalDetails, balancePercentile);

        // 存储补齐后的数据到原本的列表中，创建v2.0版本的数据集
        List<dfileEntitiy> newFileEntities = new ArrayList<>(originalFiles);
        List<ddetailEntity> newDetailEntities = new ArrayList<>(originalDetails);
        List<dataset> newDatasets = new ArrayList<>(originalDatasets);

        // 创建新的 dataset 实例 (v2.0)
        dataset newDataset = dao.createDataset(2L, 102L, "Dataset 1", "v2.0", "3", "Image Recognition", balancedDetails.size());
        newDatasets.add(newDataset);

        // 更新 dfileEntities 和 ddetailEntities 列表，添加 v2.0 的数据
        for (ddetailEntity detail : balancedDetails) {
            dfileEntitiy file = findMatchingFile(originalFiles, detail.getFile_id());
            if (file != null) {
                newFileEntities.add(dao.createFileEntity(newDataset.getId(), file.getFile_id(), file.getFile_usage(), file.getFolder_id()));
            }
            newDetailEntities.add(dao.createDetailEntity(newDataset.getId(), detail.getFile_id(), detail.getFile_name(), detail.getLabel_id(), detail.getCategory_id(), detail.getFile_usage()));
        }


        // 输出完整的ddetailEntities列表
        System.out.println("Complete ddetailEntities List:");
        for (ddetailEntity detail : newDetailEntities) {
            System.out.println("  - Dataset ID: " + detail.getDataset_id() + ", File Name: " + detail.getFile_name() + ", Category: " + detail.getCategory_id() + ", Version: " + (detail.getDataset_id() == 1L ? "v1.0" : "v2.0"));
        }


        // 输出完整的dfileEntities列表
        System.out.println("\nComplete dfileEntities List:");
        for (dfileEntitiy file : newFileEntities) {
            System.out.println("  - Dataset ID: " + file.getDataset_id() + ", File ID: " + file.getFile_id() + ", Folder ID: " + file.getFolder_id() + ", Version: " + (file.getDataset_id() == 1L ? "v1.0" : "v2.0"));
        }




        // 输出完整的datasets列表
        System.out.println("\nComplete datasets List:");
        for (dataset ds : newDatasets) {
            System.out.println("  - Dataset ID: " + ds.getId() + ", Name: " + ds.getName() + ", Version: " + ds.getVersion_num() + ", Total Files: " + ds.getTotal_files());
        }


        System.out.println("newDatasets 总数量:"+newDatasets.size());
        System.out.println("newFileEntities 总数量:"+newFileEntities.size());
        System.out.println("newDetailEntities 总数量:"+newDetailEntities.size());
    }


    public static List<ddetailEntity> balanceDataset(List<ddetailEntity> datasetDetails, double balancePercentile) {
        // Step 1: 过滤出 training 数据
        List<ddetailEntity> trainingData = datasetDetails.stream()
                .filter(detail -> "3".equals(detail.getFile_usage()))  // 过滤 training 数据集
                .collect(Collectors.toList());

        // Step 2: 按 category_id 分组并计算每个分组的图片数量
        Map<String, List<ddetailEntity>> categoryCountMap = new HashMap<>();
        for (ddetailEntity detail : trainingData) {
            String categoryId = detail.getCategory_id();
            categoryCountMap.computeIfAbsent(categoryId, k -> new ArrayList<>()).add(detail);
        }

        // Step 3: 计算每个类别的图片数量，然后计算百分位数
        int numberOfCategories = categoryCountMap.size();
        double[] imageCounts = new double[numberOfCategories];

        int index = 0;
        for (Map.Entry<String, List<ddetailEntity>> entry : categoryCountMap.entrySet()) {
            List<ddetailEntity> details = entry.getValue();
            imageCounts[index] = details.size();
            index++;
        }

        // 对图片数量进行排序
        Arrays.sort(imageCounts);

        // 计算中位数和目标百分位数的图片数量
        double targetPercentileValue = PercentileCalculator.calculatePercentile(imageCounts, balancePercentile * 100);

        System.out.println("Target Percentile Value: " + targetPercentileValue);

        // Step 5: 对每个类别的图片数量进行补齐
        List<ddetailEntity> balancedDetails = new ArrayList<>();
        for (Map.Entry<String, List<ddetailEntity>> entry : categoryCountMap.entrySet()) {
            String categoryId = entry.getKey();
            List<ddetailEntity> details = entry.getValue();
            int currentCount = details.size();

            // 如果当前图片数量小于目标百分位数的图片数量，则需要补齐
            if (currentCount < targetPercentileValue) {
                int filesToAdd = (int) targetPercentileValue - currentCount;
                System.out.println("Category " + categoryId + " needs " + filesToAdd + " more files.");

                // 在这里补齐文件，复制现有的文件
                for (int i = 0; i < filesToAdd; i++) {
                    ddetailEntity newFile = new ddetailEntity();
                    newFile.setDataset_id(details.get(0).getDataset_id());
                    newFile.setFile_id(System.currentTimeMillis() + i); // 使用时间戳作为文件ID，确保唯一性
                    newFile.setFile_name("copy_of_" + details.get(i % currentCount).getFile_name());
                    newFile.setLabel_id(details.get(0).getLabel_id());
                    newFile.setCategory_id(categoryId);
                    newFile.setFile_usage("3");  // 保持为 training

                    // 将新文件添加到平衡后的数据集中
                    balancedDetails.add(newFile);
                }
            }
            balancedDetails.addAll(details);
        }

        return balancedDetails;
    }


    public static dfileEntitiy findMatchingFile(List<dfileEntitiy> files, long fileId) {
        return files.stream().filter(file -> Long.parseLong(file.getFile_id()) == fileId).findFirst().orElse(null);
    }





}
