package com.mxpioframework.lowcode.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mxpioframework.jpa.annotation.Generator;
import com.mxpioframework.security.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = "model")
@Entity
@Table(name = "MB_LOWCODE_PROPERTY")
@Schema(description = "低代码模型属性")
public class LowcodeProperty extends BaseEntity {

	@Id
	@Generator
	@Column(name = "ID_", updatable = false)
	private String id;

	public enum ShowType {
		TEXT, NUMBER, DATE, DATETIME, SELECT, DICT, TEXTAREA, RICH_TEXT, CHECKBOX, RADIO, FILE, IMAGE
	}

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODEL_ID_")
	@Schema(description = "所属模型")
	private LowcodeModel model;

	@Column(name = "PROPERTY_CODE_", length = 100, nullable = false)
	@Schema(description = "属性编码")
	private String propertyCode;

	@Column(name = "PROPERTY_NAME_", length = 200)
	@Schema(description = "属性显示名")
	private String propertyName;

	@Column(name = "COLUMN_NAME_", length = 200)
	@Schema(description = "数据库列名")
	private String columnName;

	@Column(name = "COLUMN_TYPE_", length = 50)
	@Schema(description = "列类型")
	private String columnType;

	@Column(name = "COLUMN_LENGTH_")
	@Schema(description = "列长度")
	private Integer columnLength;

	@Column(name = "PK_FLAG_")
	@Schema(description = "是否主键")
	private Boolean pkFlag;

	@Column(name = "NULLABLE_FLAG_")
	@Schema(description = "是否可空")
	private Boolean nullableFlag;

	@Column(name = "SHOW_TYPE_", length = 50)
	@Enumerated(EnumType.STRING)
	@Schema(description = "显示类型")
	private ShowType showType;

	@Column(name = "DICT_CODE_", length = 100)
	@Schema(description = "关联字典编码")
	private String dictCode;

	@Column(name = "SORT_ORDER_")
	@Schema(description = "排序")
	private Integer sortOrder;

	@Column(name = "QUERYABLE_FLAG_")
	@Schema(description = "是否可查询")
	private Boolean queryableFlag;

	@Column(name = "LISTABLE_FLAG_")
	@Schema(description = "是否在列表显示")
	private Boolean listableFlag;

	@Column(name = "EDITABLE_FLAG_")
	@Schema(description = "是否可编辑")
	private Boolean editableFlag;

	@Column(name = "REQUIRED_FLAG_")
	@Schema(description = "是否必填")
	private Boolean requiredFlag;

	@Column(name = "DEFAULT_VALUE_", length = 500)
	@Schema(description = "默认值")
	private String defaultValue;

	@Column(name = "REF_MODEL_CODE_", length = 100)
	@Schema(description = "引用模型编码")
	private String refModelCode;

	@Column(name = "REF_PROPERTY_CODE_", length = 100)
	@Schema(description = "引用模型显示属性")
	private String refPropertyCode;

	@Column(name = "VALIDATION_RULE_", length = 1000)
	@Schema(description = "校验规则JSON")
	private String validationRule;

}
