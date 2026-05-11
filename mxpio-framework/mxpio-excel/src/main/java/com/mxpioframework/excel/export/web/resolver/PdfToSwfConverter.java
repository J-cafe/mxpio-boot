package com.mxpioframework.excel.export.web.resolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PdfToSwfConverter {

	private String pdfToSwfPath = "";
	private String xpdfPath = "";

	public PdfToSwfConverter() {

	}

	public PdfToSwfConverter(String pdfToSwfPath, String xpdfPath) {
		this.pdfToSwfPath = pdfToSwfPath;
		this.xpdfPath = xpdfPath;
	}

	public String execute(String sourcePdf, String targetSwf) throws Exception {
		if (!StringUtils.isNotEmpty(sourcePdf) || !new File(sourcePdf).exists()) {
			throw new IllegalAccessException("pdf文件不存在！");
		}
		if (!StringUtils.isNotEmpty(targetSwf)) {
			targetSwf = sourcePdf.substring(0, sourcePdf.length() - 4) + ".swf";
		}

		if (!StringUtils.isNotEmpty(pdfToSwfPath)) {
			throw new RuntimeException("请先在dorado-home/configure.properties文件中配置bdf2.swfviewer.pdfToSwf属性值");
		}
		if (!StringUtils.isNotEmpty(xpdfPath)) {
			throw new RuntimeException("请先在dorado-home/configure.properties文件中配置bdf2.swfviewer.xpdfPath属性值");
		}

		sourcePdf = "\"" + sourcePdf + "\"";
		targetSwf = "\"" + targetSwf + "\"";

		log.info("sourcePdf path:" + sourcePdf);
		log.info("targetSwf path:" + targetSwf);
		Process process = null;
		File pdf = new File(sourcePdf);
		long pdfSize = pdf.length() / (1024 * 1024);// 单位M
		if (isWindows()) {
			StringBuilder command = new StringBuilder(pdfToSwfPath + " " + sourcePdf);
			if (pdfSize > 1) {// 文件大于1M时，添加该参数增加转换效率
				command.append(" -s poly2bitmap");
			}
			command.append(" -s languagedir=").append(xpdfPath).append(" -T 9 -o ").append(targetSwf);
			process = Runtime.getRuntime().exec(command.toString());
		} else {
			String[] cmd = null;
			if (pdfSize > 1) {
				cmd = new String[10];
				cmd[0] = pdfToSwfPath;
				cmd[1] = sourcePdf;
				cmd[2] = "-s";
				cmd[3] = "poly2bitmap";
				cmd[4] = "-s";
				cmd[5] = "languagedir=" + xpdfPath + "";
				cmd[6] = "-T";
				cmd[7] = "9";
				cmd[8] = "-o";
				cmd[9] = targetSwf;
			} else {
				cmd = new String[8];
				cmd[0] = pdfToSwfPath;
				cmd[1] = sourcePdf;
				cmd[2] = "-s";
				cmd[3] = "languagedir=" + xpdfPath;
				cmd[4] = "-T";
				cmd[5] = "9";
				cmd[6] = "-o";
				cmd[7] = targetSwf;
			}
			process = Runtime.getRuntime().exec(cmd);
		}

		new DoOutput(process.getInputStream()).start();
		new DoOutput(process.getErrorStream()).start();
		process.waitFor();
		return targetSwf;
	}

	private boolean isWindows() {
		String p = System.getProperty("os.name");
		return p.toLowerCase().contains("windows");
	}

	public String getPdfToSwfPath() {
		return pdfToSwfPath;
	}

	public void setPdfToSwfPath(String pdfToSwfPath) {
		this.pdfToSwfPath = pdfToSwfPath;
	}

	public String getXpdfPath() {
		return xpdfPath;
	}

	public void setXpdfPath(String xpdfPath) {
		this.xpdfPath = xpdfPath;
	}

	private class DoOutput extends Thread {
		public InputStream is;

		public DoOutput(InputStream is) {
			this.is = is;
		}

		@Override
		public void run() {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			try {
				// 这里并没有对流的内容进行处理，只是读了一遍
				@SuppressWarnings("unused")
				String str = null;
				while ((str = br.readLine()) != null) {
					// System.out.println(str);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
                try {
                    br.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
		}
	}
}
