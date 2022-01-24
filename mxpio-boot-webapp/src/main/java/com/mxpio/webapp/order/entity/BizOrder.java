package com.mxpio.webapp.order.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.mxpioframework.jpa.BaseEntity;
import com.mxpioframework.jpa.annotation.Generator;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper=false)
public class BizOrder extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "BIZ_NO_", updatable = false)
	@Generator(policy = BizCodeGeneratorPolicy.class)
	@ApiModelProperty(value = "业务单号")
	private String bizNo;
}
