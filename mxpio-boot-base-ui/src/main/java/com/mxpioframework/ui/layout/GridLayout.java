package com.mxpioframework.ui.layout;

import java.util.List;

import com.mxpioframework.ui.VuePage;
import com.mxpioframework.ui.Widget;

import lombok.Data;

@Data
public class GridLayout implements VuePage {
	
	private String id;

	private String layout;
	
	private Object responsiveLayouts;
	
	private String colNum;
	
	private String rowHeight;
	
	private String maxRows;
	
	private String margin;
	
	private Boolean isDraggable;
	
	private Boolean isResizable;
	
	private Boolean isMirrored;
	
	private Boolean autoSize;
	
	private Boolean verticalCompact;
	
	private Boolean preventCollision;
	
	private Boolean useCssTransforms;
	
	private Boolean responsive;
	
	private Object breakpoints;
	
	private Object cols;
	
	private String useStyleCursor;
	
	private List<Widget> children;

	@Override
	public String getName() {
		return "grid-layout";
	}

	@Override
	public String getPage() {
		return null;
	}
}
