package com.mxpioframework.excel.swfviewer.handler.impl;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.mxpioframework.excel.export.web.resolver.PdfToSwfConverter;
import com.mxpioframework.excel.swfviewer.handler.ISwfFileHandler;
import com.mxpioframework.excel.util.ExportUtils;

@Service(ExportPdfToSwfHandler.BEAN_ID)
public class ExportPdfToSwfHandler implements ISwfFileHandler {

	public static final String BEAN_ID = "mxpio.ExportPdfToSwfHandler";

	public static final String NAME = "export.pdf2swf";

	public String getHandlerName() {
		return NAME;
	}

	public String getHandlerDesc() {
		return "导出的pdf报表生成swf文件在线预览";
	}

	public File execute(Map<String, String> varMap) throws Exception {
		String id = varMap.get("id");
		String name = varMap.get("name");
		name = new String(name.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
		if (!StringUtils.isNotEmpty(id) || !id.matches("[a-z0-9-]+")) {
			throw new IllegalArgumentException("illegal id");
		}
		if (!StringUtils.isNotEmpty(name) || !name.matches("[^/\\\\?<>*:\"|]+(\\.pdf)$")) {
			throw new IllegalArgumentException("illegal name");
		}
		File f = ExportUtils.getFile(id, name);
		if (!f.exists()) {
			throw new RuntimeException("文件不存在！" + ExportUtils.getFileStorePath() + id + "_" + name);
		}
		String sourcePdf = f.getAbsolutePath();
		PdfToSwfConverter pdfToSwfConverter = new PdfToSwfConverter();
		String swf = pdfToSwfConverter.execute(sourcePdf, null);
		return new File(swf);
	}
}
