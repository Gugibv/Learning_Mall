package com.macro.mall.dao;

import com.macro.mall.model.UmsAdminPermissionRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户权限自定义Dao
 * Created on 2018/10/8.
 */
@Mapper
public interface UmsAdminPermissionRelationDao {
    int insertList(@Param("list") List<UmsAdminPermissionRelation> list);
}
