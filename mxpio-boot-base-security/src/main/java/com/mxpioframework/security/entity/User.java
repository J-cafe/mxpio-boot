package com.mxpioframework.security.entity;

import java.util.Collection;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description="用户对象")
public class User extends BaseEntity implements UserDetails, OrganizationSupport, Actor {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "USERNAME_", length = 64)
	@Schema(description = "用户名")
	private String username;

	@Column(name = "NICKNAME_", length = 64)
	@Schema(description = "昵称")
	private String nickname;

	@Column(name = "AVATAR_", length = 64)
	@Schema(description = "头像")
	private String avatar;

	@Column(name = "PASSWORD_", length = 125)
	@JsonIgnore
	@Schema(description = "密码")
	private String password;

	@Column(name = "EMAIL_", length = 125)
	@Schema(description = "邮箱")
	private String email;

	@Column(name = "ADMINISTRATOR_")
	@Schema(description = "是否管理员")
	private boolean administrator = false;

	@Column(name = "ACCOUNT_NON_EXPIRED_")
	@Schema(description = "是否有效")
	private boolean accountNonExpired = true;

	@Column(name = "ACCOUNT_NON_LOCKED_")
	@Schema(description = "可用")
	private boolean accountNonLocked = true;

	@Column(name = "CREDENTIALS_NON_EXPIRED_")
	@Schema(description = "凭证可用")
	private boolean credentialsNonExpired = true;

	@Column(name = "ENABLED_")
	private boolean enabled = true;

	@Column(name = "SALT_")
	@Schema(description = "盐值")
	private String salt;

	@Column(name = "INTRODUCTION_")
	@Schema(description = "简介")
	private String introduction;

	@Column(name = "PWD_UPDATE_TIME_")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Schema(description = "密码更新时间")
	private Date pwdUpdateTime;

	@Column(name = "USER_TYPE_")
	@Schema(description = "用户类型")
	private String userType;

	@Column(name = "PHONE_")
	@Schema(description = "联系方式")
	private String phone;

	@Column(name = "RANK_")
	@Schema(description = "级别")
	@com.mxpioframework.security.annotation.Dict(dicCode = "MB_SYSTEM_RANK")
	private String rank;

	@Column(name = "POST_ID_")
	@Schema(description = "职位")
	@com.mxpioframework.security.annotation.Dict(dicCode = "id", dicEntity = Post.class, dicText = "name")
	private String postId;

	@Column(name = "CONCURRENT_POST_IDS_")
	@Schema(description = "兼任")
	@com.mxpioframework.security.annotation.Dict(dicCode = "id", dicEntity = Post.class, dicText = "name")
	private String concurrentPostIds;

	@Column(name = "ID_NUMBER_")
	@Schema(description = "身份证号")
	private String idNumber;


	@Column(name = "THIRD_ID_")
	@Schema(description = "三方平台用户ID")
	private String thirdId;
	@Transient
	@Schema(description = "密码是否过期")
	private boolean pwdExpiredFlag = false;

	@Transient
	@Schema(description = "租户")
	private Object organization;

	@Transient
	@Schema(description = "用户部门")
	private Dept dept;

	@Transient
	@Schema(description = "个性化配置")
	private Collection<UserProfile> profiles;

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

	public Date getPwdUpdateTime() {
		return pwdUpdateTime;
	}

	public void setPwdUpdateTime(Date pwdUpdateTime) {
		this.pwdUpdateTime = pwdUpdateTime;
	}

	public boolean isPwdExpiredFlag() {
		return pwdExpiredFlag;
	}

	public void setPwdExpiredFlag(boolean pwdExpiredFlag) {
		this.pwdExpiredFlag = pwdExpiredFlag;
	}

	public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getThirdId() {
		return thirdId;
	}

	public void setThirdId(String thirdId) {
		this.thirdId = thirdId;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getConcurrentPostIds() {
		return concurrentPostIds;
	}

	public void setConcurrentPostIds(String concurrentPostIds) {
		this.concurrentPostIds = concurrentPostIds;
	}

	public Collection<UserProfile> getProfiles() {
		return profiles;
	}

	public void setProfiles(Collection<UserProfile> profiles) {
		this.profiles = profiles;
	}
}
