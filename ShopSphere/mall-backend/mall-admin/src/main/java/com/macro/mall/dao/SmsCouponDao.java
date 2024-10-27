package com.macro.mall.dao;

import com.macro.mall.dto.SmsCouponParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 优惠券管理自定义查询Dao
 * Created on 2018/8/29.
 */
@Mapper
public interface SmsCouponDao {
    SmsCouponParam getItem(@Param("id") Long id);
}
