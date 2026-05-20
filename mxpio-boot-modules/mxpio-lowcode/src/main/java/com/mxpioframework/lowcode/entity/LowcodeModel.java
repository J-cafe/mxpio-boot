package com.mxpioframework.lowcode.entity;

import java.util.ArrayList;
import java.util.List;

import com.mxpioframework.jpa.annotation.Generator;
import com.mxpioframework.security.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
@Table(name = "MB_LOWCODE_MODEL")
@Schema(description = "低代码数据模型")
public class LowcodeModel extends BaseEntity {

	@Id
	@Generator
	@Column(name = "ID_", updatable = false)
	private String id;

	@Column(name = "MODEL_CODE_", length = 100, unique = true, nullable = false)
	@Schema(description = "模型编码")
	private String modelCode;

	@Column(name = "MODEL_NAME_", length = 200)
	@Schema(description = "模型名称")
	private String modelName;

	@Column(name = "ENTITY_CLASS_", length = 500)
	@Schema(description = "实体类全限定名")
	private String entityClass;

	@Column(name = "TABLE_NAME_", length = 200)
	@Schema(description = "数据库表名（从@Entity注解自动提取）")
	private String tableName;

	@Column(name = "DESCRIPTION_", length = 500)
	@Schema(description = "描述")
	private String description;

	@OneToMany(mappedBy = "model", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("sortOrder ASC")
	@Schema(description = "属性列表")
	private List<LowcodeProperty> properties = new ArrayList<>();

}
