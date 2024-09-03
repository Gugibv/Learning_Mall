package com.grey.dataset.balancer.test.utils;
import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;

public class PercentileCalculator {

    // Method to calculate percentile from a double array
    public static double calculatePercentile(double[] data, double percentile) {
        Arrays.sort(data);
        double rank = (percentile / 100.0) * (data.length - 1);
        int lowerIndex = (int) rank;
        int upperIndex = lowerIndex + 1;

        if (upperIndex >= data.length) {
            return data[lowerIndex];
        }

        double weight = rank - lowerIndex;
        return data[lowerIndex] + weight * (data[upperIndex] - data[lowerIndex]);
    }

    // Method to calculate percentile from a collection
    public static double calculatePercentile(Collection<Double> data, double percentile) {
        double[] dataArray = data.stream().mapToDouble(Double::doubleValue).toArray();
        Arrays.sort(dataArray);
        double rank = (percentile / 100.0) * (dataArray.length - 1)  ; // rank = (75 / 100.0) * (10 - 1) = 6.75
        int lowerIndex = (int) rank;      // 第6位  25
        int upperIndex = lowerIndex + 1;  // 第7位  28

        if (upperIndex >= dataArray.length) {
            return dataArray[lowerIndex];
        }

        double weight = rank - lowerIndex; //  0.75
        // 所以75.0%百分位点是 ： 25 * 0.25+ 28* 0.75 = 7+ 21 = 30
        return dataArray[lowerIndex] + weight * (dataArray[upperIndex] - dataArray[lowerIndex]);


    }

    // Method to calculate the median from an array
    public static double calculateMedian(double[] data) {
        Arrays.sort(data);
        int length = data.length;
        if (length % 2 == 0) {
            // Even number of elements: average the two middle values
            return (data[length / 2 - 1] + data[length / 2]) / 2.0;
        } else {
            // Odd number of elements: return the middle value
            return data[length / 2];
        }
    }

    // Method to calculate the median from a collection
    public static double calculateMedian(Collection<Double> data) {
        double[] dataArray = data.stream().mapToDouble(Double::doubleValue).toArray();
        return calculateMedian(dataArray);
    }

    public static void main(String[] args) {
        // Example with array
        double[] arrayData = {12, 15, 17, 20, 23, 25, 28, 30, 35, 40};
        double percentile = 75;
        double resultArray = calculatePercentile(arrayData, percentile);
        System.out.println("From Array: The " + percentile + "th percentile is: " + resultArray);

        // Example with collection
        Collection<Double> collectionData = new ArrayList<>();
        collectionData.add(12.0);
        collectionData.add(15.0);
        collectionData.add(17.0);
        collectionData.add(20.0);
        collectionData.add(23.0);
        collectionData.add(25.0);
        collectionData.add(28.0);
        collectionData.add(30.0);
        collectionData.add(35.0);
        collectionData.add(40.0);
        double resultCollection = calculatePercentile(collectionData, percentile);
        System.out.println("From Collection: The " + percentile + "th percentile is: " + resultCollection);
    }
}