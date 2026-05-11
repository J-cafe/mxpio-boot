package com.mxpioframework.module.datav.service.impl;

import com.mxpioframework.common.ds.DataSet;
import com.mxpioframework.common.ds.provider.DataSetProvider;
import com.mxpioframework.module.datav.service.DataSetService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(DataSetService.BEAN_ID)
public class DataSetServiceImpl implements DataSetService, InitializingBean {

    @Autowired(required = false)
    private List<DataSetProvider> providers;

    public static Map<String, String> types = new HashMap<>();

    public static Map<String, DataSetProvider> providerMap = new HashMap<>();

    private DataSetProvider getProvider(String type){
        return providerMap.get(type);
    }

    @Override
    public List<? extends DataSet> getDataSets(String type) {
        DataSetProvider provider = getProvider(type);
        return provider==null?null:provider.getDataSets();
    }

    @Override
    public DataSet getDataSet(String type, String code) {
        DataSetProvider provider = getProvider(type);
        return provider==null?null:provider.getDataSet(code);
    }

    @Override
    public List<?> getData(String type, String code) {
        DataSetProvider provider = getProvider(type);
        return provider==null?null:provider.getResult(provider.getDataSet(code));
    }

    @Override
    public Page<?> pagingData(String type, String code, Pageable pageAble) {
        DataSetProvider provider = getProvider(type);
        return provider==null?null:provider.getPagingResult(provider.getDataSet(code), pageAble);
    }

    @Override
    public Map<String, String> getTypes() {
        return types;
    }

    @Override
    @Transactional
    public DataSet save(String type, String json) {

        DataSetProvider provider = providerMap.get(type);
        DataSet entity = provider.unserialize(json);
        provider.addDataSet(entity);
        return entity;
    }

    @Override
    @Transactional
    public DataSet update(String type, String json) {
        DataSetProvider provider = providerMap.get(type);
        DataSet entity = provider.unserialize(json);
        provider.updateDataSet(entity);
        return entity;
    }

    @Override
    @Transactional
    public void delete(String type, String code) {
        providerMap.get(type).deleteDataSet(code);
    }

    @Override
    @Transactional
    public void afterPropertiesSet() {
        if(providers == null){
            providers = new ArrayList<>();
        }
        for(DataSetProvider provider : providers){
            types.put(provider.getTypeKey(), provider.getTypeName());
            providerMap.put(provider.getTypeKey(), provider);
        }
    }
}
