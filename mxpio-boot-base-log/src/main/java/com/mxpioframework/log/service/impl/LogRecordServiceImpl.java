package com.mxpioframework.log.service.impl;

import com.mxpioframework.log.entity.MxpioLog;
import com.mxpioframework.log.service.MxpioLogService;
import com.mzt.logapi.beans.CodeVariableType;
import com.mzt.logapi.beans.LogRecord;
import com.mzt.logapi.service.ILogRecordService;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LogRecordServiceImpl implements ILogRecordService {
    @Autowired
    private MxpioLogService mxpioLogService;

    @Override
    public void record(LogRecord logRecord) {
        MxpioLog mxpioLog = new MxpioLog();
        mxpioLog.setType(logRecord.getType());
        mxpioLog.setSubType(logRecord.getSubType());
        mxpioLog.setBizNo(logRecord.getBizNo());
        mxpioLog.setOperator(logRecord.getOperator());
        mxpioLog.setAction(logRecord.getAction());
        mxpioLog.setExtra(logRecord.getExtra());
        mxpioLog.setCreateTime(new Date());
        mxpioLog.setSuccess(logRecord.isFail()?"0":"1");
        mxpioLog.setClazzName(MapUtils.isEmpty(logRecord.getCodeVariable())?"":logRecord.getCodeVariable().get(CodeVariableType.ClassName).toString());
        mxpioLog.setMethodName(MapUtils.isEmpty(logRecord.getCodeVariable())?"":logRecord.getCodeVariable().get(CodeVariableType.MethodName).toString());
        mxpioLogService.insertLog(mxpioLog);
    }

    @Override
    public List<LogRecord> queryLog(String bizNo, String type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<LogRecord> queryLogByBizNo(String bizNo, String type, String subType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
