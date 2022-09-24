package com.mxpioframework.excel.importer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mxpioframework.jpa.annotation.Generator;
import com.mxpioframework.security.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Schema(description="Excel导入参数")
@Table(name = "MB_EXCEL_ENTRY")
public class Entry extends BaseEntity {

	private static final long serialVersionUID = -6974265493073776949L;

	@Id
	@Generator
	@Column(name = "ID_", length = 36)
	@Schema(description = "ID")
	private String id;
	
	@Schema(description = "关键字")
	@Column(name = "KEY_", length = 100)
	private String key;
	
	@Column(name = "VALUE_", length = 100)
	@Schema(description = "值")
	private String value;
	
	@Column(name = "MAPPING_RULE_ID_", length = 36)
	@Schema(description = "映射规则ID")
	private String mappingRuleId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMappingRuleId() {
		return mappingRuleId;
	}

	public void setMappingRuleId(String mappingRuleId) {
		this.mappingRuleId = mappingRuleId;
	}

}
