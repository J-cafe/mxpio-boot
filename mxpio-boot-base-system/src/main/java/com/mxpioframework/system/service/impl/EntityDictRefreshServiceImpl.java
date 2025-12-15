package com.mxpioframework.system.service.impl;

import com.mxpioframework.security.annotation.Dict;
import com.mxpioframework.system.service.DictCacheService;
import com.mxpioframework.system.service.EntityDictRefreshService;
import com.mxpioframework.system.service.PojoDictParseService;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component("mxpio.system.entityDictRefreshServiceImpl")
public class EntityDictRefreshServiceImpl implements EntityDictRefreshService {

    @Autowired
    private PojoDictParseService pojoDictParseService;

    @Autowired
    private DictCacheService cacheService;

    @Override
    public <T> void deleteCache(T entity) {
        Class entityClazz =  entity.getClass();
        BeanMap entityBeanMap = new BeanMap(entity);
        List<Dict> dictList = pojoDictParseService.getDictByEntity(entityClazz);
        for (Dict dict : dictList) {
            Object value = entityBeanMap.get(dict.dicCode());

            String cacheKey =
                    "EntityCache:" + dict.dicCode() + ":" + dict.dicEntity().getName() + ":" + Objects.toString(dict.dicText(), "") + ":"
                            + Objects.toString(value, "");
            cacheService.remove(cacheKey);
        }

    }
}
