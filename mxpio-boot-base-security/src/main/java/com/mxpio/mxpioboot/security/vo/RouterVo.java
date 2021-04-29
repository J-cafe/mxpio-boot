package com.mxpio.mxpioboot.security.vo;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RouterVo {
	
	private String name;
	
	private String path;

	private String component;
	
	private RouterMetaVo meta;
	
	private List<RouterVo> children;
	
}
