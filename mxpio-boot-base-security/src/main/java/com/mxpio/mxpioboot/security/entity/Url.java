package com.mxpio.mxpioboot.security.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.access.ConfigAttribute;

import com.mxpio.mxpioboot.jpa.BaseEntity;
import com.mxpio.mxpioboot.jpa.annotation.Generator;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_URL")
public class Url extends BaseEntity implements Resource {

	private static final long serialVersionUID = 1L;

	/**

	 * 菜单资源类型

	 */
	public static final String RESOURCE_TYPE = "URL";
	
	@Id
	@Column(name = "ID_", length = 64)
	@Generator
	@ApiModelProperty(value = "ID")
	private String id;
	
	@Column(name = "TITLE_", length = 64)
	@ApiModelProperty(value = "标题")
	private String title;
	
	@Column(name = "ICON_", length = 255)
	@ApiModelProperty(value = "图标")
	private String icon;
	
	@Column(name = "NAME_", length = 64)
	@ApiModelProperty(value = "名称")
	private String name;
	
	@Column(name = "PATH_", length = 512)
	@ApiModelProperty(value = "路径")
	private String path;
	
	@Column(name = "COMPONENT_", length = 512)
	@ApiModelProperty(value = "组件")
	private String component;
	
	@Column(name = "PARENT_ID_", length = 64)
	@ApiModelProperty(value = "父ID")
	private String parentId;
	
	@Column(name = "ORDER_")
	@ApiModelProperty(value = "顺序")
	private Integer order;
	
	@Column(name = "NAVIGABLE_")
	@ApiModelProperty(value = "是否导航")
	private boolean navigable;
	
	@Column(name = "KEEP_ALIVE_")
	@ApiModelProperty(value = "是否缓存")
	private boolean keepAlive;
	
	@Column(name = "DESCRIPTION_", length = 255)
	@ApiModelProperty(value = "描述")
	private String description;
	
	@Transient
	private List<Url> children;
	
	@Transient
	private List<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();

}
