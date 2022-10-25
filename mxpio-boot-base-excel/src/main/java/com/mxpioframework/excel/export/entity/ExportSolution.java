package com.mxpioframework.excel.export.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.mxpioframework.jpa.annotation.Generator;
import com.mxpioframework.security.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Schema(description="Excel导出方案")
@Table(name = "MB_EXCEL_EXPORT_SOLUTION")
@EqualsAndHashCode(callSuper=false)
public class ExportSolution extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator
	@Column(name = "ID_", length = 64)
	@Schema(description = "ID")
	private String id;
	
	@Column(name = "CODE_", length = 64, unique = true)
	@Schema(description = "方案编码")
	private String code;

	@Column(name = "FILE_NAME_")
	@Schema(description = "文件名")
	private String fileName;
	
	@Column(name = "API_")
	@Schema(description = "接口")
	private String api;

	@Column(name = "INTERCEPTOR_NAME_")
	@Schema(description = "拦截器")
	private String interceptorName;

	@Column(name = "SHOW_PAGE_NO_")
	@Schema(description = "是否显示页码")
	private boolean showPageNo = true;

	@Column(name = "REPEAT_HEADER_")
	@Schema(description = "重复表头")
	private boolean repeatHeader = false;

	@Column(name = "SHOW_BORDER_")
	@Schema(description = "显示边框")
	private boolean showBorder = true;

	@Column(name = "SHOW_TITLE_")
	@Schema(description = "显示标题")
	private boolean showTitle = false;

	@Column(name = "TITLE_")
	@Schema(description = "标题")
	private String title;

	@Column(name = "BG_COLOR_")
	@Schema(description = "标题背景颜色（RGB）")
	private String bgColor;

	@Column(name = "FONT_COLOR_")
	@Schema(description = "标题颜色（RGB）")
	private String fontColor;

	@Column(name = "FONT_SIZE_")
	@Schema(description = "标题字体大小")
	private int fontSize;
	
	@Column(name = "DATA_BG_COLOR_")
	@Schema(description = "正文背景颜色（RGB）")
	private String dataBgColor;
	
	@Column(name = "DATA_FONT_COLOR_")
	@Schema(description = "正文颜色（RGB）")
	private String dataFontColor;
	
	@Column(name = "DATA_FONT_SIZE_")
	@Schema(description = "正文字体大小")
	private int dataFontSize;
	
	@Transient
	private List<ExportColumn> columns;
}
