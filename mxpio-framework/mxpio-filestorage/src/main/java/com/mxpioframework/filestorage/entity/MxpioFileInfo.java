package com.mxpioframework.filestorage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.mxpioframework.jpa.annotation.Generator;
import com.mxpioframework.security.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_FILE_INFO")
@Schema(description="文件信息")
public class MxpioFileInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Generator
	@Column(name = "ID_")
	@Schema(description = "ID")
	private String id;

	@Column(name = "FILE_NO_", unique = true)
	@Schema(description = "文件编号")
	private String fileNo;

	@Column(name = "FILE_NAME_")
	@Schema(description = "文件名称")
	private String fileName;

	@Column(name = "FILE_STORAGE_TYPE_")
	@Schema(description = "存储类型")
	private String fileStorageType;

	@Column(name = "RELATIVE_PATH_")
	@Schema(description = "相对路径")
	private String relativePath;

	@Transient
	@Schema(description = "绝对路径")
	private String absolutePath;

	@Transient
	@Schema(description = "文件大小")
	private long length;

}
