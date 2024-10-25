package com.mxpio.webapp;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import com.mxpioframework.common.CommonConstant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class MBootApplication {

	public static void main(String[] args) throws UnknownHostException {
		ConfigurableApplicationContext application = SpringApplication.run(MBootApplication.class, args);
		Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        StringBuffer s = new StringBuffer();
        CommonConstant.getModules().forEach(item -> {
        	s.append("\t--").append(item.getModuleKey()).append("：").append(item.getModuleName()).append("\n");
		});
        log.info("\n----------------------------------------------------------\n\t" +
                "Application MxpIO-Boot is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
                "Swagger文档: \thttp://" + ip + ":" + port + path + "/swagger-ui.html\n\t" +
                "Swagger api-docs文档: \thttp://" + ip + ":" + port + path + "/v3/api-docs\n\t" +
                "加载模块: \n" +
                s.toString() +
                "----------------------------------------------------------");
	}

}
