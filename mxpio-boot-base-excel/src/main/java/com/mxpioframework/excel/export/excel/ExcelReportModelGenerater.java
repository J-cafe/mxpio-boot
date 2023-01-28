package com.mxpioframework.excel.export.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.mxpioframework.excel.export.AbstractReportModelGenerater;
import com.mxpioframework.excel.export.entity.ExportColumn;
import com.mxpioframework.excel.export.entity.ExportSolution;
import com.mxpioframework.excel.export.model.ReportGrid;
import com.mxpioframework.excel.export.model.ReportGridData;
import com.mxpioframework.excel.export.model.ReportGridHeader;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component(ExcelReportModelGenerater.BEAN_ID)
public class ExcelReportModelGenerater extends AbstractReportModelGenerater {
	
	public static final String BEAN_ID="mxpio.ExcelReportModelGenerater";

	public ReportGrid generateReportGridModel(ExportSolution exportSolution, String intercepterBean, String key) throws Exception {
		ReportGrid gridModel = new ReportGrid();
		List<ReportGridHeader> gridHeaders = new ArrayList<ReportGridHeader>();
		this.createGridColumnHeader(exportSolution, gridHeaders, null);
		gridModel.setGridHeaderModelList(gridHeaders);
		gridModel.setGridDataModel(this.createGridColumnData(exportSolution, intercepterBean, key));
		return gridModel;
	}

	private void createGridColumnHeader(ExportSolution exportSolution, List<ReportGridHeader> headerList, ReportGridHeader parent) {
		List<ExportColumn> columns = exportSolution.getColumns();
		if (columns == null)
			return;
		for (ExportColumn column : columns) {
			String columnName = column.getColumnName();
			int level = column.getLevel();
			String label = column.getLabel();
			int width = column.getWidth();
			String bgColor = exportSolution.getColumnBgColor();
			String fontColor = exportSolution.getColumnFontColor();
			int fontSize = 10;
			if (exportSolution.getColumnFontSize() != null) {
				fontSize = exportSolution.getColumnFontSize();
			}
			int align = 1;
			if (exportSolution.getColumnAlign() != null) {
				align = exportSolution.getColumnAlign();
			}
			int dataAlign = 1;
			if (column.getDataAlign() != null) {
				dataAlign = column.getDataAlign();
			}
			ReportGridHeader header = new ReportGridHeader();
			header.setLevel(level);
			header.setAlign(align);
			header.setDataAlign(dataAlign);
			if (StringUtils.isNotEmpty(bgColor)) {
				header.setBgColor(this.createRGB(bgColor));
			}
			if (StringUtils.isNotEmpty(fontColor)) {
				header.setFontColor(this.createRGB(fontColor));
			}
			header.setColumnName(columnName);
			header.setLabel(label);
			header.setFontSize(fontSize);
			header.setWidth(width);
			if (parent != null) {
				parent.addGridHeader(header);
				header.setParent(parent);
			} else {
				headerList.add(header);
			}
			/*List<Map<String, Object>> children = (List<Map<String, Object>>) column.get("children");
			if (children != null) {
				this.createGridColumnHeader(children, headerList, header);
			}*/
		}
	}

	private ReportGridData createGridColumnData(ExportSolution exportSolution, String intercepterBean, String key) throws Exception {
		// String treeColumn = (String) map.get("treeColumn");
		List<Map<String, Object>> dataList = getGridModelData(exportSolution, intercepterBean, key);
		ReportGridData gridData = new ReportGridData();
		gridData.setDatas(dataList);
		// gridData.setTreeColumn(treeColumn);
		gridData.setContentFontSize(exportSolution.getDataFontSize());
		String contentFontColor = exportSolution.getDataFontColor();
		String contentBgColor = exportSolution.getDataBgColor();
		gridData.setContentFontColor(this.createRGB(contentFontColor));
		// gridData.setContentFontAlign((Integer) style.get("fontAlign"));
		gridData.setContentBgColor(this.createRGB(contentBgColor));
		return gridData;
	}

}
