package com.mxpioframework.security.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.access.ConfigAttribute;

import com.mxpioframework.jpa.BaseEntity;
import com.mxpioframework.jpa.annotation.Generator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "MB_ELEMENT")
@ApiModel(value="组件对象")
@ToString
public class Element extends BaseEntity implements Resource {

	private static final long serialVersionUID = 1L;

	private ResourceType resourceType = ResourceType.ELEMENT;

	@Id
	@Generator
	@Column(name = "ID_", length = 64)
	@ApiModelProperty(value = "ID",example="mockOutputStrValue")
	private String id;

	@Column(name = "ELEMENT_ID_", length = 255)
	@ApiModelProperty(value = "组件标识")
	private String elementId;

	@Column(name = "ELEMENT_TYPE", length = 64)
	@Enumerated(EnumType.STRING)
	@ApiModelProperty(value = "组件类型")
	private ElementType elementType;

	@Column(name = "NAME_", length = 64)
	@ApiModelProperty(value = "组件名称")
	private String name;

	@Column(name = "URL_ID_", length = 64)
	@ApiModelProperty(value = "所属URL的ID")
	private String urlId;

	@Column(name = "PATH_", length = 512)
	@ApiModelProperty(value = "组件路径")
	private String path;

	@Column(name = "DESCRIPTION_", length = 512)
	@ApiModelProperty(value = "组件描述")
	private String description;

	@Transient
	private boolean authorized;

	@Transient
	private String configAttributeId;

	@Transient
	private List<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();

}
