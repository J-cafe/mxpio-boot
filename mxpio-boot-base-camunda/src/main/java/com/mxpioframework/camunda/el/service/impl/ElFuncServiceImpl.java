package com.mxpioframework.camunda.el.service.impl;

import com.mxpioframework.camunda.el.service.ElFuncService;
import com.mxpioframework.security.entity.Dept;
import com.mxpioframework.security.service.RbacCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("bpmn")
public class ElFuncServiceImpl implements ElFuncService {

    @Autowired
    private RbacCacheService rbacCacheService;

    @Override
    public String deptManager(String deptLevel, String username) {
        Dept dept = getDeptByUserAndLevel(deptLevel, username);
        if(dept != null){
            return dept.getDeptManager();
        }else{
            return "";
        }
    }

    @Override
    public String deptManager(String deptCode) {
        Dept dept = getDeptByDeptCode(deptCode);
        if(dept != null){
            return dept.getDeptManager();
        }else{
            return "";
        }
    }

    @Override
    public Dept dept(String deptLevel, String username) {
        return getDeptByUserAndLevel(deptLevel, username);
    }

    @Override
    public String deptCode(String deptLevel, String username) {
        Dept dept = dept(deptLevel, username);
        if(dept != null){
            return dept.getDeptCode();
        }
        return null;
    }

    @Override
    public String deptName(String deptLevel, String username) {
        Dept dept = dept(deptLevel, username);
        if(dept != null){
            return dept.getDeptName();
        }
        return null;
    }

    @Override
    public List<String> string2List(String str) {
        return Arrays.asList(str.split(","));
    }

    private Dept getDeptByUserAndLevel(String deptLevel, String username){
        Map<String, Set<String>> userDepts = rbacCacheService.getAllDeptIdWithFatherGroupByUser();
        Set<String> deptIds = userDepts.get(username);
        Map<String, Dept> depts = rbacCacheService.getDeptMap();
        if(!CollectionUtils.isEmpty(deptIds)){
            for(String id : deptIds){
                Dept dept = depts.get(id);
                if(deptLevel.equals(dept.getDeptLevel())){
                    return dept;
                }
            }
        }

        return null;
    }

    private Dept getDeptByDeptCode(String deptCode){
        Map<String, Dept> depts = rbacCacheService.getDeptMapByCode();

        return depts.get(deptCode);
    }

}
