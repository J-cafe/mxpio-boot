package com.mxpioframework.system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.mxpioframework.security.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_SERIAL_NUMBER")
@Schema(description="序列号")
@ToString
public class SerialNumber extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SN_EXPRESSION_")
	@Schema(description = "序列号表达式")
	private String snExpression;

	@Column(name = "CURRENT_RECORD_")
	@Schema(description = "当前记录")
	private String currentRecord;

}
