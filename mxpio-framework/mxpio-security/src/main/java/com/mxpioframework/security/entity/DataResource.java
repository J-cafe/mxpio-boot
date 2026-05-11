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
@Table(name = "MB_DATA_RESOURCE")
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

	@Column(name = "TITLE_", length = 64)
	@Schema(description = "名称")
	private String title;

	@Column(name = "PATH_", length = 512)
	@Schema(description = "路径")
	private String path;

	@Column(name = "HAS_CRITERIA_", nullable = false)
	@Schema(description = "数据资源类别")
	private boolean hasCriteria;

	@Column(name = "HAS_FILTER_", nullable = false)
	@Schema(description = "是否有过滤")
	private boolean hasFilter = false;

	@Column(name = "ELEMENT_ID_", length = 255)
	@Schema(description = "组件标识")
	private String elementId;

	@Column(name = "DATA_SCOPE_", length = 512)
	@Schema(description = "权限范围")
	private String dataScope;

	@Column(name = "PRE_PROCESS_")
	@Schema(description = "前置处理器")
	private String preProcess;

	@Column(name = "SERVICE_", length = 512)
	@Schema(description = "服务")
	private String service;

	@Column(name = "PARENT_ID_", length = 64)
	@Schema(description = "所属URL的ID")
	private String parentId;

	@Column(name = "DESCRIPTION_", length = 255)
	@Schema(description = "描述")
	private String description;

	@Column(name = "ORDER_")
	@Schema(description = "顺序")
	private Integer order;

	public String getKey(){
		return parentId+"_"+path;
	}

	@Transient
	private List<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();

	@Transient
	private List<DataFilter> dataFilters;

	@Transient
	private List<DataColumnFilter> dataColumnFilters;

}
