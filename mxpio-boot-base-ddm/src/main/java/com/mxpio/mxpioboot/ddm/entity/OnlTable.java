package com.mxpio.mxpioboot.ddm.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.mxpio.mxpioboot.jpa.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper=false)
@Table(name = "MB_ONL_TABLE")
public class OnlTable extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "TABLE_NAME_", length = 255)
	private String tableName;
	
	@Column(name = "TABLE_DESC_", length = 255)
	private String tableDesc;
	
	@Column(name = "TABLE_TYPE_", length = 64)
	@Enumerated(EnumType.STRING)
	private TableType tableType;
	
	@Transient
	private List<OnlColumn> onlColumns;

}
