package com.mxpioframework.security.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import org.springframework.security.access.ConfigAttribute;

import com.mxpioframework.jpa.annotation.Generator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "MB_ELEMENT")
@Schema(description="组件对象")
@ToString
public class Element extends BaseEntity implements Resource {

	private static final long serialVersionUID = 1L;

	@Transient
	private ResourceType resourceType = ResourceType.ELEMENT;

	@Id
	@Generator
	@Column(name = "ID_", length = 64)
	@Schema(description = "ID",example="mockOutputStrValue")
	private String id;

	@Column(name = "ELEMENT_ID_", length = 255)
	@Schema(description = "组件标识")
	private String elementId;

	@Column(name = "ELEMENT_TYPE", length = 64)
	@Enumerated(EnumType.STRING)
	@Schema(description = "组件类型")
	private ElementType elementType;

	@Column(name = "TITLE_", length = 64)
	@Schema(description = "组件名称")
	private String title;

	@Column(name = "PARENT_ID_", length = 64)
	@Schema(description = "所属URL的ID")
	private String parentId;

	@Column(name = "PATH_", length = 512)
	@Schema(description = "组件路径")
	private String path;

	@Column(name = "DESCRIPTION_", length = 512)
	@Schema(description = "组件描述")
	private String description;

	@Transient
	private boolean authorized;

	@Transient
	private String configAttributeId;

	@Transient
	private List<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();

}
