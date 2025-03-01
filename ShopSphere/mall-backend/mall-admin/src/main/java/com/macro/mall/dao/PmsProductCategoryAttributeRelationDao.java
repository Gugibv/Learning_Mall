package com.macro.mall.dao;

import com.macro.mall.model.PmsProductCategoryAttributeRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自定义商品分类和属性关系Dao
 * Created on 2018/5/23.
 */
@Mapper
public interface PmsProductCategoryAttributeRelationDao {
    int insertList(@Param("list") List<PmsProductCategoryAttributeRelation> productCategoryAttributeRelationList);
}
