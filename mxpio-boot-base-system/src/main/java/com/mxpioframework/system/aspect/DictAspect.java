package com.mxpioframework.system.aspect;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxpioframework.common.annotation.Dict;
import com.mxpioframework.common.util.BeanReflectionUtils;
import com.mxpioframework.common.vo.Result;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.system.SystemConstant;
import com.mxpioframework.system.entity.DictItem;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Aspect
@Component
@Slf4j
public class DictAspect {
	
    private Pattern pattern = Pattern.compile("\\$\\{\\S+\\}");
    // 定义切点Pointcut
    @Pointcut("execution(public * *..*.*Controller.*(..))")
    public void excudeService() {
    }

    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
    	long time1=System.currentTimeMillis();
        Object result = pjp.proceed();
        long time2=System.currentTimeMillis();
        log.debug("获取JSON数据 耗时："+(time2-time1)+"ms");
        long start=System.currentTimeMillis();
        this.parseDictText(result);
        long end=System.currentTimeMillis();
        log.debug("解析注入JSON数据  耗时"+(end-start)+"ms");
        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void parseDictText(Object result) {
        if (result instanceof Result) {
            if (((Result) result).getResult() instanceof Page) {
                List<JSONObject> items = new ArrayList<>();
                Page page = (Page) ((Result) result).getResult();
                boolean b = parseDictText(items, page.getContent());

                if(b) {
                	((Result) result).setResult(new PageImpl<JSONObject>(items, page.getPageable(), page.getTotalElements()));
                }
            }else if(((Result) result).getResult() instanceof List) {
            	List<JSONObject> items = new ArrayList<>();
                boolean b = parseDictText(items, (List) ((Result) result).getResult());
                if(b) {
                	((Result) result).setResult(items);
                }
            }
        }
    }

    @SuppressWarnings("rawtypes")
	private boolean parseDictText(List<JSONObject> items, List list) {
    	for (Object record : list) {
            ObjectMapper mapper = new ObjectMapper();
            String json="{}";
            try {
                //解决@JsonFormat注解解析不了的问题详见SysAnnouncement类的@JsonFormat
                 json = mapper.writeValueAsString(record);

            } catch (JsonProcessingException e) {
            	log.error("json解析失败"+e.getMessage(),e);
            	return false;
            }
            try {
            	JSONObject item = JSONObject.parseObject(json);

                for (Field field : BeanReflectionUtils.loadClassFields(BeanReflectionUtils.getClass(record))) {
                	if("serialVersionUID".equals(field.getName())) {
                		continue;
                	}
                	Object value = BeanReflectionUtils.getProperty(record, field.getName());
                	if(value instanceof List) {
                		List<JSONObject> subItems = new ArrayList<>();
                		boolean b = parseDictText(subItems, (List) value);
                        if(b) {
                        	// BeanReflectionUtils.setProperty(record, field.getName(), subItems);
                            item.put(field.getName(), subItems);
                        }
                	} else if (field.getAnnotation(Dict.class) != null) {
                        String code = field.getAnnotation(Dict.class).dicCode();
                        String valueStr = String.valueOf(item.get(field.getName()));
                        if (code.contains("${")){//增加处理code中的动态变量
                            code = replace(code,item);
                        }
                        //翻译字典值对应的txt
                        String text = translateDictValue(code, valueStr);

                        log.debug(" 字典Text : "+ text);
                        log.debug(" __翻译字典字段__ "+field.getName() + SystemConstant.DICT_TEXT_SUFFIX+"： "+ text);
                        item.put(field.getName() + SystemConstant.DICT_TEXT_SUFFIX, text);
                    }
                    // date类型默认转换string格式化日期
                    if (field.getType().getName().equals("java.util.Date")&&field.getAnnotation(JsonFormat.class)==null&&item.get(field.getName())!=null){
                        SimpleDateFormat aDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        item.put(field.getName(), aDate.format(new Date((Long) item.get(field.getName()))));
                    }
                    // 敏感字段过滤
                    if(record instanceof User && "password".equals(field.getName())) {
                    	item.remove(field.getName());
                    }
                }
                items.add(item);
            }catch (Exception e) {
            	e.printStackTrace();
            	return false;
			}
        }
    	return true;
    }

    /**
     *  翻译字典文本
     * @param code
     * @param valueStr
     * @return
     */
    @Transactional(readOnly = true)
    private String translateDictValue(String code, String valueStr) {
    	if(StringUtils.isEmpty(valueStr)) {
    		return null;
    	}
        StringBuffer text = new StringBuffer();
        String[] values = valueStr.split(",");
        for (String value : values) {
            String tmpText = null;
            log.debug(" 字典 value : "+ value);
            if (value.trim().length() == 0) {
                continue; //跳过循环
            }
            DictItem item = JpaUtil.linq(DictItem.class).equal("itemValue", value.trim()).exists(Dict.class).equal("dictCode", code).equalProperty("id", "dictId").end().findOne();
            tmpText = item.getItemText();
            
            if (tmpText != null) {
                if (!"".equals(text.toString())) {
                	text.append(",");
                }
                text.append(tmpText);
            }

        }
        return text.toString();
    }

    //处理code字符串中的${变量}
    private String replace(String context,JSONObject item){
        String result = context;
        Matcher matcher = pattern.matcher(context);
        while (matcher.find()){
            String name = matcher.group(0);
            String cleanName = name;
            cleanName=cleanName.replace("${","");
            cleanName=cleanName.replace("}","");
            String variableValue = String.valueOf(item.get(cleanName));
            if(variableValue == null){
                variableValue = "";
            }
            result = result.replace(name, variableValue);
        }
        return result;
    }
}
