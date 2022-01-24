package com.mxpio.webapp.order.entity;

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
@Table(name = "MB_ERP_ITEM")
@ApiModel(value="物料")
public class Item extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator(policy = ResCodeGeneratorPolicy.class)
	@Column(name = "ITEM_CODE_", updatable = false)
	@ApiModelProperty(value = "物料编号")
	private String itemCode;

}
