package com.mxpioframework.system.service.impl;

import com.mxpioframework.common.util.BeanReflectionUtils;
import com.mxpioframework.common.vo.Result;
import com.mxpioframework.jpa.BaseEntity;
import com.mxpioframework.jpa.annotation.DictAble;
import com.mxpioframework.security.annotation.Dict;
import com.mxpioframework.security.entity.DictItem;
import com.mxpioframework.system.SystemConstant;
import com.mxpioframework.system.service.DictCacheService;
import com.mxpioframework.system.service.DictService;
import com.mxpioframework.system.service.PojoDictParseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class PojoDictParseServiceImpl implements PojoDictParseService {

    final private Pattern pattern = Pattern.compile("\\$\\{\\S+\\}");
    private DictService dictService;
    final private Map<Class<?>, Map<String, Dict>> dictListOfClassMap = new ConcurrentHashMap<>();
    private DictCacheService cacheService;

    @Autowired
    public void setDictService(DictService dictService) {
        this.dictService = dictService;
    }

    @Autowired
    public void setCacheService(DictCacheService cacheService) {
        this.cacheService = cacheService;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void parseDictResult(Result result) {
        Object resultContent = result.getResult();
        if (resultContent instanceof Page) {
            Page page = (Page) resultContent;
            parseDictTextCollection(page.getContent());
        } else if (resultContent instanceof Collection) {
            parseDictTextCollection((Collection) resultContent);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Annotation> T getAnnotationOfField(Class<?> clazz, String property, Class<T> annotationClass) {
        Field field;
        Annotation annotation = null;
        try {
            field = clazz.getDeclaredField(property);
            annotation = field.getAnnotation(annotationClass);
        } catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return getAnnotationOfField(superClass, property, annotationClass);
            } else {
                log.error("????????????????????????", e);
            }
        } catch (SecurityException e) {
            log.error("????????????????????????", e);
        }
        return (T) annotation;
    }

    private Map<String, Dict> loadDictInfo(Class<?> clazz) {
        Map<String, Dict> dictInfoMap = new HashMap<>();
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
            log.error("introspection error", e);
            return dictInfoMap;
        }

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {

            Method readMethod = propertyDescriptor.getReadMethod();
            if (readMethod == null) {
                continue;
            }

            if (isNestedObject(readMethod.getReturnType())) {
                dictInfoMap.put(propertyDescriptor.getName(), NESTED_DICT);
                continue;
            }

            Dict dict = readMethod.getAnnotation(Dict.class);
            if (dict == null) {
                dict = getAnnotationOfField(clazz, propertyDescriptor.getName(), Dict.class);
            }

            if (dict == null) {
                continue;
            }
            dictInfoMap.put(propertyDescriptor.getName(), dict);
        }
        return dictInfoMap;
    }

    private boolean isNestedObject(Class<?> clazz) {
        // ???????????????????????????
        if (Collection.class.isAssignableFrom(clazz)) {
            return true;
        }

        // Java????????????????????????
        if (clazz.isPrimitive() || clazz.getPackage().getName().startsWith("java.")) {
            return false;
        }

        // ????????????????????????
        return true;
    }


    private static final Dict NESTED_DICT = new Dict() {
        @Override
        public Class<? extends Annotation> annotationType() {
            return null;
        }

        @Override
        public String dicCode() {
            return "collection";
        }

        @Override
        public Class<? extends BaseEntity> dicEntity() {
            return null;
        }

        @Override
        public String dicText() {
            return null;
        }

        /**
         * ??????????????????
         */
        @Override
        public String displayProp() {
            return null;
        }
    };

    Map<String, Dict> getDictMapping(Class<?> clazz) {
        Map<String, Dict> dictInfos = dictListOfClassMap.get(clazz);
        if (dictInfos == null) {
            synchronized (this) {
                dictInfos = dictListOfClassMap.get(clazz);
                if (dictInfos == null) {
                    dictInfos = loadDictInfo(clazz);
                    dictListOfClassMap.put(clazz, dictInfos);
                }
            }
        }
        return dictInfos;

    }

    @SuppressWarnings("unchecked")
    private void parseDictPojo(Object pojo) {
        Class<?> clazz = BeanReflectionUtils.getClass(pojo);

        Map<String, Dict> dictInfo = getDictMapping(clazz);
        BeanMap itemBeanMap = new BeanMap(pojo);
        for (Map.Entry<String, Dict> entry : dictInfo.entrySet()) {
            String property = entry.getKey();
            Object value = itemBeanMap.get(property);
            if (value instanceof Collection) {
                parseDictTextCollection((Collection<Object>) value);
            } else {
                Dict dict = entry.getValue();
                String dictCode = convertDictCode(dict.dicCode(), itemBeanMap);
                // ????????????????????????txt
                String text = translateDictValue(dictCode, dict.dicEntity(), dict.dicText(),
                  Objects.toString(value, null));

                log.debug(" ??????Text : " + text);
                log.debug(" __??????????????????__ " + property + SystemConstant.DICT_TEXT_SUFFIX + "??? " + text);

                if (pojo instanceof DictAble) {
                    ((DictAble) pojo).putText(property + SystemConstant.DICT_TEXT_SUFFIX, text);
                }

                String labelProp = dict.displayProp();
                if (StringUtils.isNotEmpty(dict.displayProp()) && itemBeanMap.containsKey(labelProp)) {
                    try {
                        itemBeanMap.put(labelProp, text);
                    } catch (Exception e) {
                        log.error("set {} to {} failed", text, labelProp, e);
                    }
                }
            }
        }
    }

    private void parseDictTextCollection(Collection<Object> collection) {
        if (collection == null) {
            return;
        }
        for (Object record : collection) {
            try {
                parseDictPojo(record);
            } catch (Exception e) {
                log.error("parseDictPojo", e);
            }

        }
    }

    private Map<String, String> getDictMappings(String code) {
        Map<String, String> dictMapping = cacheService.get("DictCache:" + code);
        if (dictMapping == null) {
            dictMapping = dictService.getDictMappingByCode(code);
            cacheService.put(code, dictMapping);
        }
        return dictMapping;
    }

    private String parseDictValue(String code, String value) {
        return getDictMappings(code).get(value);
    }

    private String parseEntityDictValue(String code, Class<? extends BaseEntity> clazz, String dicText, String value) {
        String cacheKey = "EntityCache:" + code + ":" + clazz.getName() + ":" + Objects.toString(dicText, "") + ":"
          + Objects.toString(value, "");

        String result = cacheService.get(cacheKey);
        if (result != null) {
            return result;
        }

        result = dictService.getEntityDictText(code, clazz, dicText, value);
        if (result == null) {
            result = "";
        }
        cacheService.put(cacheKey, result);
        return result;
    }

    /**
     * ??????????????????
     *
     * @param code     ????????????
     * @param valueStr
     * @return
     */
    private String translateDictValue(String code, Class<? extends BaseEntity> clazz, String dicText, String valueStr) {
        if (StringUtils.isEmpty(valueStr) || "null".equals(valueStr)) {
            return null;
        }
        StringBuilder text = new StringBuilder();
        String[] values = valueStr.split(",");
        for (String value : values) {
            String tmpText = null;
            log.debug(" ?????? value : " + value);
            if (value.trim().length() == 0) {
                continue; // ????????????
            }

            if (clazz.equals(DictItem.class)) {
                tmpText = parseDictValue(code, value);
            } else {
                tmpText = parseEntityDictValue(code, clazz, dicText, value);
            }
            if (tmpText != null) {
                if (!"".equals(text.toString())) {
                    text.append(",");
                }
                text.append(tmpText);
            }

        }
        return text.toString();
    }

    /**
     * ??????code???????????????${??????}
     */
    @SuppressWarnings("rawtypes")
    private String replace(String context, Map item) {
        String result = context;
        Matcher matcher = pattern.matcher(context);
        while (matcher.find()) {
            String name = matcher.group(0);
            String cleanName = name;
            cleanName = cleanName.replace("${", "");
            cleanName = cleanName.replace("}", "");
            String variableValue = String.valueOf(item.get(cleanName));
            if (variableValue == null) {
                variableValue = "";
            }
            result = result.replace(name, variableValue);
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    private String convertDictCode(String dictCode, Map itemBeanMap) {
        if (dictCode.contains("${")) {// ????????????code??????????????????
            dictCode = replace(dictCode, itemBeanMap);
        }
        return dictCode;
    }

}
