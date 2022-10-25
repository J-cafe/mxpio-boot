package com.mxpioframework.excel.export.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.mxpioframework.excel.util.ExportUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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

	public int rowAccessWindowSize = 500;
	
	@Value("${mxpio.excel.exporter.cacheSize}")
	private String cacheSize;
	
	@Autowired
	private ExportSolutionService exportSolutionService;
	
	@GetMapping("download/{solutionId}/{extension}")
	@Operation(summary = "导出数据", description = "导出数据", method = "POST")
	public Map<String, String> generateReportFile(@PathVariable("solutionId") String solutionId, @PathVariable("extension") String extension) throws Exception {
		ExportSolution exportSolution = exportSolutionService.getById(solutionId);
		String fileName = exportSolution.getFileName();
		String interceptorName = null;
		if (exportSolution.getInterceptorName() != null) {
			interceptorName = exportSolution.getInterceptorName();
		}
		String id = UUID.randomUUID().toString();
		String location = ExportUtils.getFileStorePath() + id + "_" + fileName + "." + extension;
		ReportTitle reportTitle = excelReportModelGenerater.generateReportTitleModel(exportSolution);
		if (FileExtension.xls.equals(extension) || FileExtension.xlsx.equals(extension)) {
			this.generateExcelFile(reportTitle, exportSolution, fileName, location, interceptorName);
		} else if (FileExtension.pdf.equals(extension)) {
			this.generatePdfFile(reportTitle, exportSolution, location, interceptorName);
		} else {
			this.generateOtherFile(extension, reportTitle, exportSolution, location, interceptorName);
		}
		Map<String, String> outParameter = new HashMap<String, String>();
		outParameter.put("id", id);
		outParameter.put("name", fileName + "." + extension);
		return outParameter;
	}

	private void generatePdfFile(ReportTitle reportTitle, ExportSolution exportSolution, String fileName, String interceptorName) throws Exception {
		FileOutputStream out = new FileOutputStream(fileName);
		Document doc = pdfReportBuilder.createDocument(reportTitle, out);
		try {
			pdfReportBuilder.addGridToDocument(doc, reportTitle, pdfReportModelGenerater.generateReportGridModel(exportSolution, interceptorName));
		} finally {
			doc.close();
			out.close();
		}
	}

	private void generateExcelFile(ReportTitle reportTitle, ExportSolution exportSolution, String fileName, String location, String interceptorName) throws Exception {
		Workbook workbook = null;
		if (location.endsWith(FileExtension.xls)) {
			workbook = excelReportBuilder.createWorkBook2003();
		} else if (location.endsWith(FileExtension.xlsx)) {
			workbook = excelReportBuilder.createWorkBook2007(rowAccessWindowSize);
		}
		Assert.notNull(workbook, "the workbook must not be null");
		Sheet sheet = excelReportBuilder.createSheet(workbook, fileName);
		int nextRow = 0;
		
		ReportGrid reportGridModel = excelReportModelGenerater.generateReportGridModel(exportSolution, interceptorName);
		List<ReportGridHeader> bottomColumnHeaderModelList = new ArrayList<ReportGridHeader>();
		excelReportBuilder.calculateBottomColumnHeader(reportGridModel.getGridHeaderModelList(), bottomColumnHeaderModelList);
		int bottomColumnHeaderCount = bottomColumnHeaderModelList.size();
		reportGridModel.setColumnCount(bottomColumnHeaderCount);
		nextRow = excelReportBuilder.addTitleToSheet(reportTitle, sheet, bottomColumnHeaderCount - 1);
		nextRow = excelReportBuilder.addGridToSheet(reportGridModel, sheet, nextRow);
		excelReportBuilder.writeFile(workbook, location);
	}

	private void generateOtherFile(String extension, ReportTitle reportTitle, ExportSolution exportSolution, String fileName, String interceptorName) throws Exception {
		FileOutputStream out = new FileOutputStream(fileName);
		try {
			ReportBuilder builder = this.getReportBuilder(extension);
			Assert.notNull(builder, "ReportBuilder is null.");
			ReportGrid report = commonReportGenerater.generateReportGridModel(exportSolution, interceptorName);
			builder.execute(out, report);
		} finally {
			out.close();
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

}
