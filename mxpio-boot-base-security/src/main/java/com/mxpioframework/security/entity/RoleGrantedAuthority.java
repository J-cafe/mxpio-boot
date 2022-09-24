package com.mxpioframework.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import com.mxpioframework.jpa.annotation.Generator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.ToString;

@Entity
@Table(name = "MB_ROLE_GRANTED_AUTHORITY")
@Schema(description="角色授权对象")
@ToString
public class RoleGrantedAuthority extends BaseEntity implements GrantedAuthority {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID_", length = 64)
	@Generator
	@Schema(description = "ID")
	private String id;
	
	@Column(name = "ACTOR_ID_", length = 64)
	@Schema(description = "演员ID")
	private String actorId;
	
	@Column(name = "ROLE_ID_", length = 64)
	@Schema(description = "角色ID")
	private String roleId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**

	 * 演员ID，及角色被赋予者ID

	 * @return actorId

	 */
	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
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

	@Override
	public String getAuthority() {
		if (StringUtils.isEmpty(roleId)) {
			return null;
		}
		return "ROLE_" + roleId;
	}

}
