package com.mxpioframework.security.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_DB_DATATYPE")
@Schema(description="实体类型")
public class DataType extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Schema(description = "编码")
	@Column(name = "CODE_")
	private String code;
	
	@Schema(description = "名称")
	@Column(name = "NAME_")
	private String name;
	
	@Schema(description = "引用类")
	@Column(name = "CTEATION_TYPE_")
	private String creationType;
	
	@Transient
	private List<DataProperty> properties;

}
