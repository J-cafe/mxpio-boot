package com.mxpioframework.security.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.mxpioframework.jpa.BaseEntity;
import com.mxpioframework.jpa.annotation.Generator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_DICT")
@ApiModel(value="字典")
@ToString
public class Dict extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator
	@Column(name = "ID_", updatable = false)
	@ApiModelProperty(value = "ID", hidden = true)
	private String id;
	
	@Column(name = "DICT_CODE_", updatable = false)
	@ApiModelProperty(value = "编号")
	private String dictCode;
	
	@Column(name = "DICT_NAME_")
	@ApiModelProperty(value = "名称")
	private String dictName;
	
	@Column(name = "DICT_DESC_")
	@ApiModelProperty(value = "描述")
	private String dictDesc;
	
	@Column(name = "DICT_TYPE_")
	@ApiModelProperty(value = "类别")
	private String dictType;
	
	@Column(name = "DICT_DEFAULT_VALUE_")
	@ApiModelProperty(value = "默认值")
	private String dictDefaultValue;
	
	@Transient
	private List<DictItem> items;

}
