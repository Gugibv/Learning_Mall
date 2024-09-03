package com.grey.dataset.balancer.test.bean;

import lombok.Data;

/**
 * 数据集的文件标记信息
 */
@Data
public class ddetailEntity {
    Long dataset_id;
    Long file_id;
    String file_name;
    String label_id;
    String category_id;
    String file_usage;

}
