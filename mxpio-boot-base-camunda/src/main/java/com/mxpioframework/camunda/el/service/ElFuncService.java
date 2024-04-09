package com.mxpioframework.camunda.el.service;

public interface ElFuncService {

    /**
     * 根据组织级别和发起人获取审批人
     * @param deptLevel 组织级别
     * @param username 发起人
     * @return 审批人
     */
    public String deptManager(String deptLevel, String username);
}
