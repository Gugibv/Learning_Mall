package com.grey.admin.bean;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserBean {

    private String userId;
    private String userName;
    private String userPass;
    private List<RoleBean> userRoles = new ArrayList<>();
    private List<ResourceBean> resourceBeans = new ArrayList<>();

    public UserBean(){

    }
    public UserBean(String userId, String userName, String userPass) {
        this.userId = userId;
        this.userName = userName;
        this.userPass = userPass;
    }

    public boolean havaPermission(String resource) {
        return this.resourceBeans.stream()
                .filter(resourceBean -> resourceBean.getResourceName().equals(resource))
                .count()>0;
    }

}