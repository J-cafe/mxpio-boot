package com.mxpioframework.module.datav.entity;

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
@Table(name = "MB_DATAV_SCREEN_CONFIG")
@Schema(description="DataV大屏配置")
public class DatavScreenConfig extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator
	@Column(name = "ID_", updatable = false)
	@Schema(description = "ID")
	private String id;
	
	@Column(name = "SCREEN_ID_")
	@Schema(description = "大屏ID")
	private String screenId;
	
	@Column(name = "BG_GROUND_")
	@Schema(description = "背景")
	private String bgGround;
	
}
