package com.mxpioframework.excel.export.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.Document;
import com.mxpioframework.excel.export.csv.CvsReportBuilder;
import com.mxpioframework.excel.export.entity.ExportSolution;
import com.mxpioframework.excel.export.excel.ExcelReportBuilder;
import com.mxpioframework.excel.export.excel.ExcelReportModelGenerater;
import com.mxpioframework.excel.export.extension.ReportBuilder;
import com.mxpioframework.excel.export.extension.ReportGenerater;
import com.mxpioframework.excel.export.model.FileExtension;
import com.mxpioframework.excel.export.model.ReportGrid;
import com.mxpioframework.excel.export.model.ReportGridHeader;
import com.mxpioframework.excel.export.model.ReportTitle;
import com.mxpioframework.excel.export.pdf.PdfReportBuilder;
import com.mxpioframework.excel.export.pdf.PdfReportModelGenerater;
import com.mxpioframework.excel.export.service.ExportSolutionService;
import com.mxpioframework.excel.swfviewer.handler.ISwfFileHandler;
import com.mxpioframework.excel.util.ExportUtils;
import com.mxpioframework.security.service.DataResourceService;
import com.mxpioframework.security.vo.DataVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Export2ReportController", description = "导出接口")
@RestController(Export2ReportController.BEAN_ID)
@RequestMapping("/excel/export/")
public class Export2ReportController implements InitializingBean, ApplicationContextAware {

	public static final String BEAN_ID = "mxpio.Export2ReportController";

	@Autowired
	@Qualifier(ExcelReportModelGenerater.BEAN_ID)
	public ExcelReportModelGenerater excelReportModelGenerater;

	@Autowired
	@Qualifier(ExcelReportBuilder.BEAN_ID)
	public ExcelReportBuilder excelReportBuilder;

	@Autowired
	@Qualifier(PdfReportModelGenerater.BEAN_ID)
	public PdfReportModelGenerater pdfReportModelGenerater;

	@Autowired
	@Qualifier(PdfReportBuilder.BEAN_ID)
	public PdfReportBuilder pdfReportBuilder;

	@Autowired
	@Qualifier(ReportGenerater.BEAN_ID)
	public ReportGenerater commonReportGenerater;

	@Autowired
	@Qualifier(CvsReportBuilder.BEAN_ID)
	public CvsReportBuilder cvsReportBuilder;
	
	@Autowired
	public DataResourceService dataResourceService;
	
	@Autowired
	private Map<String, ISwfFileHandler> swfFileHandlers;

	public int rowAccessWindowSize = 500;
	
	@Value("${mxpio.excel.exporter.cacheSize}")
	private String cacheSize;
	
	@Value("${mxpio.excel.exporter.extension.fileType}")
	public String extensionFileType;
	
	@Autowired
	private ExportSolutionService exportSolutionService;
	
	@GetMapping("download/{extension}/{solutionCode}")
	@Operation(summary = "导出数据", description = "导出数据", method = "GET")
	public void generateReportFile(@PathVariable("solutionCode") String solutionCode,
			@PathVariable("extension") String extension,
			@RequestParam("_key") String key, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ExportSolution exportSolution = exportSolutionService.getByCode(solutionCode);
		exportSolution.setParams(request.getParameterMap());
		List<DataVo> dataResourceVos = dataResourceService.findAllApi(true, exportSolution.getApi());
		if(CollectionUtils.isNotEmpty(dataResourceVos)){
			exportSolution.setDataResource(dataResourceVos.get(0));
		}
		
		String fileName = exportSolution.getFileName();
		String interceptorName = null;
		if (exportSolution.getInterceptorName() != null) {
			interceptorName = exportSolution.getInterceptorName();
		}
		String id = UUID.randomUUID().toString();
		String location = ExportUtils.getFileStorePath() + id + "_" + fileName + "." + extension;
		ReportTitle reportTitle = excelReportModelGenerater.generateReportTitleModel(exportSolution);
		if (FileExtension.xls.equals(extension) || FileExtension.xlsx.equals(extension)) {
			this.generateExcelFile(reportTitle, exportSolution, fileName, location, interceptorName, key);
		} else if (FileExtension.pdf.equals(extension)) {
			this.generatePdfFile(reportTitle, exportSolution, location, interceptorName, key);
		} else {
			this.generateOtherFile(extension, reportTitle, exportSolution, location, interceptorName, key);
		}
		Map<String, String> outParameter = new HashMap<String, String>();
		outParameter.put("id", id);
		outParameter.put("name", fileName + "." + extension);
		doDownloadExcelReport(outParameter,request,response);
	}

	private void generatePdfFile(ReportTitle reportTitle, ExportSolution exportSolution, String fileName, String interceptorName, String key) throws Exception {
		FileOutputStream out = new FileOutputStream(fileName);
		Document doc = pdfReportBuilder.createDocument(reportTitle, out);
		try {
			pdfReportBuilder.addGridToDocument(doc, reportTitle, pdfReportModelGenerater.generateReportGridModel(exportSolution, interceptorName, key));
		} finally {
			doc.close();
			out.close();
		}
	}

