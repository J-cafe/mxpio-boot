package com.mxpioframework.excel.importer.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.mxpioframework.excel.importer.parser.CellPostParser;
import com.mxpioframework.excel.importer.parser.CellPreParser;
import com.mxpioframework.jpa.annotation.Generator;
import com.mxpioframework.security.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Excel导入映射规则")
@Entity
@Table(name = "MB_EXCEL_MAPPING_RULE")
public class MappingRule extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	@Generator
	@Column(name = "ID_", length = 36)
	@Schema(description = "ID")
	private String id;

	@Schema(description = "名称")
	@Column(name = "NAME_", length = 255)
	private String name;

	@Column(name = "IMPORTER_SOLUTION_ID_", length = 64)
	private String importerSolutionId;

	@Schema(description = "Excel列号")
	@Column(name = "EXCEL_COLUMN_")
	private int excelColumn;

	@Schema(description = "忽略错误格式数据")
	@Column(name = "IGNORE_ERROR_FORMAT_DATA_")
	private boolean ignoreErrorFormatData;

	@Schema(description = "实体属性")
	@Column(name = "PROPERTY_NAME_", length = 60)
	private String propertyName;

	@Schema(description = "单元格后置解析器")
	@Column(name = "CELL_POST_PARSER_BEAN_", length = 255)
	private String cellPostParserBean = CellPostParser.DEFAULT;

	@Schema(description = "单元格后置解析器参数")
	@Column(name = "CELL_POST_PARSER_PARAM_", length = 255)
	private String cellPostParserParam;

	@Schema(description = "单元格前置解析器")
	@Column(name = "CELL_PREV_PARSER_BEAN_", length = 255)
	private String cellPreParserBean = CellPreParser.DEFAULT;

	@Schema(description = "单元格前置解析器参数")
	@Column(name = "CELL_PREV_PARSER_PARAM_", length = 255)
	private String cellPreParserParam;

	@Transient
	private ImporterSolution importerSolution;

	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JoinColumn(name = "MAPPING_RULE_ID_", insertable = false, updatable = false)
	private List<Entry> entries;

	@Transient
	private Map<String, String> map;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImporterSolutionId() {
		return importerSolutionId;
	}

	public void setImporterSolutionId(String importerSolutionId) {
		this.importerSolutionId = importerSolutionId;
	}

	public int getExcelColumn() {
		return excelColumn;
	}

	public void setExcelColumn(int excelColumn) {
		this.excelColumn = excelColumn;
	}


	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}


	public ImporterSolution getImporterSolution() {
		return importerSolution;
	}

	public void setImporterSolution(ImporterSolution importerSolution) {
		this.importerSolution = importerSolution;
	}

	public String getCellPostParserBean() {
		return cellPostParserBean;
	}

	public void setCellPostParserBean(String cellPostParserBean) {
		this.cellPostParserBean = cellPostParserBean;
	}

	public String getCellPostParserParam() {
		return cellPostParserParam;
	}

	public void setCellPostParserParam(String cellPostParserParam) {
		this.cellPostParserParam = cellPostParserParam;
	}

	public String getCellPreParserBean() {
		return cellPreParserBean;
	}

	public void setCellPreParserBean(String cellPreParserBean) {
		this.cellPreParserBean = cellPreParserBean;
	}

	public String getCellPreParserParam() {
		return cellPreParserParam;
	}

	public void setCellPreParserParam(String cellPreParserParam) {
		this.cellPreParserParam = cellPreParserParam;
	}

	public List<Entry> getEntries() {
		return entries;
	}



	public boolean isIgnoreErrorFormatData() {
		return ignoreErrorFormatData;
	}

	public void setIgnoreErrorFormatData(boolean ignoreErrorFormatData) {
		this.ignoreErrorFormatData = ignoreErrorFormatData;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

	public String getMappingValueIfNeed(String key) {
		String value = key;
		if(entries != null && !entries.isEmpty() && StringUtils.isNotEmpty(key)) {
			if (map == null) {
				map = new HashMap<>();
				for (Entry entry : entries) {
					map.put(entry.getKey(), entry.getValue());
				}
			}
			value = map.get(key);
			if (value == null) {
				throw new RuntimeException("[" + key+ "] 不在枚举范围内。");
			}
		}

		return value;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

}
