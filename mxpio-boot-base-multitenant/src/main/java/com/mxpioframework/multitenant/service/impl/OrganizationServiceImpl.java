package com.mxpioframework.multitenant.service.impl;

import java.util.List;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.multitenant.domain.Organization;
import com.mxpioframework.multitenant.resource.ResourceAllocator;
import com.mxpioframework.multitenant.resource.ResourceReleaser;
import com.mxpioframework.multitenant.service.OrganizationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("mxpio.multitenant.organizationService")
@Transactional(readOnly = true)
public class OrganizationServiceImpl implements OrganizationService {

	@Autowired
	private List<ResourceAllocator> allocators;
	
	@Autowired
	private List<ResourceReleaser> releasers;
	
	@Override
	public Organization get(String id) {
		return JpaUtil.getOne(Organization.class, id);
	}

	@Override
	@Transactional
	public void register(Organization organization) {
		for (ResourceAllocator allocator : allocators) {
			allocator.allocate(organization);
		}
		JpaUtil.persist(organization);
	}
	
	@Override
	@Transactional
	public void allocteResource(Organization organization) {
		for (ResourceAllocator allocator : allocators) {
			allocator.allocate(organization);
		}
		JpaUtil.merge(organization);
	}

	@Override
	@Transactional
	public void releaseResource(Organization organization) {
		for (ResourceReleaser releaser : releasers) {
			releaser.release(organization);
		}
		JpaUtil.remove(organization);
	}

}
