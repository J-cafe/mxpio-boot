package com.mxpioframework.security.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="路由信息")
public class RouterVo {
	
	@ApiModelProperty(value = "父路由")
	private String parentId;
	
	@ApiModelProperty(value = "路由key")
	private String key;
	
	@ApiModelProperty(value = "路由名称")
	private String name;
	
	@ApiModelProperty(value = "路由路径")
	private String path;

	@ApiModelProperty(value = "Vue组件")
	private String component;
	
	@ApiModelProperty(value = "路由扩展信息")
	private RouterMetaVo meta;
	
	@ApiModelProperty(value = "子路由")
	private List<RouterVo> children;
	
}
