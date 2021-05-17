package com.mxpio.mxpioboot.security.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouterVo {
	
	private String parentId;
	
	private String key;
	
	private String name;
	
	private String path;

	private String component;
	
	private RouterMetaVo meta;
	
	private List<RouterVo> children;
	
}
