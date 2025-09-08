package com.mxpioframework.log.provider;

import com.mxpioframework.log.vo.LogParam;
import com.mxpioframework.log.vo.LogVO;
import com.mzt.logapi.beans.LogRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StorageProvider {

    String getProviderName();

    void saveLog(LogRecord logRecord);

    Page<LogVO> listPage(Pageable pageable, LogParam param);

}
