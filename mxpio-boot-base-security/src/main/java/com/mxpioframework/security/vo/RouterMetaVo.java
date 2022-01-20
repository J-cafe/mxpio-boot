package com.mxpioframework.security.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="路由扩展信息")
public class RouterMetaVo {
	
	@ApiModelProperty(value = "标题")
	private String title;
	
	@ApiModelProperty(value = "是否隐藏")
	private boolean isHidden;
	
	@ApiModelProperty(value = "是否保持链接")
	private boolean keepAlive;
	
	@ApiModelProperty(value = "图标")
	private String icon;
	
	@ApiModelProperty(value = "排序")
	private Integer order;
	
	@ApiModelProperty(value = "描述")
	private String desc;
	
}
