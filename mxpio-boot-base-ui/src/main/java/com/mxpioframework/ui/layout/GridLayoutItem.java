package com.mxpioframework.ui.layout;

import java.util.List;

import com.mxpioframework.ui.Widget;

import lombok.Data;

@Data
public class GridLayoutItem implements Widget {
	
	private String id;

	private String i;
	
	private Integer x;
	
	private Integer y;
	
	private Integer w;
	
	private Integer h;
	
	private Integer minW;
	
	private Integer minH;
	
	private Integer maxW;
	
	private Integer maxH;
	
	private Boolean isDraggable;
	
	private Boolean isResizable;
	
	private Boolean isStatic;
	
	private String dragIgnoreFrom;
	
	private String dragAllowFrom;
	
	private String resizeIgnoreFrom;
	
	private String preserveAspectRatio;
	
	private List<Widget> children;

	@Override
	public String getName() {
		return "grid-item";
	}
	
}
