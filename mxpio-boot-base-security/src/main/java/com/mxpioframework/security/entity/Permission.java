package com.mxpioframework.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.access.ConfigAttribute;

import com.mxpioframework.jpa.BaseEntity;
import com.mxpioframework.jpa.annotation.Generator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

@Entity
@Table(name = "MB_PERMISSION")
@ToString
@ApiModel(value="权限对象")
public class Permission extends BaseEntity implements ConfigAttribute{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator
	@Column(name = "ID_", length = 64)
	@ApiModelProperty(value = "ID")
	private String id;
	
	@Column(name = "ROLE_ID_", length = 64)
	@ApiModelProperty(value = "角色ID")
	private String roleId;
	
	@Column(name = "RESOURCE_ID_", length = 64)
	@ApiModelProperty(value = "资源ID")
	private String resourceId;
	
	@Column(name = "RESOURCE_TYPE_", length = 32)
	@ApiModelProperty(value = "资源类型")
	private String resourceType;
	
	@Column(name = "ATTRIBUTE_", length = 255)
	@ApiModelProperty(value = "资源属性")
	private String attribute;
	
	@Transient
	@ApiModelProperty(value = "角色对象")
	private Role role;
	
	@Transient
	@ApiModelProperty(value = "组件对象")
	private Element element;
	
	@Transient
	@ApiModelProperty(value = "菜单对象")
	private Url url;
	
	public Url getUrl() {
		return url;
	}

	public void setUrl(Url url) {
		this.url = url;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**

	 * 角色ID

	 * @return roleId

	 */
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	/**

	 * 资源ID，如菜单和组件，以及用户定义的资源

	 * @return resourceId

	 */
	public String getResourceId() {
		return resourceId;
	}
	
	/**

	 * 资源类型，菜单为URL，组件为ELEMENT

	 * @return resourceType

	 */
	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	@Override
	/**

	 * 权限鉴别属性，默认格式：ROLE_{roleId}，可以扩展

	 * @return attribute

	 */
	public String getAttribute() {
		return attribute;
	}

}