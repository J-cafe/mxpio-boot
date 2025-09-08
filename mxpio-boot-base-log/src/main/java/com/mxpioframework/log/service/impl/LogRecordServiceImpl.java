package com.mxpioframework.log.service.impl;

import com.mxpioframework.common.exception.MBootException;
import com.mxpioframework.log.provider.StorageProvider;
import com.mxpioframework.log.service.LogRecordService;
import com.mxpioframework.log.vo.LogParam;
import com.mxpioframework.log.vo.LogVO;
import com.mzt.logapi.beans.LogRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogRecordServiceImpl implements LogRecordService {

    @Value("${mxpio.log.provider:db}")
    public String providerName;

    @Autowired
    private List<StorageProvider> StorageProvider;

    @Override
    public void record(LogRecord logRecord) {
        for (StorageProvider storageProvider : StorageProvider) {
            if(StringUtils.equals(providerName,storageProvider.getProviderName())){
                storageProvider.saveLog(logRecord);
                return;
            }
        }
        throw new MBootException("未找到可用的日志存储provider");
    }

    @Override
    public List<LogRecord> queryLog(String bizNo, String type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<LogRecord> queryLogByBizNo(String bizNo, String type, String subType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Page<LogVO> listPage(Pageable pageable, LogParam param) {
        for (StorageProvider storageProvider : StorageProvider) {
            if(StringUtils.equals(providerName,storageProvider.getProviderName())){
                return storageProvider.listPage(pageable, param);
            }
        }
        throw new MBootException("未找到可用的日志存储provider");
    }
}
