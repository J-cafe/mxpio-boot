package com.mxpioframework.filestorage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import com.mxpioframework.jpa.annotation.Generator;
import com.mxpioframework.security.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_FILE_BLOB")
@Schema(description="Blob对象")
public class MxpioBlob extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Generator
	@Column(name = "ID_")
	@Schema(description = "ID")
	private String id;

	@Lob
	@Column(name = "DATA_")
	private byte[] data;

}
