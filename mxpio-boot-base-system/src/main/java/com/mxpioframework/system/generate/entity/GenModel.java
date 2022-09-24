package com.mxpioframework.system.generate.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.mxpioframework.jpa.annotation.Generator;
import com.mxpioframework.security.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_GENERATE_MODEL")
@Schema(description="领域模型")
public class GenModel extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator
	@Column(name = "ID_")
	@Schema(description = "ID")
	private String id;
	
	@Column(name = "SYSTEM_ID_")
	@Schema(description = "系统ID")
	private String systemId;
	
	@Column(name = "TABLE_NAME_")
	@Schema(description = "数据库表名")
	private String tableName;
	
	@Column(name = "MODEL_CODE_")
	@Schema(description = "模型编码")
	private String modelCode;
	
	@Column(name = "MODEL_NAME_")
	@Schema(description = "模型名称")
	private String modelName;
	
	@Column(name = "MODEL_TYPE_")
	@Schema(description = "模型类型")
	private String modelType;
	
	@Column(name = "MODEL_VERSION_")
	@Schema(description = "版本")
	private String modelVersion;
	
	@Column(name = "MODEL_STATUS_")
	@Schema(description = "状态")
	private String modelStatus;
	
	@Column(name = "PACKAGE_NAME_")
	@Schema(description = "子包名")
	private String packageName;
	
	@Column(name = "TREE_ABLE_")
	@Schema(description = "是否树")
	private boolean treeAble;
	
	@Column(name = "TREE_CHILDREN_")
	@Schema(description = "叶子属性")
	private String treeChildren;
	
	@Column(name = "CURRENT_NODE_KEY_")
	@Schema(description = "当前节点属性")
	private String currentNodeKey;
	
	@Column(name = "PARENT_NODE_KEY_")
	@Schema(description = "父节点属性")
	private String parentNodeKey;
	
	@Column(name = "MODEL_DESC_")
	@Schema(description = "属性描述")
	private String modelDesc;
	
	@Transient
	private List<GenProperty> genProperties;

}
