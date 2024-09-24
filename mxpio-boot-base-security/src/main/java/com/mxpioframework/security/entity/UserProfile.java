package com.mxpioframework.security.entity;

import com.mxpioframework.jpa.annotation.Generator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@Table(name = "MB_USER_PROFILE")
@ToString
@Schema(description="个性化配置")
@EqualsAndHashCode(callSuper = true)
public class UserProfile extends BaseEntity{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator
	@Column(name = "ID_", length = 64)
	@Schema(description = "ID")
	private String id;
	
	@Column(name = "USER_ID_", length = 64)
	@Schema(description = "用户ID")
	private String userId;

	@Column(name = "PAGE_KEY_", length = 64)
	@Schema(description = "页面Key")
	private String pageKey;

	@Column(name = "ELEMENT_KEY_", length = 64)
	@Schema(description = "组件Key")
	private String elementKey;

	@Lob
	@Column(name = "PROPERTIES_")
	@Schema(description = "属性")
	private String properties;

}
