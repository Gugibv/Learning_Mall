package com.grey.dataset.balancer.test.bean;

import lombok.Data;

/**
 * 数据集信息
 */
@Data
public class dataset {
    Long id ; // 即datasetId，数据集编号
    Long bucket_id; // 文件存储桶的ID
    String name ; // 数据集名称
    String version_num; //数据集版本
    String usage_type; // 用途：1 evaluation, 2 prediction, 3 training
    String scene_id; // 场景编号：3 为 Image Recognition
    int total_files ;// 文件总数

}
