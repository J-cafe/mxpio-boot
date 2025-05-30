package com.mxpioframework.excel.export.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import com.mxpioframework.excel.export.model.ReportGrid;
import com.mxpioframework.excel.export.model.ReportGridHeader;

public abstract class AbstractExcelReportBuilder {
	public static final String DefaultSheetName = "Sheet1";

	public Workbook createWorkBook2003() {
		return new HSSFWorkbook();
	}

	public Workbook createWorkBook2007(int rowAccessWindowSize) {
		return new SXSSFWorkbook(rowAccessWindowSize);
	}

	public void writeFile(Workbook workbook, String fileLocation) throws IOException {
        try (FileOutputStream out = new FileOutputStream(fileLocation)) {
            workbook.write(out);
        }
	}

	public void writeOutputStream(Workbook workbook, OutputStream out) throws IOException {
		workbook.write(out);
	}

	public Sheet createSheet(Workbook workbook, String sheetName) {
		if (StringUtils.isNotEmpty(sheetName)) {
			return workbook.createSheet(sheetName);
		} else {
			return workbook.createSheet(DefaultSheetName);
		}
	}

	public void fillCellValue(Cell cell, Object value, SimpleDateFormat sdf) {
		if (value != null) {
			if (value instanceof Date) {
				String result = sdf.format(value);
				if (result.endsWith("00:00:00")) {
					result = result.substring(0, 11);
				}
				cell.setCellValue(result);
			} else if (value instanceof Double) {
				cell.setCellValue((Double) value);
			} else if (value instanceof Integer) {
				cell.setCellValue((Integer) value);
			} else if (value instanceof Byte) {
				cell.setCellValue((Byte) value);
			} else if (value instanceof Short) {
				cell.setCellValue((Short) value);
			} else if (value instanceof Boolean) {
				cell.setCellValue((Boolean) value);
			} else if (value instanceof Long) {
				cell.setCellValue((Long) value);
			} else if (value instanceof Float) {
				cell.setCellValue((Float) value);
			} else if (value instanceof BigDecimal) {
				cell.setCellValue(((BigDecimal) value).doubleValue());
			} else {
				// cell.setCellType(CellType.STRING);
				cell.setCellValue(value.toString());
			}
		} else {
			cell.setCellValue("");
		}
	}

	public void calculateMaxHeaderLevel(ReportGrid gridModel, List<ReportGridHeader> gridHeaders) {
		int maxLevel = gridModel.getMaxHeaderLevel();
		for (ReportGridHeader header : gridHeaders) {
			if (header.getLevel() > maxLevel) {
				maxLevel = header.getLevel();
				gridModel.setMaxHeaderLevel(maxLevel);
			}
			if (!header.getHeaders().isEmpty()) {
				this.calculateMaxHeaderLevel(gridModel, header.getHeaders());
			}
		}
	}

	public void calculateGridHeadersByLevel(List<ReportGridHeader> columnHeaderModels, int level, List<ReportGridHeader> result) {
		for (ReportGridHeader reportGridHeaderModel : columnHeaderModels) {
			if (reportGridHeaderModel.getLevel() == level) {
				result.add(reportGridHeaderModel);
			} else if (!reportGridHeaderModel.getHeaders().isEmpty()) {
				calculateGridHeadersByLevel(reportGridHeaderModel.getHeaders(), level, result);
			}
		}
	}

	public void calculateBottomColumnHeader(List<ReportGridHeader> gridHeader, List<ReportGridHeader> result) {
		for (ReportGridHeader header : gridHeader) {
			if (header.getHeaders().isEmpty()) {
				result.add(header);
			} else {
				this.calculateBottomColumnHeader(header.getHeaders(), result);
			}
		}
	}

	public int calculateGridHeaderColspan(ReportGridHeader headerModel) {
		if (headerModel.getHeaders().isEmpty()) {
			return 1;
		} else {
			List<ReportGridHeader> result = new ArrayList<>();
			calculateBottomColumnHeader(headerModel.getHeaders(), result);
			return result.size();
		}
	}

}
