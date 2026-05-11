package com.mxpioframework.multitenant.manager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.common.util.BeanReflectionUtils;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.policy.CrudContext;
import com.mxpioframework.jpa.policy.impl.SmartCrudPolicyAdapter;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.multitenant.MultitenantUtils;
import com.mxpioframework.multitenant.domain.DataSourceInfo;
import com.mxpioframework.multitenant.domain.Organization;
import com.mxpioframework.multitenant.manager.service.OrganizationService;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.service.UserService;

@Service("manager.organizationService")
@Transactional(readOnly = true)
public class OrganizationServiceImpl implements OrganizationService {

	@Autowired
	private com.mxpioframework.multitenant.service.OrganizationService organizationService;
	
	@Autowired
	private UserService userService;
	
	@Override
	public Page<Organization> load(Pageable page, Criteria criteria) {
		return JpaUtil.linq(Organization.class)
			.collect(DataSourceInfo.class, "dataSourceInfoId")
			.collectSelect(DataSourceInfo.class, "id", "name")
			.where(criteria)
			.paging(page);
	}

	@Override
	@Transactional
	public void save(List<Organization> organizations) {
		JpaUtil.save(organizations, new SmartCrudPolicyAdapter() {
			
			@Override
			public boolean beforeInsert(CrudContext context) {
				Organization organization = context.getEntity();
				organizationService.allocteResource(organization);
				MultitenantUtils.doNonQuery(organization.getId(), () -> {
					User user = new User();
					user.setNickname("系统管理员");
					user.setUsername(BeanReflectionUtils.getPropertyValue(organization, "username"));
					user.setPassword("123456");
					user.setAdministrator(true);
					user.setAccountNonExpired(true);
					user.setAccountNonLocked(true);
					user.setCredentialsNonExpired(true);
					user.setCredentialsNonExpired(true);
					user.setEnabled(true);
					userService.create(user);
				});
				return true;
			}

			@Override
			public void afterDelete(CrudContext context) {
				Organization organization = context.getEntity();
				organizationService.releaseResource(organization);
			}

		});

	}

	@Override
	public boolean isExist(String organizationId) {
		return JpaUtil.linq(Organization.class).equal("id", organizationId).exists();
		
	}

}
