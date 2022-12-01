package com.mxpioframework.security.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description="路由扩展信息")
public class RouterMetaVo {
	
	@Schema(description = "标题")
	private String title;
	
	@Schema(description = "目录/菜单")
	private String urlType;
	
	@Schema(description = "是否隐藏")
	private boolean isHidden;
	
	@Schema(description = "是否保持链接")
	private boolean keepAlive;
	
	@Schema(description = "是否外部")
	private boolean outside;
	
	@Schema(description = "图标")
	private String icon;
	
	@Schema(description = "排序")
	private Integer order;
	
	@Schema(description = "描述")
	private String desc;
	
}
