package com.macro.mall.dao;

import com.macro.mall.dto.PmsProductResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * 商品自定义Dao
 * Created on 2018/4/26.
 */
@Mapper
public interface PmsProductDao {
    /**
     * 获取商品编辑信息
     */
    PmsProductResult getUpdateInfo(@Param("id") Long id);
}
