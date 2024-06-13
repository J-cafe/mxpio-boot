package com.mxpioframework.security.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_URL")
@Schema(description="菜单对象")
@ToString
public class Url extends BaseEntity implements Resource {

	private static final long serialVersionUID = 1L;

	@Transient
	private ResourceType resourceType = ResourceType.URL;

	@Id
	@Column(name = "ID_", length = 64)
	@Generator
	@Schema(description = "ID")
	private String id;

	@Column(name = "TITLE_", length = 64)
	@Schema(description = "标题")
	private String title;

	@Column(name = "RUL_TYPE_", length = 64)
	@Schema(description = "目录/菜单")
	private String urlType;

	@Column(name = "ICON_", length = 255)
	@Schema(description = "图标")
	private String icon;

	@Column(name = "NAME_", length = 64)
	@Schema(description = "名称")
	private String name;

	@Column(name = "PATH_", length = 512)
	@Schema(description = "路径")
	private String path;

	@Column(name = "COMPONENT_", length = 512)
	@Schema(description = "组件")
	private String component;

	@Column(name = "PARENT_ID_", length = 64)
	@Schema(description = "父ID")
	private String parentId;

	@Column(name = "ORDER_")
	@Schema(description = "顺序")
	private Integer order;

	@Column(name = "NAVIGABLE_")
	@Schema(description = "是否导航")
	private boolean navigable;

	@Column(name = "KEEP_ALIVE_")
	@Schema(description = "是否缓存")
	private boolean keepAlive;

	@Column(name = "OUTSIDE_")
	@Schema(description = "是否外部")
	private boolean outside;

	@Column(name = "DESCRIPTION_", length = 255)
	@Schema(description = "描述")
	private String description;

	@Transient
	private List<Url> children;

	@Transient
	private List<DataResource> datas;

	@Transient
	private List<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();

}
