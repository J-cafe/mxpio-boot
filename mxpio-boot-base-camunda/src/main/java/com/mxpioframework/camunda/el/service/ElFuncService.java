package com.mxpioframework.camunda.el.service;

import com.mxpioframework.security.entity.Dept;

import java.util.List;

public interface ElFuncService {

    /**
     * 根据组织级别和发起人获取审批人
     * @param deptLevel 组织级别
     * @param username 发起人
     * @return 审批人
     */
    String deptManager(String deptLevel, String username);

    /**
     * 根据部门级别获取所属部门对象
     * @param deptLevel 级别
     * @param username 用户名
     * @return 所属部门
     */
    Dept dept(String deptLevel, String username);

    List<String> string2List(String str);

}
