package com.mxpio.webapp.order.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;

import com.mxpioframework.jpa.BaseEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper=false)
public class BizLine extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private BizLineKey id;
	
	@Column(name = "SORT_")
	@ApiModelProperty(value = "顺序号")
	private String sort;

}
