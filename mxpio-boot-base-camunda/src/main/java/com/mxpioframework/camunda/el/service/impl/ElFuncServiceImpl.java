package com.mxpioframework.camunda.el.service.impl;

import com.mxpioframework.camunda.el.service.ElFuncService;
import com.mxpioframework.security.entity.Dept;
import com.mxpioframework.security.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service("bpmn")
public class ElFuncServiceImpl implements ElFuncService {

    @Autowired
    private DeptService deptService;

    @Override
    public String deptManager(String deptLevel, String username) {
        Map<String, Set<String>> userDepts = deptService.getAllDeptIdWithFatherGroupByUser();
        Set<String> deptIds = userDepts.get(username);
        Map<String, Dept> depts = deptService.getDeptMap();
        for(String id : deptIds){
            Dept dept = depts.get(id);
            if(deptLevel.equals(dept.getDeptLevel())){
                return dept.getDeptManager();
            }
        }
        return "";
    }

}
