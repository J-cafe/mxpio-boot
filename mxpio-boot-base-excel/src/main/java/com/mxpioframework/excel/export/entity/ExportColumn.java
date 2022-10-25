package com.mxpioframework.excel.export.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mxpioframework.jpa.annotation.Generator;
import com.mxpioframework.security.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Schema(description="Excel导出方案")
@Table(name = "MB_EXCEL_EXPORT_COLUMN")
@EqualsAndHashCode(callSuper=false)
public class ExportColumn extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator
	@Column(name = "ID_", length = 64)
	@Schema(description = "ID")
	private String id;
	
	@Column(name = "SOLUTION_ID_", length = 64)
	@Schema(description = "方案ID")
	private String solutionId;
	
	@Column(name = "COLUMN_NAME_")
	@Schema(description = "字段名")
	private String columnName;
	
	@Column(name = "LEVEL_")
	@Schema(description = "级别")
	private Integer level;
	
	@Column(name = "LABEL_")
	@Schema(description = "标签")
	private String label;
	
	@Column(name = "WIDTH_")
	@Schema(description = "宽度")
	private Integer width;
	
	@Column(name = "BG_COLOR_")
	@Schema(description = "背景颜色（RGB）")
	private String bgColor;
	
	@Column(name = "FONT_COLOR_")
	@Schema(description = "字体颜色（RGB）")
	private String fontColor;
	
	@Column(name = "FONT_SIZE_")
	@Schema(description = "字体大小")
	private Integer fontSize;
	
	@Column(name = "ALIGN_")
	@Schema(description = "列头对齐方式")
	private Integer align;
	
	@Column(name = "DATA_ALIGN_")
	@Schema(description = "数据对齐方式")
	private Integer dataAlign;
}
