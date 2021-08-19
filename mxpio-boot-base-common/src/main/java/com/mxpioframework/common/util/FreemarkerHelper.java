package com.mxpioframework.common.util;

import java.io.StringWriter;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FreemarkerHelper {
    private static Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);

    public static String process(String templatePath, String encode, Map<String, Object> data) {
        try {
        	configuration.setDefaultEncoding(encode);
            StringWriter write = new StringWriter();
            Template template = null;
            template = configuration.getTemplate(templatePath, encode);
            template.process(data, write);
            return write.toString();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return e.toString();
        }
    }

    public static String process(String templatePath, Map<String, Object> data) {
        return process(templatePath, "utf-8", data);
    }

    public static void main(String[] args) {
        String tableTemplate = process("org/jeecg/modules/online/config/engine/tableTemplate.ftl", null);
        System.out.println(tableTemplate);
    }

    static {
    	configuration.setNumberFormat("0.#####################");
    	configuration.setClassForTemplateLoading(FreemarkerHelper.class, "/");
    }
}
