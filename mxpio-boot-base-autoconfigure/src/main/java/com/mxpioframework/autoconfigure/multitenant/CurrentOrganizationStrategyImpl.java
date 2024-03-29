package com.mxpioframework.autoconfigure.multitenant;

import com.mxpioframework.multitenant.domain.Organization;
import com.mxpioframework.multitenant.strategy.CurrentOrganizationStrategy;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.util.SecurityUtils;

/**
 * @author MxpIO (mailto:i@mxpio.com)
 * @since 2021年12月14日
 */
public class CurrentOrganizationStrategyImpl implements CurrentOrganizationStrategy {

	@Override
	public Organization getCurrent() {
		User user = SecurityUtils.getLoginUser();
		if (user != null) {
			return user.getOrganization();
		}
		return null;
	}

}
