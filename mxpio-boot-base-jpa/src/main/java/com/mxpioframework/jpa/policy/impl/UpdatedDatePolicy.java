package com.mxpioframework.jpa.policy.impl;



import java.util.Date;

public class UpdatedDatePolicy extends AbstractGeneratorPolicy {

	@Override
	public CrudType getType() {
		return CrudType.UPDATE;
	}

    @Override
    protected Object getValue(Object entity, String name) {
        return new Date();
    }

}
