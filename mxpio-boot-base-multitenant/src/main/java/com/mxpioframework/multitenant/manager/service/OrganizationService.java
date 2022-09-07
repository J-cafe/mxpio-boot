package com.mxpioframework.multitenant.manager.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.multitenant.domain.Organization;

public interface OrganizationService {
	

	Page<Organization> load(Pageable page, Criteria criteria);
	
	void save(List<Organization> organizations);
	
	boolean isExist(String organizationId);
}
