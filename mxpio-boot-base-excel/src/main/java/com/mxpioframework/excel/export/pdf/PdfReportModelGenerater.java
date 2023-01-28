package com.mxpioframework.excel.export.pdf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mxpioframework.excel.export.AbstractReportModelGenerater;
import com.mxpioframework.excel.export.entity.ExportColumn;
import com.mxpioframework.excel.export.entity.ExportSolution;
import com.mxpioframework.excel.export.pdf.model.ColumnHeader;
import com.mxpioframework.excel.export.pdf.model.ReportData;
import com.mxpioframework.excel.export.pdf.model.ReportDataModel;
import com.mxpioframework.excel.export.pdf.model.TextChunk;

import org.springframework.stereotype.Component;

@Component(PdfReportModelGenerater.BEAN_ID)
public class PdfReportModelGenerater extends AbstractReportModelGenerater {
	
	public static final String BEAN_ID="mxpio.PdfReportModelGenerater";


	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public ReportDataModel generateReportGridModel(ExportSolution exportSolution, String intercepterBean, String key) throws Exception {
		List<ExportColumn> columnInfos = exportSolution.getColumns();

		Map<String, Integer> columDataAlignMap = new HashMap<String, Integer>();
		this.calculateColumnAlign(columnInfos, columDataAlignMap);

		List<ColumnHeader> topColumnHeaders = new ArrayList<ColumnHeader>();
		createGridColumnHeader(exportSolution, topColumnHeaders, null);

		List<Map<String, Object>> dataMapList = getGridModelData(exportSolution, intercepterBean, key);
		List<ReportData> reportDataList = createGridColumnData(dataMapList, topColumnHeaders, exportSolution, columDataAlignMap);
		ReportDataModel dataModel = new ReportDataModel(topColumnHeaders, reportDataList);
		
		return dataModel;
	}

	private List<ReportData> createGridColumnData(List<Map<String, Object>> dataList, List<ColumnHeader> parentColumnHeaders, ExportSolution exportSolution, Map<String, Integer> dataColumnAlign) {
		List<ReportData> result = new ArrayList<ReportData>();
		List<String> dataColumnNameList = new ArrayList<String>();
		calculateBottomColumnNames(parentColumnHeaders, dataColumnNameList);
		for (Map<String, Object> dataMap : dataList) {
			for (String name : dataColumnNameList) {
				Object data = dataMap.get(name);
				TextChunk textChunk = new TextChunk();
				String value = "";
				if (data != null && data instanceof Date) {
					value = simpleDateFormat.format((Date) data);
					if (value.endsWith("00:00:00")) {
						value = value.substring(0, 11);
					}
				} else {
					if (data != null) {
						value = data.toString();
					}
				}
				int dataAlign = (Integer) dataColumnAlign.get(name);
				textChunk.setFontSize(exportSolution.getDataFontSize());
				textChunk.setFontColor(this.createRGB(exportSolution.getDataFontColor()));
				textChunk.setText(value);
				ReportData columnData = new ReportData(textChunk);
				columnData.setAlign(dataAlign);
				columnData.setBgColor(this.createRGB(exportSolution.getDataBgColor()));
				result.add(columnData);
			}
		}
		return result;
	}

	private void createGridColumnHeader(ExportSolution exportSolution, List<ColumnHeader> topColumnHeaders, ColumnHeader parentHeader) throws Exception {
		List<ExportColumn> columnInfos = exportSolution.getColumns();
		if(columnInfos == null){
			return;
		}
		for (ExportColumn column : columnInfos) {
			String columnName = column.getColumnName();
			int level = column.getLevel();
			String label = column.getLabel();
			int width = column.getWidth();
			String bgColor = exportSolution.getColumnBgColor();
			String fontColor = exportSolution.getColumnFontColor();
			int align = exportSolution.getColumnAlign();
			int fontSize = exportSolution.getColumnFontSize();

			ColumnHeader header = new ColumnHeader(level);
			header.setAlign(align);
			header.setBgColor(this.createRGB(bgColor));
			header.setFontColor(this.createRGB(fontColor));
			header.setName(columnName);
			header.setText(label);
			header.setFontSize(fontSize);
			header.setWidth(width);
			if (parentHeader != null) {
				parentHeader.addColumnHeader(header);
			} else {
				topColumnHeaders.add(header);
			}
			/*List<Map<String, Object>> children = (List<Map<String, Object>>) column.get("children");
			if (children != null) {
				createGridColumnHeader(children, topColumnHeaders, header);
			}*/
		}
	}
	
	private void calculateColumnAlign(List<ExportColumn> columnInfos, Map<String, Integer> result) {
		for (ExportColumn column : columnInfos) {
			String columnName = column.getColumnName();
			int dataAlign = 1;
			if (column.getDataAlign() != null) {
				dataAlign = column.getDataAlign();
			}
			result.put(columnName, dataAlign);
			/*List<Map<String, Object>> children = (List<Map<String, Object>>) column.get("children");
			if (children != null) {
				calculateColumnAlign(children, result);
			}*/
		}
	}
	
	private void calculateBottomColumnNames(List<ColumnHeader> parentColumnHeaders, List<String> result) {
		for (ColumnHeader header : parentColumnHeaders) {
			if (header.getColumnHeaders().size() == 0) {
				result.add(header.getName());
			} else {
				calculateBottomColumnNames(header.getColumnHeaders(), result);
			}
		}
	}
}
