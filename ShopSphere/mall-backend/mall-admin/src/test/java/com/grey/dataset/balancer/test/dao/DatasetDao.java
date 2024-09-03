package com.grey.dataset.balancer.test.dao;

import com.grey.dataset.balancer.test.bean.dataset;
import com.grey.dataset.balancer.test.bean.ddetailEntity;
import com.grey.dataset.balancer.test.bean.dfileEntitiy;

import java.util.ArrayList;
import java.util.List;

public class DatasetDao {

    public List<dataset> getDatasets() {

        List<dataset> datasets = new ArrayList<>();
        datasets.add(createDataset(1L, 101L, "Dataset 1", "v1.0", "3", "Image Recognition", 20));

        return datasets;
    }


    public List<dfileEntitiy> getDfileEntities() {
        List<dfileEntitiy> dfileEntities = new ArrayList<>();

        // Dataset 1 - 分组A的文件实体
        dfileEntities.add(createFileEntity(1L, "201", "3", "folderA"));
        dfileEntities.add(createFileEntity(1L, "202", "3", "folderA"));
        dfileEntities.add(createFileEntity(1L, "203", "3", "folderA"));
        dfileEntities.add(createFileEntity(1L, "204", "3", "folderA"));
        dfileEntities.add(createFileEntity(1L, "205", "3", "folderA"));
        dfileEntities.add(createFileEntity(1L, "206", "3", "folderA"));
        dfileEntities.add(createFileEntity(1L, "207", "3", "folderA"));
        dfileEntities.add(createFileEntity(1L, "208", "3", "folderA"));


        // Dataset 1 - 分组B的文件实体
        dfileEntities.add(createFileEntity(1L, "301", "3", "folderB"));
        dfileEntities.add(createFileEntity(1L, "302", "3", "folderB"));
        dfileEntities.add(createFileEntity(1L, "303", "3", "folderB"));
        dfileEntities.add(createFileEntity(1L, "304", "3", "folderB"));
        dfileEntities.add(createFileEntity(1L, "305", "3", "folderB"));
        dfileEntities.add(createFileEntity(1L, "306", "3", "folderB"));

        // Dataset 1 - 分组C的文件实体
        dfileEntities.add(createFileEntity(1L, "401", "3", "folderC"));
        dfileEntities.add(createFileEntity(1L, "402", "3", "folderC"));
        dfileEntities.add(createFileEntity(1L, "403", "3", "folderC"));
        dfileEntities.add(createFileEntity(1L, "404", "3", "folderC"));

        // Dataset 1 - 分组D的文件实体
        dfileEntities.add(createFileEntity(1L, "501", "3", "folderD"));
        dfileEntities.add(createFileEntity(1L, "502", "3", "folderD"));

        return dfileEntities;
    }


    public List<ddetailEntity> getDdetailEntities() {
        // A，B,C,D 四个分组图片数量分别为8，6，4，2
        List<ddetailEntity> ddetailEntities = new ArrayList<>();

        // Dataset 1 - 分组A ，总共有8张图片
        ddetailEntities.add(createDetailEntity(1L, 201L, "fileA1", "label1", "A", "3"));
        ddetailEntities.add(createDetailEntity(1L, 202L, "fileA2", "label1", "A", "3"));
        ddetailEntities.add(createDetailEntity(1L, 203L, "fileA3", "label1", "A", "3"));
        ddetailEntities.add(createDetailEntity(1L, 204L, "fileA4", "label1", "A", "3"));
        ddetailEntities.add(createDetailEntity(1L, 205L, "fileA5", "label1", "A", "3"));
        ddetailEntities.add(createDetailEntity(1L, 206L, "fileA6", "label1", "A", "3"));
        ddetailEntities.add(createDetailEntity(1L, 207L, "fileA7", "label1", "A", "3"));
        ddetailEntities.add(createDetailEntity(1L, 208L, "fileA8", "label1", "A", "3"));


        // Dataset 1 - 分组2，总共有6张图片
        ddetailEntities.add(createDetailEntity(1L, 301L, "fileB1", "label2", "B", "3"));
        ddetailEntities.add(createDetailEntity(1L, 302L, "fileB2", "label2", "B", "3"));
        ddetailEntities.add(createDetailEntity(1L, 303L, "fileB3", "label2", "B", "3"));
        ddetailEntities.add(createDetailEntity(1L, 304L, "fileB4", "label2", "B", "3"));
        ddetailEntities.add(createDetailEntity(1L, 305L, "fileB5", "label2", "B", "3"));
        ddetailEntities.add(createDetailEntity(1L, 306L, "fileB6", "label2", "B", "3"));



        // Dataset 1 - 分组3，总共有4张图片
        ddetailEntities.add(createDetailEntity(1L, 401L, "fileC1", "label3", "C", "3"));
        ddetailEntities.add(createDetailEntity(1L, 402L, "fileC2", "label3", "C", "3"));
        ddetailEntities.add(createDetailEntity(1L, 403L, "fileC2", "label3", "C", "3"));
        ddetailEntities.add(createDetailEntity(1L, 404L, "fileC2", "label3", "C", "3"));



        // Dataset 1 - 分组4，总共有2张图片
        ddetailEntities.add(createDetailEntity(1L, 501L, "fileD1", "label4", "D", "3"));
        ddetailEntities.add(createDetailEntity(1L, 502L, "fileD1", "label4", "D", "3"));



        return ddetailEntities;
    }


    private dataset createDataset(Long id, Long bucket_id, String name, String version_num, String usage_type, String scene_id, int total_files) {
        dataset ds = new dataset();
        ds.setId(id);
        ds.setBucket_id(bucket_id);
        ds.setName(name);
        ds.setVersion_num(version_num);
        ds.setUsage_type(usage_type);
        ds.setScene_id(scene_id);
        ds.setTotal_files(total_files);
        return ds;
    }

    private ddetailEntity createDetailEntity(Long datasetId, Long fileId, String fileName, String labelId, String categoryId, String fileUsage) {
        ddetailEntity entity = new ddetailEntity();
        entity.setDataset_id(datasetId);
        entity.setFile_id(fileId);
        entity.setFile_name(fileName);
        entity.setLabel_id(labelId);
        entity.setCategory_id(categoryId);
        entity.setFile_usage(fileUsage);
        return entity;
    }


    private dfileEntitiy createFileEntity(Long datasetId, String fileId, String fileUsage, String folderId) {
        dfileEntitiy entity = new dfileEntitiy();
        entity.setDataset_id(datasetId);
        entity.setFile_id(fileId);
        entity.setFile_usage(fileUsage);
        entity.setFolder_id(folderId);
        return entity;
    }
}
