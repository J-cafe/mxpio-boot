package com.mxpioframework.log.provider;


import com.mxpioframework.log.service.MxpioLogService;
import com.mxpioframework.log.vo.LogParam;
import com.mxpioframework.log.vo.LogVO;
import com.mzt.logapi.beans.LogRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("mxpio.log.es.storage.provider")
@ConditionalOnClass(name = {"org.elasticsearch.client.RestHighLevelClient"})
public class ElasticsearchStorageProvider implements StorageProvider{

    private static final String PROVIDER_NAME = "es";

    @Resource(name="mxpio.log.es.logService")
    private MxpioLogService esLogService;

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public void saveLog(LogRecord logRecord) {
        esLogService.saveLog(logRecord);
    }

    @Override
    public Page<LogVO> listPage(Pageable pageable, LogParam param) {
        return esLogService.listPage(pageable,param);
    }
}
