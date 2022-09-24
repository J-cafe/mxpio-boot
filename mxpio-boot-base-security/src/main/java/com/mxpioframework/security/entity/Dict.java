package com.mxpioframework.security.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.mxpioframework.jpa.annotation.Generator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_DICT")
@Schema(description="字典")
@ToString
public class Dict extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator
	@Column(name = "ID_", updatable = false)
	@Schema(description = "ID", hidden = true)
	private String id;
	
	@Column(name = "DICT_CODE_", updatable = false)
	@Schema(description = "编号")
	private String dictCode;
	
	@Column(name = "DICT_NAME_")
	@Schema(description = "名称")
	private String dictName;
	
	@Column(name = "DICT_DESC_")
	@Schema(description = "描述")
	private String dictDesc;
	
	@Column(name = "DICT_TYPE_")
	@Schema(description = "类别")
	private String dictType;
	
	@Column(name = "DICT_DEFAULT_VALUE_")
	@Schema(description = "默认值")
	private String dictDefaultValue;
	
	@Transient
	private List<DictItem> items;

}
