package com.mxpioframework.log.service.impl;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.log.entity.MxpioLog;
import com.mxpioframework.log.service.MxpioLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MxpioLogServiceImpl implements MxpioLogService {

    @Override
    @Async
    @Transactional
    public void insertLog(MxpioLog mxpioLog) {
        JpaUtil.save(mxpioLog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MxpioLog> list(Criteria criteria){
        return JpaUtil.linq(MxpioLog.class).where(criteria).list();
    }


    @Override
    @Transactional(readOnly = true)
    public Page<MxpioLog> listPage(Pageable page , Criteria criteria){
        return JpaUtil.linq(MxpioLog.class).where(criteria).paging(page);
    }

    @Override
    @Transactional(readOnly = true)
    public MxpioLog getById(Long id){
        return JpaUtil.getOne(MxpioLog.class,id);
    }
}
