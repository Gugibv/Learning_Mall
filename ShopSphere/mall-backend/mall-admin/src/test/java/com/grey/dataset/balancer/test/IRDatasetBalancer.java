package com.grey.dataset.balancer.test;

import com.grey.dataset.balancer.test.bean.ddetailEntity;
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
        List<ddetailEntity> datasetDetails = dao.getDdetailEntities();
        double balancePercentile = 0.5;
        balanceDataset(datasetDetails, balancePercentile);
    }


    public static void balanceDataset(List<ddetailEntity> datasetDetails, double balancePercentile) {
        // Step 1: 过滤出 training 数据
        List<ddetailEntity> trainingData = datasetDetails.stream()
                .filter(detail -> "3".equals(detail.getFile_usage()))  // Filter for training dataset
                .collect(Collectors.toList());

        //  Step 2: 按 category_id 分组并计算每个分组的图片数量
        Map<String, List<ddetailEntity>> categoryCountMap = new HashMap<>();
        for (ddetailEntity detail : trainingData) {
            String categoryId = detail.getCategory_id();
            categoryCountMap.computeIfAbsent(categoryId, k -> new ArrayList<>()).add(detail);
        }

        // Step 3: 计算每个类别的图片数量
        int numberOfCategories = categoryCountMap.size();
        double[] imageCounts = new double[numberOfCategories];
        Map<String, Double> categoryPercentileMap = new HashMap<>();
        int index = 0;
        for (Map.Entry<String, List<ddetailEntity>> entry : categoryCountMap.entrySet()) {
            List<ddetailEntity> details = entry.getValue();
            imageCounts[index] = details.size();
            index++;
        }


        // Step 4: 计算中位数和目标百分位数的图片数量
        double medianValue = PercentileCalculator.calculateMedian(imageCounts);
        double targetPercentileValue = PercentileCalculator.calculatePercentile(imageCounts, 100);

        System.out.println("Median Value: " + medianValue);
        System.out.println("Target Percentile Value: " + targetPercentileValue);


       // Step 5: 对每个类别的图片数量进行补齐
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

                    // 将新文件添加到当前类别中
                    details.add(newFile);
                }
            }
        }

        // 输出补齐后的数据集的全部内容
        System.out.println("\nBalanced Dataset Details:");
        for (Map.Entry<String, List<ddetailEntity>> entry : categoryCountMap.entrySet()) {
            String categoryId = entry.getKey();
            List<ddetailEntity> details = entry.getValue();
            System.out.println("Category ID: " + categoryId + ", Total Images: " + details.size());
            for (ddetailEntity detail : details) {
                System.out.println("  - " + detail.getFile_name() + " (ID: " + detail.getFile_id() + ")");
            }
        }
    }





}
