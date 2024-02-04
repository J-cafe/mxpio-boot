package com.mxpioframework.system.service;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.security.annotation.Dict;
import com.mxpioframework.security.entity.BaseEntity;

public interface PojoDictParseService {
  @SuppressWarnings({"rawtypes"})
  void parseDictResult(Result result);

  Dict getDictByClazzAndField(Class<?> clazz, String field);

  String getValueByText(String dictCode, String itemText);

  String getEntityValueByText(String dicCode, Class<? extends BaseEntity> clazz,
                              String dicText, String value);
}
