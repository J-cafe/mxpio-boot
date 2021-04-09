package com.mxpio.mxpioboot.jpa;

import javax.persistence.Transient;

public class AdditionalSupport {

	@Transient
	private Object additional;

	public Object getAdditional() {
		return additional;
	}

	public void setAdditional(Object additional) {
		this.additional = additional;
	}
	
	
}
