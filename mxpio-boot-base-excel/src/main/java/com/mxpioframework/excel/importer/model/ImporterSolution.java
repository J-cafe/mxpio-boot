package com.mxpioframework.excel.importer.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.mxpioframework.jpa.BaseEntity;
import com.mxpioframework.jpa.annotation.Generator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@ApiModel(value="Excel导入方案")
@Table(name = "MB_EXCEL_IMPORTER_SOLUTION")
public class ImporterSolution extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	@Generator
	@Column(name = "ID_", length = 64)
	@ApiModelProperty(value = "ID")
	private String id;
	
	@Column(name = "CODE_", length = 64, unique = true)
	@ApiModelProperty(value = "方案编码")
	private String code;
	
	@ApiModelProperty(value = "方案名称")
	@Column(name = "NAME_", length = 60)
	private String name;
	
	@ApiModelProperty(value = "Sheet页名称")
	@Column(name = "EXCEL_SHEET_NAME_", length = 60)
	private String excelSheetName;
	
	@Column(name = "ENTITY_CLASS_NAME_", length = 255)
	@ApiModelProperty(value = "实体类")
	private String entityClassName;
	
	@ApiModelProperty(value = "描述")
	@Column(name = "DESC_", length = 255)
	private String desc;
	
	@ApiModelProperty(value = "实体管理工厂")
	@Column(name = "ENTITY_MANAGER_FACTORY_NAME_", length = 60)
	private String entityManagerFactoryName;
	
	@Column(name = "START_ROW_")
	@ApiModelProperty(value = "起始行")
	private Integer startRow;

	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JoinColumn(name = "IMPORTER_SOLUTION_ID_", insertable = false, updatable = false)
	private List<MappingRule> mappingRules = new ArrayList<MappingRule>();

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

	public String getExcelSheetName() {
		return excelSheetName;
	}

	public void setExcelSheetName(String excelSheetName) {
		this.excelSheetName = excelSheetName;
	}

	public String getEntityClassName() {
		return entityClassName;
	}

	public void setEntityClassName(String entityClassName) {
		this.entityClassName = entityClassName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getEntityManagerFactoryName() {
		return entityManagerFactoryName;
	}

	public void setEntityManagerFactoryName(String entityManagerFactoryName) {
		this.entityManagerFactoryName = entityManagerFactoryName;
	}

	public List<MappingRule> getMappingRules() {
		return mappingRules;
	}

	public void setMappingRules(List<MappingRule> mappingRules) {
		this.mappingRules = mappingRules;
	}

	public Integer getStartRow() {
		return startRow;
	}

	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
