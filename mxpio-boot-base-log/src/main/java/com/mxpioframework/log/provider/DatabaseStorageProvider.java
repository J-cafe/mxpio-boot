package com.mxpioframework.log.provider;

import com.mxpioframework.log.service.MxpioLogService;
import com.mxpioframework.log.vo.LogParam;
import com.mxpioframework.log.vo.LogVO;
import com.mzt.logapi.beans.LogRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service("mxpio.log.db.storage.provider")
public class DatabaseStorageProvider implements StorageProvider{

    private static final String PROVIDER_NAME = "db";

    @Resource(name="mxpio.log.db.logService")
    private MxpioLogService dbLogService;


    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Async
    @Override
    public void saveLog(LogRecord logRecord) {
        dbLogService.saveLog(logRecord);
    }

    @Override
    public Page<LogVO> listPage(Pageable pageable, LogParam param) {
        return dbLogService.listPage(pageable,param);
    }


}
