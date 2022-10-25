package com.mxpioframework.excel.export;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.common.util.BeanReflectionUtils;
import com.mxpioframework.common.util.SpringUtil;
import com.mxpioframework.excel.export.entity.ExportColumn;
import com.mxpioframework.excel.export.entity.ExportSolution;
import com.mxpioframework.excel.export.interceptor.IDataInterceptor;
import com.mxpioframework.excel.export.model.ReportTitle;
import com.mxpioframework.excel.export.model.ReportTitleStyle;
import com.mxpioframework.excel.util.ColorUtils;
import com.mxpioframework.security.entity.Dict;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class AbstractReportModelGenerater {

	public ReportTitle generateReportTitleModel(ExportSolution parameter) {
		ReportTitle reportTitle = new ReportTitle();
		if (parameter == null) {
			return reportTitle;
		}
		Boolean showTitle = parameter.isShowTitle();
		if (showTitle != null && showTitle) {
			reportTitle.setShowTitle(true);
			reportTitle.setTitle(parameter.getTitle());
			reportTitle.setStyle(createTitleStyle(parameter));
		}
		reportTitle.setShowBorder(parameter.isShowBorder());
		reportTitle.setShowPageNo(parameter.isShowPageNo());
		return reportTitle;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String, Object>> getGridModelData(ExportSolution exportSolution, String intercepterBean) throws Exception {
		/*List<Map<String, Object>> dataList = null;
		int pageNo = Integer.valueOf(map.get("pageNo").toString());
		int pageSize = Integer.valueOf(map.get("pageSize").toString());
		Object dataProviderParameter = map.get("dataProviderParameter");
		String dataProviderId = (String) map.get("dataProviderId");
		
		if (map.get("sysParameter") != null) {
			ObjectMapper om = new ObjectMapper();
			String sp = om.writeValueAsString(map.get("sysParameter"));
			ObjectNode rudeSysParameter = (ObjectNode) om.readTree(sp);
			JsonNode rudeCriteria = null;
			if (rudeSysParameter != null) {
				rudeCriteria = rudeSysParameter.remove("criteria");
			}
			MetaData sysParameter = null;
			if (rudeSysParameter != null) {
				sysParameter = (MetaData) JsonUtils.toJavaObject(rudeSysParameter, null, null, false, null);
				if (rudeCriteria != null && rudeCriteria instanceof ObjectNode) {
					sysParameter.put("criteria", CriterionUtils.getCriteria((ObjectNode) rudeCriteria));
				}
				if (sysParameter != null && !sysParameter.isEmpty()) {
					dataProviderParameter = new ParameterWrapper(dataProviderParameter, sysParameter);
				}
			}
		}
		DataType resultDataType = null;
		String resultDataTypeName = (String) map.get("resultDataType");
		if (StringUtils.isNotEmpty(resultDataTypeName)) {
			resultDataType = viewManager.getDataType(resultDataTypeName);
		}
		DataProvider dataProvider = viewManager.getDataProvider(dataProviderId);
		Collection<Object> collection = null;
		if (pageSize > 0) {
			Pageable pageable = PageRequest.of(pageNo-1, pageSize);
			Page<Object> page = dataProvider.getPagingResult(dataProviderParameter, pageable, resultDataType);
			collection = page.getContent();
		} else {
			collection = (Collection<Object>) dataProvider.getResult(dataProviderParameter, resultDataType);
		}
		dataList = new ArrayList<Map<String, Object>>();
		for (Object obj : collection) {
			if (obj instanceof Map) {
				dataList.add((Map<String, Object>) obj);
			} else {
				if(collection.size()<=65536){
					Map<String, Object> targetMap = new HashMap<String, Object>();
					BeanReflectionUtils.copyProperties(targetMap, obj);
					dataList.add(targetMap);
				}else{
					dataList.add(PropertyUtils.describe(obj));
				}
			}
		}
		fireGridDataInterceptor(intercepterBean, dataList);
		return dataList;*/
		return null;
	}

	@SuppressWarnings({ "unchecked" })
	public void createChildData(List<Map<String, Object>> dataList, List<Object> sub, String header, String treeColumn) {
		for (Object obj : sub) {
			Map<String, Object> tempMap = null;
			if (obj instanceof Map){
				tempMap = (Map<String, Object>)obj;
			} else {
				tempMap = new HashMap<String, Object>();
				try {
					BeanReflectionUtils.copyProperties(tempMap, obj);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			String tmp = header + "\t";
			if (tempMap.get(treeColumn) != null) {
				tempMap.put(treeColumn, tmp + tempMap.get(treeColumn));
			}
			dataList.add(tempMap);
			if (tempMap.get("children") != null) {
				this.createChildData(dataList, (List<Object>) tempMap.get("children"), tmp, treeColumn);
			}
		}
	}

	public ReportTitleStyle createTitleStyle(ExportSolution parameter) {
		ReportTitleStyle titleStyle = new ReportTitleStyle();
		int fontSize = parameter.getFontSize();
		String fontColor = parameter.getFontColor();
		String bgColor = parameter.getBgColor();
		titleStyle.setBgColor(this.createRGB(bgColor));
		titleStyle.setFontSize(fontSize);
		titleStyle.setFontColor(this.createRGB(fontColor));
		return titleStyle;
	}

	public int[] createRGB(String s) {
		if (StringUtils.isNotEmpty(s)) {
			return ColorUtils.parse2RGB(s);
		}
		return null;
	}

	public void fireGridDataInterceptor(String beanName, List<Map<String, Object>> list) throws Exception {
		fireControlDataInterceptor(beanName, SupportCompent.grid, list);
	}

	public void fireAutoFormDataInterceptor(String beanName, List<Map<String, Object>> list) throws Exception {
		fireControlDataInterceptor(beanName, SupportCompent.form, list);
	}

	private void fireControlDataInterceptor(String beanName, SupportCompent supportCompent, List<Map<String, Object>> list) throws Exception {
		if (StringUtils.isNotEmpty(beanName)) {
			Map<String, IDataInterceptor> map = SpringUtil.getApplicationContext().getBeansOfType(IDataInterceptor.class);
			for (Map.Entry<String, IDataInterceptor> entry : map.entrySet()) {
				if (entry.getValue().getName().equals(beanName)) {
					if (supportCompent.equals(SupportCompent.grid)) {
						entry.getValue().interceptGridData(list);
					} else if (supportCompent.equals(SupportCompent.form)) {
						entry.getValue().interceptAutoFormData(list);
					}
				}
			}
		}
	}

}
