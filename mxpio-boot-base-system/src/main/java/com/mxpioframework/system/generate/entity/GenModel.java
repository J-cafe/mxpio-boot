package com.mxpioframework.system.generate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mxpioframework.jpa.BaseEntity;
import com.mxpioframework.jpa.annotation.Generator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_GENERATE_MODEL")
@ApiModel(value="领域模型")
public class GenModel extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator
	@Column(name = "ID_")
	@ApiModelProperty(value = "ID")
	private String id;
	
	@Column(name = "SYSTEM_ID_")
	@ApiModelProperty(value = "系统ID")
	private String systemId;
	
	@Column(name = "TABLE_NAME_")
	@ApiModelProperty(value = "数据库表名")
	private String tableName;
	
	@Column(name = "MODEL_NAME_")
	@ApiModelProperty(value = "模型名称")
	private String modelName;
	
	@Column(name = "MODEL_TYPE_")
	@ApiModelProperty(value = "模型类型")
	private String modelType;
	
	@Column(name = "MODEL_VERSION_")
	@ApiModelProperty(value = "版本")
	private String modelVersion;
	
	@Column(name = "MODEL_STATUS_")
	@ApiModelProperty(value = "状态")
	private String modelStatus;
	
	@Column(name = "PACKAGE_NAME_")
	@ApiModelProperty(value = "包名")
	private String packageName;
	
	@Column(name = "TREE_ABLE_")
	@ApiModelProperty(value = "是否树")
	private boolean treeAble;
	
	@Column(name = "TREE_CHILDREN_")
	@ApiModelProperty(value = "叶子属性")
	private String treeChildren;
	
	@Column(name = "CURRENT_NODE_KEY_")
	@ApiModelProperty(value = "当前节点属性")
	private String currentNodeKey;
	
	@Column(name = "PARENT_NODE_KEY_")
	@ApiModelProperty(value = "父节点属性")
	private String parentNodeKey;
	
	@Column(name = "MODEL_DESC_")
	@ApiModelProperty(value = "属性描述")
	private String modelDesc;

}
