package com.macro.mall.dao;

import com.macro.mall.dto.PmsProductAttributeCategoryItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 自定义商品属性分类Dao
 * Created on 2018/5/24.
 */
@Mapper
public interface PmsProductAttributeCategoryDao {
    List<PmsProductAttributeCategoryItem> getListWithAttr();
}
