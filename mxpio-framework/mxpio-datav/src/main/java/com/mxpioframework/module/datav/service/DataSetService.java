package com.mxpioframework.module.datav.service;

import com.mxpioframework.common.ds.DataSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface DataSetService {
    public static String BEAN_ID = "mxpio.datav.dataSetService";

    List<? extends DataSet> getDataSets(String type);

    DataSet getDataSet(String type, String code);

    List<?> getData(String type, String code);

    Page<?> pagingData(String type, String code, Pageable pageAble);

    Map<String, String> getTypes();

    DataSet save(String type, String json);

    DataSet update(String type, String json);

    void delete(String type, String code);
}
