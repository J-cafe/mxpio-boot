package com.mxpioframework.dbconsole.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mxpioframework.common.ds.DataSet;
import com.mxpioframework.security.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_DB_DATASET")
@Schema(description="数据集")
public class DbDataSet extends BaseEntity implements DataSet {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Schema(description = "编码")
	@Column(name = "CODE_")
	private String code;
	
	@Schema(description = "名称")
	@Column(name = "NAME_")
	private String name;
	
	@Schema(description = "数据库ID")
	@Column(name = "DB_ID_")
	private String dbId;
	
	@Schema(description = "SQL")
	@Column(name = "SQL_STR_")
	private String sqlStr;

	@Override
	public String getType() {
		return "DB";
	}

}
