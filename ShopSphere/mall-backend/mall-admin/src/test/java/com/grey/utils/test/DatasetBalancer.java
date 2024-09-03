package com.grey.utils.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class DatasetBalancer {


    public static void main(String[] args) {
        // 假设我们有一个训练集的数据
        List<ImageData> trainingSet = loadTrainingSet();

        // 调用balanceTrainingSet方法, 目标设置为20%分位数
        balanceTrainingSet(trainingSet, 20);
    }

    private static List<ImageData> loadTrainingSet() {
        // 加载训练集数据的逻辑
        return new ArrayList<>();
    }

    public static void balanceTrainingSet(List<ImageData> trainingSet, int targetPercentile) {
        // 获取每个类别的图片数量
        Map<String, List<ImageData>> categoryMap = trainingSet.stream()
                .collect(Collectors.groupingBy(ImageData::getCategory));

        // 获取所有类别的图片数量
        List<Integer> categorySizes = categoryMap.values().stream()
                .map(List::size)
                .sorted()
                .collect(Collectors.toList());

        // 计算20%分位数, 中位数和最大值
        int q20 = getPercentile(categorySizes, 20);
        int median = getPercentile(categorySizes, 50);
        int max = getPercentile(categorySizes, 100);

        // 根据配置参数设置目标数量，默认为中位数
        int targetSize = (targetPercentile == 0) ? median : Math.max(q20, Math.min(max, targetPercentile));

        // 对每个类别进行补齐
        for (Map.Entry<String, List<ImageData>> entry : categoryMap.entrySet()) {
            List<ImageData> images = entry.getValue();
            int currentSize = images.size();

            if (currentSize < targetSize) {
                int numToAdd = targetSize - currentSize;
                List<ImageData> additionalImages = generateAdditionalImages(images, numToAdd);
                images.addAll(additionalImages);
            }
        }

        // 将平衡后的数据返回或保存
        saveBalancedDataset(categoryMap);
    }

    private static int getPercentile(List<Integer> sortedList, int percentile) {
        int index = (int) Math.ceil(percentile / 100.0 * sortedList.size()) - 1;
        return sortedList.get(index);
    }

    private static List<ImageData> generateAdditionalImages(List<ImageData> originalImages, int numToAdd) {
        List<ImageData> additionalImages = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < numToAdd; i++) {
            // 随机选取已有的图片进行复制
            ImageData randomImage = originalImages.get(rand.nextInt(originalImages.size()));
            additionalImages.add(new ImageData(randomImage));
        }
        return additionalImages;
    }

    private static void saveBalancedDataset(Map<String, List<ImageData>> balancedDataset) {
        // 保存或进一步处理平衡后的数据集
        // 这里可以实现保存逻辑，例如将平衡后的数据集写回磁盘
    }

    public static class ImageData {
        private String category;
        private String imagePath;

        // 构造函数和其他方法

        public ImageData(ImageData other) {
            this.category = other.category;
            this.imagePath = other.imagePath;
            // 复制其他属性
        }

        public String getCategory() {
            return category;
        }

        public String getImagePath() {
            return imagePath;
        }

        // 其他getter和setter方法
    }




}

