package com.mxpioframework.security.entity;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.mxpioframework.jpa.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

/**
 * 用戶模型
 * 
 * @author MxpIO
 *
 */
@Entity
@Table(name = "MB_USER")
@ToString
@ApiModel(value="用户对象")
public class User extends BaseEntity implements UserDetails, OrganizationSupport, Actor {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "USERNAME_", length = 64)
	@ApiModelProperty(value = "用户名")
	private String username;

	@Column(name = "NICKNAME_", length = 64)
	@ApiModelProperty(value = "昵称")
	private String nickname;
	
	@Column(name = "AVATAR_", length = 64)
	@ApiModelProperty(value = "头像")
	private String avatar;

	@Column(name = "PASSWORD_", length = 125)
	@JsonIgnore
	@ApiModelProperty(value = "密码")
	private String password;
	
	@Column(name = "EMAIL_", length = 125)
	@ApiModelProperty(value = "邮箱")
	private String email;

	@Column(name = "ADMINISTRATOR_")
	@ApiModelProperty(value = "是否管理员")
	private boolean administrator = false;

	@Column(name = "ACCOUNT_NON_EXPIRED_")
	@ApiModelProperty(value = "是否有效")
	private boolean accountNonExpired = true;

	@Column(name = "ACCOUNT_NON_LOCKED_")
	@ApiModelProperty(value = "可用")
	private boolean accountNonLocked = true;

	@Column(name = "CREDENTIALS_NON_EXPIRED_")
	@ApiModelProperty(value = "凭证可用")
	private boolean credentialsNonExpired = true;

	@Column(name = "ENABLED_")
	private boolean enabled = true;
	
	@Column(name = "SALT_")
	@ApiModelProperty(value = "盐值")
	private String salt;
	
	@Column(name = "INTRODUCTION_")
	@ApiModelProperty(value = "简介")
	private String introduction;

	@Transient
	private Object organization;

	@Transient
	private Collection<? extends GrantedAuthority> authorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdministrator() {
		return administrator;
	}

	public void setAdministrator(boolean administrator) {
		this.administrator = administrator;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	/**
	 * 
	 * 昵称
	 * 
	 * @return nickname
	 * 
	 */
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getOrganization() {
		return (T) organization;
	}

	@Override
	public <T> void setOrganization(T organization) {
		this.organization = organization;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	@Override
	public String getActorId() {
		return getUsername();
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
}
