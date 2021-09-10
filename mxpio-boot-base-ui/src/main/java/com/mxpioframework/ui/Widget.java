package com.mxpioframework.ui;

import java.util.List;

public interface Widget {
	
	public String getId();
	
	public String getName();
	
	public List<Widget> getChildren();
}
