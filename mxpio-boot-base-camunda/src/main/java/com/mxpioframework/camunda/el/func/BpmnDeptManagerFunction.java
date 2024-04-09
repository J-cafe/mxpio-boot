package com.mxpioframework.camunda.el.func;

import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import com.mxpioframework.expression.func.type.AbstractSpringAviatorFunction;
import com.mxpioframework.security.entity.Dept;
import com.mxpioframework.security.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class BpmnDeptManagerFunction extends AbstractSpringAviatorFunction {

    @Autowired
    private DeptService deptService;

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject deptLevelObj, AviatorObject usernameObj) {
        String deptLevel = FunctionUtils.getStringValue(deptLevelObj, env);
        String username = FunctionUtils.getStringValue(usernameObj, env);
        return new AviatorString(getDeptManagerByUser(deptLevel, username));
    }
    @Override
    public String getName() {
        return "deptManager";
    }

    @Override
    public boolean disabled() {
        return false;
    }

    @Override
    public String getDesc() {
        return "使用方法：deptManager(deptLevel, username)";
    }

    public String getDeptManagerByUser(String deptLevel, String username) {
        Map<String, Set<String>> userDepts = deptService.getAllDeptIdGroupByUser();
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
