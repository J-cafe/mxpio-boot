package com.mxpioframework.log.service;

import com.mxpioframework.log.vo.LogParam;
import com.mxpioframework.log.vo.LogVO;
import com.mzt.logapi.beans.LogRecord;
import com.mzt.logapi.service.ILogRecordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LogRecordService extends ILogRecordService {
    Page<LogVO> listPage(Pageable pageable, LogParam param);

    default List<LogRecord> queryLog(String bizNo, String type){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    default List<LogRecord> queryLogByBizNo(String bizNo, String type, String subType){
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
