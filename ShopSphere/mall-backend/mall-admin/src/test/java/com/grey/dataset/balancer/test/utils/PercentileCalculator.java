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


    public static void main(String[] args) {
        // Example with array
        double[] arrayData = {12, 15, 17, 20, 23, 25, 28, 30, 35, 40};
        double percentile = 75;
        double resultArray = calculatePercentile(arrayData, percentile);
        System.out.println("From Array: The " + percentile + "th percentile is: " + resultArray);


    }
}