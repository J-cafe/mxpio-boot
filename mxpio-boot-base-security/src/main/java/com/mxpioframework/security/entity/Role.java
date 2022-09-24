package com.mxpioframework.security.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;

import com.mxpioframework.jpa.annotation.Generator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.ToString;

@Entity
@Table(name = "MB_ROLE")
@Schema(description="角色对象")
@ToString
public class Role extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID_", length = 64)
	@Generator
	@Schema(description = "ID")
	private String id;
	
	@Column(name = "NAME_", length = 64)
	@Schema(description = "角色名称")
	private String name;
	
	@Column(name = "DESCRIPTION_", length = 255)
	@Schema(description = "角色描述")
	private String description;
	
	@Transient
	@Schema(description = "权限列表", hidden = true)
	private List<Permission> permissions;
	
	@Transient
	@Schema(description = "授权信息", hidden = true)
	private List<GrantedAuthority> authorities;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**

	 * 名称

	 * @return name

	 */
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/**

	 * 描述

	 * @return description

	 */
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**

	 * 权限信息

	 * @return permissions

	 */
	public List<Permission> getPermissions() {
		return permissions;
	}
	
	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	/**

	 * 授权权限信息

	 * @return authorities

	 */
	public List<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

}
