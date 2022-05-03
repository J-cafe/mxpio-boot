package com.mxpioframework.system.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mxpioframework.jpa.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_SERIAL_NUMBER")
@ApiModel(value="序列号")
@ToString
public class SerialNumber extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SN_EXPRESSION_", updatable = false)
	@ApiModelProperty(value = "序列号表达式")
	private String snExpression;
	
	@Column(name = "CURRENT_RECORD_", updatable = false)
	@ApiModelProperty(value = "当前记录")
	private String currentRecord;

}
