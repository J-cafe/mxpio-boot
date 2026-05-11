package com.mxpioframework.log.service.impl;

import com.mxpioframework.security.util.SecurityUtils;
import com.mzt.logapi.beans.Operator;
import com.mzt.logapi.service.IOperatorGetService;
import org.springframework.stereotype.Service;

/**
 * 扩展bizlog-sdk的IOperatorGetService接口，自动获取操作人
 */
@Service
public class OperatorGetServiceImpl implements IOperatorGetService {
    @Override
    public Operator getUser() {
        Operator operatorObj = new Operator();
        String operator = SecurityUtils.getLoginUser()==null?"":"["+SecurityUtils.getLoginUser().getUsername()+"]"+SecurityUtils.getLoginUser().getNickname();
        operatorObj.setOperatorId(operator);
        return operatorObj;
    }
}
