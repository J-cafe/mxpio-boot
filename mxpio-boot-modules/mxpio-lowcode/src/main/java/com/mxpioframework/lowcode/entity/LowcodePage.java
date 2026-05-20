package com.mxpioframework.lowcode.entity;

import com.mxpioframework.jpa.annotation.Generator;
import com.mxpioframework.security.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
@Table(name = "MB_LOWCODE_PAGE")
@Schema(description = "低代码页面配置")
public class LowcodePage extends BaseEntity {

	@Id
	@Generator
	@Column(name = "ID_", updatable = false)
	private String id;

	@Column(name = "PAGE_CODE_", length = 200, unique = true, nullable = false)
	@Schema(description = "页面编码（唯一，如 /sales/order）")
	private String pageCode;

	@Column(name = "PAGE_NAME_", length = 200)
	@Schema(description = "页面名称")
	private String pageName;

	@Column(name = "MODEL_CODE_", length = 100)
	@Schema(description = "关联的数据模型编码")
	private String modelCode;

	@Column(name = "PAGE_TYPE_", length = 30)
	@Schema(description = "页面类型：LIST/FORM/DETAIL/MASTER_DETAIL")
	private String pageType;

	@Lob
	@Column(name = "WIDGET_CONFIG_")
	@Schema(description = "控件配置JSON（widgets + layout）")
	private String widgetConfig;

	@Lob
	@Column(name = "DATA_SET_CONFIG_")
	@Schema(description = "数据集配置JSON")
	private String dataSetConfig;

	@Lob
	@Column(name = "BUTTON_CONFIG_")
	@Schema(description = "按钮配置JSON")
	private String buttonConfig;

	@Lob
	@Column(name = "EVENT_CONFIG_")
	@Schema(description = "事件配置JSON（字段联动、按钮动作）")
	private String eventConfig;

	@Version
	@Column(name = "VERSION_")
	@Schema(description = "乐观锁版本号")
	private Integer version;
}
