package com.mxpioframework.security.service.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.policy.CrudContext;
import com.mxpioframework.jpa.policy.impl.SmartCrudPolicyAdapter;
import com.mxpioframework.security.cache.SecurityCacheEvict;
import com.mxpioframework.security.entity.Element;
import com.mxpioframework.security.entity.Permission;
import com.mxpioframework.security.entity.ResourceType;
import com.mxpioframework.security.service.PermissionService;

@Service("mxpio.security.permissionService")
@Transactional(readOnly = true)
public class PermissionServiceImpl implements PermissionService {

	@Override
	public Collection<Element> loadElements(String pageId) {
		
		return null;
	}
	
	
	@Override
	public List<Permission> loadPermissions(String roleId, String urlId) {
		return JpaUtil
				.linq(Permission.class)
				.collect(Element.class, "resourceId")
				.equal("roleId", roleId)
				.equal("resourceType", ResourceType.ELEMENT)
				.exists(Element.class)
					.equalProperty("id", "resourceId")
					.equal("parentId", urlId)
				.list();
	}
	
	@SecurityCacheEvict
	@Override
	@Transactional
	public void save(Permission permission) {
		if(permission.getAttribute() == null){
			permission.setAttribute("ROLE_"+permission.getRoleId());
		}
		JpaUtil.save(permission);
	}
	
	@SecurityCacheEvict
	@Override
	@Transactional
	public void save(List<Permission> permissions) {
		JpaUtil.save(permissions, new SmartCrudPolicyAdapter(){
			@Override
			public boolean beforeInsert(CrudContext context) {
				Permission permission = context.getEntity();
				if(permission.getAttribute() == null){
					permission.setAttribute("ROLE_"+permission.getRoleId());
				}
				return super.beforeInsert(context);
			}
			@Override
			public boolean beforeUpdate(CrudContext context) {
				Permission permission = context.getEntity();
				if(permission.getAttribute() == null){
					permission.setAttribute("ROLE_"+permission.getRoleId());
				}
				return super.beforeUpdate(context);
			}
			
		});
	}
	
	@SecurityCacheEvict
	@Override
	@Transactional
	public void delete(Permission permission) {
		JpaUtil.delete(permission);
	}

}
