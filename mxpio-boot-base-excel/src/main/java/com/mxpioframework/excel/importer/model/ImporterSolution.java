package com.mxpioframework.excel.importer.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "MB_EXCEL_IMPORTER_SOLUTION")
public class ImporterSolution implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID_", length = 64)
	@ApiModelProperty(value = "方案编码")
	private String id;
	
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
	@Column(name = "Entity_Manager_Factory_Name_", length = 60)
	private String EntityManagerFactoryName;
	
	@Column(name = "CREATE_DATE_")
	@ApiModelProperty(value = "创建时间")
	private Date createDate;
	
	@Column(name = "START_ROW_")
	@ApiModelProperty(value = "起始行")
	private Integer startRow;

	@OneToMany
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
		return EntityManagerFactoryName;
	}

	public void setEntityManagerFactoryName(String entityManagerFactoryName) {
		EntityManagerFactoryName = entityManagerFactoryName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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

}