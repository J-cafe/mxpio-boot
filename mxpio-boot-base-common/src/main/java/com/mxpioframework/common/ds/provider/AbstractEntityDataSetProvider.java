package com.mxpioframework.common.ds.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxpioframework.common.ds.DataSet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractEntityDataSetProvider implements DataSetProvider {

    static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T extends DataSet> T unserialize(String json){
        try {
            return objectMapper.readValue(json, getClazz());
        } catch (JsonProcessingException e) {
            log.error("格式错误！");
            return null;
        }
    }

    public abstract <T extends DataSet> Class<T> getClazz();
}
