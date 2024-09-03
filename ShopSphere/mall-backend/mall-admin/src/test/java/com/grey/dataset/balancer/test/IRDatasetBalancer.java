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
        // Step 1: Filter out the training data
        List<ddetailEntity> trainingData = datasetDetails.stream()
                .filter(detail -> "3".equals(detail.getFile_usage()))  // Filter for training dataset
                .collect(Collectors.toList());

        // Step 2: Group by category_id and count the number of images per category
        Map<String, List<ddetailEntity>> categoryCountMap = new HashMap<>();
        for (ddetailEntity detail : trainingData) {
            String categoryId = detail.getCategory_id();
            categoryCountMap.computeIfAbsent(categoryId, k -> new ArrayList<>()).add(detail);
        }

        // Step 3: Calculate the number of images per category and then calculate the percentile
        int numberOfCategories = categoryCountMap.size();
        double[] imageCounts = new double[numberOfCategories];

        Map<String, Double> categoryPercentileMap = new HashMap<>();
        int index = 0;
        for (Map.Entry<String, List<ddetailEntity>> entry : categoryCountMap.entrySet()) {

            List<ddetailEntity> details = entry.getValue();

            imageCounts[index] = details.size();
            index++;


        }
       // 2 4 6 8

        // 计算中位数
        double meDianValue = PercentileCalculator.calculateMedian(imageCounts);

        // 计算传人百位数
        double percentileValue = PercentileCalculator.calculatePercentile(imageCounts, 75.0);


        System.out.println("meDianValue:"+ meDianValue);

        System.out.println("percentileValue:"+ percentileValue);
    }



}
