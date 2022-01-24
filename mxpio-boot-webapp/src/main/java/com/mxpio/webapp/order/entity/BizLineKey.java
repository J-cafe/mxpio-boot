package com.mxpio.webapp.order.entity;

import java.io.Serializable;

import javax.persistence.Column;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BizLineKey implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "LINE_NO_", updatable = false)
	@ApiModelProperty(value = "行号")
	private String lineNo;
	
	@Column(name = "BIZ_NO_", updatable = false)
	@ApiModelProperty(value = "业务单号")
	private String bizNo;
}
