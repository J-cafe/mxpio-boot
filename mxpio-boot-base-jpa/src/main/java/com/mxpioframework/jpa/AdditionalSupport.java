package com.mxpioframework.jpa;

import jakarta.persistence.Transient;

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
