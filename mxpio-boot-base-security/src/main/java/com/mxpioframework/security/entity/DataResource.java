package com.mxpioframework.security.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Schema(description="数据对象")
@ToString
public class DataResource extends BaseEntity implements Resource {

	private static final long serialVersionUID = 1L;

	@Transient
	private ResourceType resourceType = ResourceType.DATA;
	
	@Id
	@Column(name = "ID_", length = 64)
	@Generator
	@Schema(description = "ID")
	private String id;
	
	@Column(name = "NAME_", length = 64)
	@Schema(description = "名称")
	private String name;
	
	@Column(name = "PATH_", length = 512)
	@Schema(description = "路径")
	private String path;
	
	@Column(name = "DATA_TYPE_", length = 255)
	@Schema(description = "数据资源类别")
	private String dataType;
	
	@Column(name = "ELEMENT_ID_", length = 255)
	@Schema(description = "组件标识")
	private String elementId;
	
	@Column(name = "DATA_SCOPE_", length = 512)
	@Schema(description = "权限范围")
	private String dataScope;
	
	@Column(name = "PARENT_RES_ID_", length = 64)
	@Schema(description = "所属资源的ID")
	private String parentResId;
	
	@Column(name = "DESCRIPTION_", length = 255)
	@Schema(description = "描述")
	private String description;
	
	@Transient
	private List<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();

}
