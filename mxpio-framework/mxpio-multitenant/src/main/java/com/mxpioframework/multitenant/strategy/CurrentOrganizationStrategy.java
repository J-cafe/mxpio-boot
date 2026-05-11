package com.mxpioframework.multitenant.strategy;

import com.mxpioframework.multitenant.domain.Organization;

public interface CurrentOrganizationStrategy {
    Organization getCurrent();
}
