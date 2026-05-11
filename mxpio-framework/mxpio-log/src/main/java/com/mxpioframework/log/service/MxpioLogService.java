package com.mxpioframework.log.service;

import com.mxpioframework.log.vo.LogParam;
import com.mxpioframework.log.vo.LogVO;
import com.mzt.logapi.beans.LogRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MxpioLogService {

    void saveLog(LogRecord logRecord);

    Page<LogVO> listPage(Pageable page , LogParam param);

}
