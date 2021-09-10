package com.mxpioframework.ui.widget;

import java.util.List;

import com.mxpioframework.ui.Widget;

import lombok.Data;

@Data
public class Button implements Widget {
	
	private String id;
	
	private String size;
	
	private String type;
	
	private Boolean plain;
	
	private Boolean round;

	private Boolean circle;
	
	private Boolean loading;
	
	private Boolean disabled;
	
	private String icon;
	
	private Boolean autofocus;
	
	private List<Widget> children;

	@Override
	public String getName() {
		return "el-button";
	}

}
