package com.mxpioframework.camunda.el.service.impl;

import com.mxpioframework.camunda.el.service.ElFuncService;
import com.mxpioframework.security.entity.Dept;
import com.mxpioframework.security.service.RbacCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service("bpmn")
public class ElFuncServiceImpl implements ElFuncService {

    @Autowired
    private RbacCacheService rbacCacheService;

    @Override
    public String deptManager(String deptLevel, String username) {
        Map<String, Set<String>> userDepts = rbacCacheService.getAllDeptIdWithFatherGroupByUser();
        Set<String> deptIds = userDepts.get(username);
        Map<String, Dept> depts = rbacCacheService.getDeptMap();
        for(String id : deptIds){
            Dept dept = depts.get(id);
            if(deptLevel.equals(dept.getDeptLevel())){
                return dept.getDeptManager();
            }
        }
        return "";
    }

}
