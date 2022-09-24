package com.mxpioframework.security.vo;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description="路由信息")
public class RouterVo {
	
	@Schema(description = "父路由")
	private String parentId;
	
	@Schema(description = "路由key")
	private String key;
	
	@Schema(description = "路由名称")
	private String name;
	
	@Schema(description = "路由路径")
	private String path;

	@Schema(description = "Vue组件")
	private String component;
	
	@Schema(description = "路由扩展信息")
	private RouterMetaVo meta;
	
	@Schema(description = "子路由")
	private List<RouterVo> children;
	
}
