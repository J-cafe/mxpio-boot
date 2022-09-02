package com.mxpioframework.filestorage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.mxpioframework.jpa.BaseEntity;
import com.mxpioframework.jpa.annotation.Generator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_FILE_INFO")
@ApiModel(value="文件信息")
public class MxpioFileInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator
	@Column(name = "ID_")
	@ApiModelProperty(value = "ID")
	private String id;
	
	@Column(name = "FILE_NO_", unique = true)
	@ApiModelProperty(value = "文件编号")
	private String fileNo;
	
	@Column(name = "FILE_NAME_")
	@ApiModelProperty(value = "文件名称")
	private String fileName;
	
	@Column(name = "FILE_STORAGE_TYPE_")
	@ApiModelProperty(value = "存储类型")
	private String fileStorageType;
	
	@Column(name = "RELATIVE_PATH_")
	@ApiModelProperty(value = "相对路径")
	private String relativePath;
	
	@Transient
	@ApiModelProperty(value = "绝对路径")
	private String absolutePath;

}
