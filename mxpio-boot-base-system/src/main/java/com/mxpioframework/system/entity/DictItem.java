package com.mxpioframework.system.entity;

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
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_DICT_ITEM")
@ApiModel(value="字典项")
@ToString
public class DictItem extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator
	@Column(name = "ID_", updatable = false)
	@ApiModelProperty(value = "ID", hidden = true)
	private String id;
	
	@Column(name = "DICT_ID_")
	@ApiModelProperty(value = "字典ID")
	private String dictId;
	
	@Column(name = "ITEM_TEXT_")
	@ApiModelProperty(value = "显示文本")
	private String itemText;
	
	@Column(name = "ITEM_VALUE_")
	@ApiModelProperty(value = "字典值")
	private String itemValue;
	
	@Column(name = "ITEM_DESC_")
	@ApiModelProperty(value = "描述")
	private String itemDesc;
	
	@Column(name = "ITEM_SORT_")
	@ApiModelProperty(value = "排序")
	private String itemSort;
	
	@Column(name = "ITEM_STATUS_")
	@ApiModelProperty(value = "状态")
	private String itemStatus;

}
