package com.mxpio.mxpioboot.security.vo;

import lombok.Data;

@Data
public class RouterMetaVo {
	
	private String title;
	
	private boolean isHidden;
	
	private boolean keepAlive;
	
	private String icon;
}
