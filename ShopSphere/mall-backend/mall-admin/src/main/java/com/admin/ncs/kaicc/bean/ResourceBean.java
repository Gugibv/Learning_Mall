package com.admin.ncs.kaicc.bean;


import lombok.Data;

/**
 * Spring Security中，资源被简化成一个字符串。
 * 而在自己设计资源时，可以设计不同类型的资源控制不同的行为。
 * 例如 菜单资源，Rest接口资源，页面控件资源等。
 */
@Data
public class ResourceBean {

    private String resourceId;
    private String resourceType;
    private String resourceName;

    public ResourceBean(){

    }

    public ResourceBean(String resourceId, String resourceName) {
        this.resourceId = resourceId;
        this.resourceName = resourceName;
    }

}