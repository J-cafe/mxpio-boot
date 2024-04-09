package com.mxpioframework.camunda.el.service.impl;

import com.googlecode.aviator.AviatorEvaluator;
import com.mxpioframework.camunda.el.service.AviatorFuncService;
import org.springframework.stereotype.Service;

@Service("$bpmn")
public class AviatorFuncServiceImpl implements AviatorFuncService {
    @Override
    public String exe(String funcStr) {
        String[] strs = funcStr.split(",");
        StringBuffer func = new StringBuffer();
        for (int i = 0; i < strs.length; i++) {
            if(i == 0){
                func.append(strs[i]).append("(");
            }else{
                func.append(strs[i]).append(",");
            }
        }
        if (func.charAt(func.length() - 1) == '('){
            func.append(")");
        }else {
            func.setLength(func.length() - 1);
            func.append(")");
        }
        return AviatorEvaluator.execute(func.toString())+"";
    }
}
