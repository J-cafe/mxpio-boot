package com.mxpioframework.log.service.impl;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.log.entity.MxpioLog;
import com.mxpioframework.log.service.MxpioLogService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MxpioLogServiceImpl implements MxpioLogService {

    @Override
    @Async
    @Transactional
    public void insertLog(MxpioLog mxpioLog) {
        JpaUtil.save(mxpioLog);
    }
}
