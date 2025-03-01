package com.macro.mall.dao;

import com.macro.mall.model.PmsProductAttributeValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品参数，商品自定义规格属性Dao
 * Created on 2018/4/26.
 */
@Mapper
public interface PmsProductAttributeValueDao {
    int insertList(@Param("list")List<PmsProductAttributeValue> productAttributeValueList);
}
