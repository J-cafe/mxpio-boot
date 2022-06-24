package com.mxpioframework.system.generate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mxpioframework.jpa.BaseEntity;
import com.mxpioframework.jpa.annotation.Generator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_GENERATE_PROPERTY")
@ApiModel(value="模型属性")
public class GenProperty extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator
	@Column(name = "ID_")
	@ApiModelProperty(value = "ID")
	private String id;
	
	@Column(name = "MODEL_ID_")
	@ApiModelProperty(value = "模型ID")
	private String modelId;
	
	@Column(name = "PROPERTY_NAME_")
	@ApiModelProperty(value = "属性名称")
	private String propertyName;
	
	@Column(name = "COLUMN_NAME_")
	@ApiModelProperty(value = "字段名称")
	private String columnName;
	
	@Column(name = "COLUMN_TYPE_")
	@ApiModelProperty(value = "字段类型")
	private String columnType;
	
	@Column(name = "COLUMN_LENGTH_")
	@ApiModelProperty(value = "字段长度")
	private String columnLength;
	
	@Column(name = "COLUMN_PRECISION_")
	@ApiModelProperty(value = "字段精度")
	private String columnPrecision;
	
	@Column(name = "COLUMN_SCALE_")
	@ApiModelProperty(value = "小数位数")
	private String columnScale;
	
	@Column(name = "UNIQUE_")
	@ApiModelProperty(value = "是否唯一")
	private boolean unique;
	
	@Column(name = "NULLABLE_")
	@ApiModelProperty(value = "是否可空")
	private boolean nullable;
	
	@Column(name = "SHOW_TYPE_")
	@ApiModelProperty(value = "展示类型")
	private String showType;
	
	@Column(name = "SHOW_FORMAT_")
	@ApiModelProperty(value = "格式化")
	private String showFormat;
	
	@Column(name = "DICE_CODE_")
	@ApiModelProperty(value = "字典编码")
	private String dictCode;

}
