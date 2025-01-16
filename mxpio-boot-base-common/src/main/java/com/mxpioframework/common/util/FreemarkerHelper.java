package com.mxpioframework.common.util;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FreemarkerHelper {
    private static final Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);

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
    
    public static void process(String templatePath, String encode, Map<String, Object> data, Writer write) {
        try {
        	configuration.setDefaultEncoding(encode);
            Template template = null;
            template = configuration.getTemplate(templatePath, encode);
            template.process(data, write);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    
    public static void process(String templatePath, Map<String, Object> data, Writer write) {
        process(templatePath, "utf-8", data, write);
    }

    static {
    	configuration.setNumberFormat("0.#####################");
    	configuration.setClassForTemplateLoading(FreemarkerHelper.class, "/template");
    }
}
