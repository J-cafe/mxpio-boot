package com.mxpioframework.system.service.impl;

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

import javax.persistence.Entity;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;

import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.util.BeanReflectionUtils;
import com.mxpioframework.common.vo.Result;
import com.mxpioframework.jpa.annotation.DictAble;
import com.mxpioframework.security.annotation.Dict;
import com.mxpioframework.security.annotation.Translatable;
import com.mxpioframework.security.entity.BaseEntity;
import com.mxpioframework.security.entity.DictItem;
import com.mxpioframework.system.service.DictCacheService;
import com.mxpioframework.system.service.DictService;
import com.mxpioframework.system.service.PojoDictParseService;

import lombok.extern.slf4j.Slf4j;

@Component("mxpio.system.pojoDictParseService")
@Slf4j
public class PojoDictParseServiceImpl implements PojoDictParseService {

  final private Pattern pattern = Pattern.compile("\\$\\{\\S+\\}");
  private DictService dictService;
  final private Map<Class<?>, Map<String, Dict>> dictListOfClassMap = new ConcurrentHashMap<>();
  private DictCacheService cacheService;
  private static final Map<Class<?>, Map<String, Field>> declaredFieldsCache =
      new ConcurrentReferenceHashMap<>(256);

  @Autowired
  public void setDictService(DictService dictService) {
    this.dictService = dictService;
  }

