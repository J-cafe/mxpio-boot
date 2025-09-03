package com.mxpioframework.log.service;

import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.log.entity.MxpioLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MxpioLogService {

    void insertLog(MxpioLog mxpioLog);

    List<MxpioLog> list(Criteria criteria);

    Page<MxpioLog> listPage(Pageable page , Criteria criteria);

    MxpioLog getById(Long id);


}
