package com.mxpioframework.excel.export.web.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.AbstractView;

import com.mxpioframework.excel.swfviewer.handler.ISwfFileHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PdfView extends AbstractView {
	
	@Autowired
	private Map<String, ISwfFileHandler> swfFileHandlers;

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		Map<String, String> parameterMap = new HashMap<String, String>();
		Enumeration<String> parameters = request.getParameterNames();
		while (parameters.hasMoreElements()) {
			String parameter = parameters.nextElement();
			String value = request.getParameter(parameter);
			parameterMap.put(parameter, value);
		}
		String handler = parameterMap.get("handler");
		Assert.hasText(handler, "url请求必须包含handler参数!");
		// Map<String, ISwfFileHandler> beanMap = DoradoContext.getAttachedWebApplicationContext().getBeansOfType(ISwfFileHandler.class);
		ISwfFileHandler swfFileHandler = null;
		for (Map.Entry<String, ISwfFileHandler> entry : swfFileHandlers.entrySet()) {
			if (entry.getValue().getHandlerName().equals(handler)) {
				swfFileHandler = entry.getValue();
				break;
			}
		}
		log.info("SwfFileHandler Parameter: " + parameterMap);
		Assert.notNull(swfFileHandler, "没有找到com.mxpioframework.excel.swfviewer.handler.ISwfFileHandler的实现类！");
		File file = swfFileHandler.execute(parameterMap);
		if (file == null || !file.exists()) {
			String msg = swfFileHandler.getHandlerName()+"返回的SWF文件不存在！";
			if (file!=null){
				msg += " path:" + file.getAbsolutePath();
			}
			throw new RuntimeException(msg);
		}
		response.setHeader("Server", "http://www.mxpio.com");
		response.setContentType("application/x-shockwave-flash");
		response.setHeader("Content-Disposition", "attachment;filename=\"report.swf\"");
		OutputStream output = response.getOutputStream();
		InputStream input = null;
		try {
			input = new FileInputStream(file);
			IOUtils.copy(input, output);
			output.flush();
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}		
	}

}