  @Autowired
  public void setCacheService(DictCacheService cacheService) {
    this.cacheService = cacheService;
  }


  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public void parseDictResult(Result result) {
    Object resultContent = result.getResult();
    if (resultContent == null) {
      return;
    }

    if (resultContent instanceof Page) {
      Page page = (Page) resultContent;
      parseDictTextCollection(page.getContent());
    } else {
      try {
        parseDictPojo(resultContent);
      } catch (Exception e) {
        log.error("parseDictPojo", e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private <T extends Annotation> T getAnnotationOfField(Class<?> clazz, String property,
                                                        Class<T> annotationClass) {
    Field field;
    Annotation annotation = null;
    Class<?> currCls = clazz;
    while (!currCls.equals(Object.class) && annotation == null) {
      try {
        field = getDeclaredFields(currCls, property);
        if (field == null) {
          currCls = currCls.getSuperclass();
          continue;
        }
        annotation = field.getAnnotation(annotationClass);
        if(annotation == null){
          return null;
        }
      } catch (SecurityException e) {
        log.error("获取属性注解失败:" + clazz, e);
      }
    }
    return (T) annotation;
  }

  private Map<String, Dict> loadDictInfo(Class<?> clazz) {
    Map<String, Dict> dictInfoMap = new HashMap<>();
    BeanInfo beanInfo;
    try {
      beanInfo = Introspector.getBeanInfo(clazz, Object.class);
    } catch (IntrospectionException e) {
      log.error("introspection error", e);
      return dictInfoMap;
    }

    PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
    for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {

      Method readMethod = propertyDescriptor.getReadMethod();
      String propertyName = propertyDescriptor.getName();

      if("textMap".equals(propertyName)){
    	  continue;
      }

      if (readMethod == null) {
        continue;
      }

      if (isNestedObject(readMethod.getReturnType())) {
        dictInfoMap.put(propertyName, NESTED_DICT);
        continue;
      }

      Dict dict = readMethod.getAnnotation(Dict.class);
      if (dict == null) {
        dict = getAnnotationOfField(clazz, propertyName, Dict.class);
      }

      if (dict == null) {
        continue;
      }
      dictInfoMap.put(propertyName, dict);
    }
    return dictInfoMap;
  }

  private boolean isNestedObject(Class<?> clazz) {
    // 集合类型，对象数组，嵌套转化
    if (Collection.class.isAssignableFrom(clazz) || clazz.getName().startsWith("[L")) {
      return true;
    }

    // 不支持的类不嵌套转化
    if (!isSupported(clazz)) {
      return false;
    }

    // 其它对象嵌套转化
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
     * 显示属性名称
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

  private static Set<Class<?>> supportedClazzSet = new HashSet<>();
  private static Set<Class<? extends Annotation>> supportedAnnonationSet = new HashSet<>();

  public static void addSupportedType(Class<?>... objs) {
    for (Class<?> object : objs) {
      supportedClazzSet.add(object);
    }
  }

  public static void removeSupportedType(Class<?>... objs) {
    for (Class<?> object : objs) {
      supportedClazzSet.remove(object);
    }
  }

  @SafeVarargs
  public static void addSupportedAnnonation(Class<? extends Annotation>... clazzs) {
    for (Class<? extends Annotation> class1 : clazzs) {
      supportedAnnonationSet.add(class1);
    }
  }

  @SafeVarargs
  public static void removeSupportedAnnonation(Class<? extends Annotation>... clazzs) {
    for (Class<? extends Annotation> class1 : clazzs) {
      supportedAnnonationSet.remove(class1);
    }
  }

  static {
    addSupportedType(DictAble.class);
    addSupportedAnnonation(Entity.class, Translatable.class);
  }

  private static Map<Class<?>, Boolean> supportedCache = new ConcurrentHashMap<>();

  static boolean isSupported(Class<?> clazz) {
    return supportedCache.computeIfAbsent(clazz, (x) -> calcSupported(x));
  }

  static boolean calcSupported(Class<?> clazz) {
    if (clazz.isPrimitive()) {
      return false;
    }

    for (Class<?> supportedClazz : supportedClazzSet) {
      if (supportedClazz.isAssignableFrom(clazz)) {
        return true;
      }
    }

    for (Class<? extends Annotation> class1 : supportedAnnonationSet) {
      Annotation[] annotations = clazz.getAnnotationsByType(class1);
      if (annotations.length > 0) {
        return true;
      }
    }

    return false;
  }


  @SuppressWarnings("unchecked")
  private void parseDictPojo(Object pojo) {

    if (pojo instanceof Collection) {
      parseDictTextCollection((Collection<Object>) pojo);
      return;
    } else if (pojo.getClass().getName().startsWith("[L")) {
      // object array
      parseDictTextArray((Object[]) pojo);
      return;
    }

    Class<?> clazz = BeanReflectionUtils.getClass(pojo);

    if (!isSupported(clazz)) {
      return;
    }

    BeanMap itemBeanMap = new BeanMap(pojo);
    for (Map.Entry<String, Dict> entry : getDictMapping(clazz).entrySet()) {
      try {
        convertProperty(itemBeanMap, pojo, entry);
      } catch (Exception e) {
        log.error("error when convert property.", e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void convertProperty(BeanMap itemBeanMap, Object pojo, Map.Entry<String, Dict> entry) {
    String property = entry.getKey();
    Object value = itemBeanMap.get(property);
    if (value == null) {
      return;
    }

    Dict dict = entry.getValue();

    if (NESTED_DICT.equals(dict)) {
      if (value instanceof Collection) {
        parseDictTextCollection((Collection<Object>) value);
      } else {
        parseDictPojo(value);
      }
    } else {
      String dictCode = convertDictCode(dict.dicCode(), itemBeanMap);
      // 翻译字典值对应的txt
      String text = translateDictValue(dictCode, dict.dicEntity(), dict.dicText(),
          Objects.toString(value, null));

      log.debug(" 字典Text : " + text);
      log.debug(" __翻译字典字段__ " + property + CommonConstant.DICT_TEXT_SUFFIX + "： " + text);

      if (pojo instanceof DictAble) {
        ((DictAble) pojo).putText(property + CommonConstant.DICT_TEXT_SUFFIX, text);
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

  private void parseDictTextArray(Object[] arr) {
    if (arr == null) {
      return;
    }
    for (Object record : arr) {
      try {
        parseDictPojo(record);
      } catch (Exception e) {
        log.error("parseDictPojo", e);
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
      cacheService.put("DictCache:" + code, dictMapping);
    }
    return dictMapping;
  }

  private String parseDictValue(String code, String value) {
    return getDictMappings(code).get(value);
  }

  private String parseEntityDictValue(String code, Class<? extends BaseEntity> clazz,
      String dicText, String value) {
    String cacheKey =
        "EntityCache:" + code + ":" + clazz.getName() + ":" + Objects.toString(dicText, "") + ":"
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
   * 翻译字典文本
   *
   * @param code 字典编码
   * @param valueStr
   * @return
   */
  private String translateDictValue(String code, Class<? extends BaseEntity> clazz, String dicText,
      String valueStr) {
    if (StringUtils.isEmpty(valueStr) || "null".equals(valueStr)) {
      return null;
    }
    StringBuilder text = new StringBuilder();
    String[] values = valueStr.split(",");
    for (String value : values) {
      String tmpText = null;
      log.debug(" 字典 value : " + value);
      if (value.trim().length() == 0) {
        continue; // 跳过循环
      }
      if (clazz == null) {
        continue; // 跳过循环
      }

      if (DictItem.class.equals(clazz)) {
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
   * 处理code字符串中的${变量}
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
    if (dictCode.contains("${")) {// 增加处理code中的动态变量
      dictCode = replace(dictCode, itemBeanMap);
    }
    return dictCode;
  }

  private static Field getDeclaredFields(Class<?> clazz, String name) {
    Assert.notNull(clazz, "Class must not be null");
    Map<String, Field> resultMap = declaredFieldsCache.get(clazz);
    if (resultMap == null) {
      Field[] fields = clazz.getDeclaredFields();
      Map<String, Field> myResultMap = new HashMap<>();
      for (Field field : fields) {
        myResultMap.put(field.getName(), field);
      }
      declaredFieldsCache.put(clazz, myResultMap);
      resultMap = myResultMap;
    }
    return resultMap.get(name);
  }

  /**
   * 根据类名和字段名获取Dict注解
   * @param clazz
   * @param field
   * @return
   */
  @Override
  @Transactional
  public Dict getDictByClazzAndField(Class<?> clazz,String field){
    Map<String, Dict> dictInfo = loadDictInfo(clazz);
    return dictInfo.get(field);
  }

  /**
   * 根据静态数据字典(mb_dict/mb_dict_item)itemText反查itemValue
   * @param dictCode
   * @param itemText
   * @return
   */
  @Override
  @Transactional
  public String getValueByText(String dictCode,String itemText){
    Map<String, String> mappings = getDictMappings(dictCode);
    for(Map.Entry<String,String> entry:mappings.entrySet()){
      if(StringUtils.equals(itemText,entry.getValue())){
        return entry.getKey();
      }
    }
    return null;
  }

  /**
   * 对于动态数据字典，根据value反查text
   * @param dicCode
   * @param clazz
   * @param dicText
   * @param value
   * @return
   */
  @Override
  @Transactional
  public String getEntityValueByText(String dicCode, Class<? extends BaseEntity> clazz,
                                     String dicText, String value) {
    String cacheKey =
            "EntityTextCache:" + dicCode + ":" + clazz.getName() + ":" + Objects.toString(dicText, "") + ":"
                    + Objects.toString(value, "");

    String result = cacheService.get(cacheKey);
    if (result != null) {
      return result;
    }

    result = dictService.getEntityDictValueByText(dicCode, clazz, dicText, value);
    if (result == null) {
      result = "";
    }
    cacheService.put(cacheKey, result);
    return result;
  }

  @Override
  public List<Dict> getDictByEntity(Class<?> clazz){
      Map<String, Dict> dictInfoMap = new HashMap<>();
      for(Map<String,Dict> dictMap:dictListOfClassMap.values()){
          for(Dict dict:dictMap.values()){
            if(dict.dicEntity()!=null&&dict.dicEntity().equals(clazz)){
                String key = dict.dicCode()+dict.dicText();
                if(!dictInfoMap.containsKey(key)){
                    dictInfoMap.put(key,dict);
                }
            }
          }
      }
      return new ArrayList<>(dictInfoMap.values());
  }

}
