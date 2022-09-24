package com.mxpioframework.system.generate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mxpioframework.jpa.annotation.Generator;
import com.mxpioframework.security.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_GENERATE_PROPERTY")
@Schema(description="模型属性")
public class GenProperty extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator
	@Column(name = "ID_")
	@Schema(description = "ID")
	private String id;
	
	@Column(name = "MODEL_ID_")
	@Schema(description = "模型ID")
	private String modelId;
	
	@Column(name = "PROPERTY_CODE_")
	@Schema(description = "属性编码")
	private String propertyCode;
	
	@Column(name = "PROPERTY_NAME_")
	@Schema(description = "属性名称")
	private String propertyName;
	
	@Column(name = "COLUMN_NAME_")
	@Schema(description = "字段名称")
	private String columnName;
	
	@Column(name = "COLUMN_TYPE_")
	@Schema(description = "字段类型")
	private String columnType;
	
	@Column(name = "COLUMN_LENGTH_")
	@Schema(description = "字段长度")
	private Integer columnLength;
	
	@Column(name = "COLUMN_PRECISION_")
	@Schema(description = "字段精度")
	private Integer columnPrecision;
	
	@Column(name = "COLUMN_SCALE_")
	@Schema(description = "小数位数")
	private Integer columnScale;
	
	@Column(name = "KEY_")
	@Schema(description = "是否主键")
	private boolean key;
	
	@Column(name = "UNIQUE_")
	@Schema(description = "是否唯一")
	private boolean unique;
	
	@Column(name = "NULLABLE_")
	@Schema(description = "是否可空")
	private boolean nullable;
	
	@Column(name = "VALUE_RULE_")
	@Schema(description = "值生成规则")
	private String valueRule;
	
	@Column(name = "SHOW_TYPE_")
	@Schema(description = "展示类型")
	private String showType;
	
	@Column(name = "SHOW_FORMAT_")
	@Schema(description = "格式化")
	private String showFormat;
	
	@Column(name = "DICE_CODE_")
	@Schema(description = "字典编码")
	private String dictCode;

}
