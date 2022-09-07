package com.mxpioframework.multitenant.listener;

import com.mxpioframework.multitenant.domain.Organization;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder.Builder;


/**
 * @author Kevin Yang (mailto:muxiangqiu@gmail.com)
 * @since 2017年11月24日
 */
public interface EntityManagerFactoryCreateListener {

	void onCreate(Organization organization, Builder builder);
}
