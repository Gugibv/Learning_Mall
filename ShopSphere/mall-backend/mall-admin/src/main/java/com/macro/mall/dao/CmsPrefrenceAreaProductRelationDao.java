package com.macro.mall.dao;

import com.macro.mall.model.CmsPrefrenceAreaProductRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自定义优选和商品关系操作
 * Created on 2018/4/26.
 */
@Mapper
public interface CmsPrefrenceAreaProductRelationDao {
    int insertList(@Param("list") List<CmsPrefrenceAreaProductRelation> prefrenceAreaProductRelationList);
}
