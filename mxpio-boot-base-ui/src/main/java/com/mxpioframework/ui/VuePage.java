package com.mxpioframework.ui;

import java.util.List;

public interface VuePage {

	public String getId();
	
	public String getName();
	
	public List<Widget> getChildren();
	
	public String getPage();
}