	private void generateExcelFile(ReportTitle reportTitle, ExportSolution exportSolution, String fileName, String location, String interceptorName, String key) throws Exception {
		Workbook workbook = null;
		if (location.endsWith(FileExtension.xls)) {
			workbook = excelReportBuilder.createWorkBook2003();
		} else if (location.endsWith(FileExtension.xlsx)) {
			workbook = excelReportBuilder.createWorkBook2007(rowAccessWindowSize);
		}
		Assert.notNull(workbook, "the workbook must not be null");
		Sheet sheet = excelReportBuilder.createSheet(workbook, fileName);
		int nextRow = 0;
		
		ReportGrid reportGridModel = excelReportModelGenerater.generateReportGridModel(exportSolution, interceptorName, key);
		List<ReportGridHeader> bottomColumnHeaderModelList = new ArrayList<ReportGridHeader>();
		excelReportBuilder.calculateBottomColumnHeader(reportGridModel.getGridHeaderModelList(), bottomColumnHeaderModelList);
		int bottomColumnHeaderCount = bottomColumnHeaderModelList.size();
		reportGridModel.setColumnCount(bottomColumnHeaderCount);
		nextRow = excelReportBuilder.addTitleToSheet(reportTitle, sheet, bottomColumnHeaderCount - 1);
		nextRow = excelReportBuilder.addGridToSheet(reportGridModel, sheet, nextRow);
		excelReportBuilder.writeFile(workbook, location);
	}

	private void generateOtherFile(String extension, ReportTitle reportTitle, ExportSolution exportSolution, String fileName, String interceptorName, String key) throws Exception {
        try (FileOutputStream out = new FileOutputStream(fileName)) {
            ReportBuilder builder = this.getReportBuilder(extension);
            Assert.notNull(builder, "ReportBuilder is null.");
            ReportGrid report = commonReportGenerater.generateReportGridModel(exportSolution, interceptorName, key);
            builder.execute(out, report);
        }
	}

	public void afterPropertiesSet() throws Exception {
		if (StringUtils.isNotEmpty(cacheSize)) {
			rowAccessWindowSize = Integer.parseInt(cacheSize);
		}
		String fileLocation = ExportUtils.getFileStorePath();
		File f = new File(fileLocation);
		if (!f.exists()) {
			f.mkdirs();
		}
		builders = applicationContext.getBeansOfType(ReportBuilder.class).values();
	}

	private Collection<ReportBuilder> builders;

	private ApplicationContext applicationContext;

	public Collection<ReportBuilder> getBuilders() {
		return builders;
	}

	public ReportBuilder getReportBuilder(String extension) {
		for (ReportBuilder builder : getBuilders()) {
			if (builder.support(extension)) {
				return builder;
			}
		}
		return null;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	private void doDownloadExcelReport(Map<String, String> outParameter, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		String id = outParameter.get("id");
		String name = outParameter.get("name");
		if (!StringUtils.isNotEmpty(id) || !id.matches("[a-z0-9-]+")) {
			this.processError(request, response, "request parameter id error");
			return;
		}
		String type = "";
		if (StringUtils.isNotEmpty(extensionFileType)) {
			type = "|" + extensionFileType;
		}
		if (!StringUtils.isNotEmpty(name) || !name.matches("[^/\\\\?<>*:\"|]+(\\.(xls|xlsx|pdf|csv" + type + "))$")) {
			this.processError(request, response, "request parameter name error");
			return;
		}
		String fileName = id + "_" + name;
		String location = ExportUtils.getFileStorePath();
		File file = new File(location, fileName);
		if (!file.exists() || !file.isFile()) {
			this.processError(request, response, "find not found " + file);
			return;
		}
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Server", "http://www.mxpio.com");
		response.setContentType("application/octet-stream;charset=utf-8");
		response.setHeader("Connection", "close");
		response.setHeader("Accept-Ranges", "bytes");
		name = URLEncoder.encode(name, "UTF-8");
		name = name.replaceAll("\\+", "%20");
		response.setHeader("Content-Disposition", "attachment;filename=\"" + name + "\";filename*=utf-8''" + name + "");
		response.setHeader("Content-Length", "" + file.length());
		FileInputStream input = null;
		OutputStream out = null;
		try {
			input = new FileInputStream(file);
			out = response.getOutputStream();
			IOUtils.copy(input, out);
			out.flush();
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(out);
            deletePreviousDayTempFile();
        }
	}

	private void deletePreviousDayTempFile() throws IOException {
		String location = ExportUtils.getFileStorePath();
		File file = new File(location);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
            assert files != null;
            for (File f : files) {
				long time = f.lastModified();
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(time);
				Calendar last = Calendar.getInstance();
				last.add(Calendar.DAY_OF_MONTH, -1);
				if (cal.before(last)) {
					f.delete();
				}
			}
		}
	}

	private void processError(HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write(message);
		out.flush();
		out.close();
	}
	
	@SuppressWarnings("unused")
	private void doDownloadPdfReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
			input = Files.newInputStream(file.toPath());
			IOUtils.copy(input, output);
			output.flush();
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}
}
