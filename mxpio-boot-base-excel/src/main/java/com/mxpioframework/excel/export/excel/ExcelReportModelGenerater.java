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

	public ReportGrid generateReportGridModel(ExportSolution exportSolution, String intercepterBean) throws Exception {
		ReportGrid gridModel = new ReportGrid();
		List<ReportGridHeader> gridHeaders = new ArrayList<ReportGridHeader>();
		this.createGridColumnHeader(exportSolution.getColumns(), gridHeaders, null);
		gridModel.setGridHeaderModelList(gridHeaders);
		gridModel.setGridDataModel(this.createGridColumnData(exportSolution, intercepterBean));
		return gridModel;
	}

	private void createGridColumnHeader(List<ExportColumn> columns, List<ReportGridHeader> headerList, ReportGridHeader parent) {
		if (columns == null)
			return;
		for (ExportColumn column : columns) {
			String columnName = column.getColumnName();
			int level = column.getLevel();
			String label = column.getLabel();
			int width = column.getWidth();
			String bgColor = column.getBgColor();
			String fontColor = column.getFontColor();
			int fontSize = 10;
			if (column.getFontSize() != null) {
				fontSize = column.getFontSize();
			}
			int align = 1;
			if (column.getAlign() != null) {
				align = column.getAlign();
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

	private ReportGridData createGridColumnData(ExportSolution exportSolution, String intercepterBean) throws Exception {
		// String treeColumn = (String) map.get("treeColumn");
		List<Map<String, Object>> dataList = getGridModelData(exportSolution, intercepterBean);
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
