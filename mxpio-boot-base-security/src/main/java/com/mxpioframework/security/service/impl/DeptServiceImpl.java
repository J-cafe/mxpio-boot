package com.mxpioframework.security.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.policy.CrudContext;
import com.mxpioframework.jpa.policy.impl.SmartCrudPolicyAdapter;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.jpa.query.Order;
import com.mxpioframework.security.cache.SecurityCacheEvict;
import com.mxpioframework.security.entity.Dept;
import com.mxpioframework.security.entity.RoleGrantedAuthority;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.entity.UserDept;
import com.mxpioframework.security.service.DeptService;

@Service
@Transactional(readOnly = true)
public class DeptServiceImpl extends BaseServiceImpl<Dept> implements DeptService {

	@Override
	public List<Dept> getDeptTree() {
		List<Dept> result = new ArrayList<Dept>();
		Map<String, List<Dept>> childrenMap = new HashMap<String, List<Dept>>();
		List<Dept> depts = JpaUtil.linq(Dept.class).list();
		for (Dept dept : depts) {

			if (childrenMap.containsKey(dept.getId())) {
				dept.setChildren(childrenMap.get(dept.getId()));
			} else {
				dept.setChildren(new ArrayList<Dept>());
				childrenMap.put(dept.getId(), dept.getChildren());
			}

			if (dept.getFaDeptId() == null) {
				result.add(dept);
			} else {
				List<Dept> children;
				if (childrenMap.containsKey(dept.getFaDeptId())) {
					children = childrenMap.get(dept.getFaDeptId());
				} else {
					children = new ArrayList<Dept>();
					childrenMap.put(dept.getFaDeptId(), children);
				}
				children.add(dept);
			}
		}
		return result;
	}

	@Override
	@SecurityCacheEvict
	@Transactional(readOnly = false)
	public void saveDepts(List<Dept> depts) {
		JpaUtil.save(depts, new SmartCrudPolicyAdapter() {

			@Override
			public boolean beforeDelete(CrudContext context) {
				Dept dept = context.getEntity();
				JpaUtil.lind(UserDept.class).equal("deptId", dept.getId()).delete();
				return true;
			}

			@Override
			public void apply(CrudContext context) {
				Dept dept = context.getEntity();
				if (dept.getFaDeptId() == null) {
					Dept parent = context.getParent();
					if (parent != null) {
						dept.setFaDeptId(parent.getId());
					}
				}
				super.apply(context);
			}
		});
	}

	@Override
	@Transactional(readOnly = true)
	public void loadDeptsWithout(Pageable pageable, Criteria criteria, String roleId) {
		List<Order> orders = new ArrayList<Order>();
		if(criteria != null){
			orders = criteria.getOrders();
		}
		JpaUtil
			.linq(Dept.class)
			.where(criteria)
			.notExists(RoleGrantedAuthority.class)
				.equalProperty("actorId", "id")
				.equal("roleId", roleId)
			.end()
			.addIfNot(orders)
				.asc("deptCode")
			.endIf()
			.paging(pageable);
	}
	
	@Override
	@Transactional(readOnly = true)
	public void loadDeptsWithin(Pageable pageable, Criteria criteria, String roleId) {
		List<Order> orders = new ArrayList<Order>();
		if(criteria != null){
			orders = criteria.getOrders();
		}
		JpaUtil
			.linq(Dept.class)
			.where(criteria)
			.exists(RoleGrantedAuthority.class)
				.equalProperty("actorId", "id")
				.equal("roleId", roleId)
			.end()
			.addIfNot(orders)
				.asc("deptCode")
			.endIf()
			.paging(pageable);
	}
	
	@Override
	@Transactional(readOnly = true)
	public void loadUsersWithout(Pageable pageable, Criteria criteria, String deptId) {
		List<Order> orders = new ArrayList<Order>();
		if(criteria != null){
			orders = criteria.getOrders();
		}
		JpaUtil
			.linq(User.class)
			.where(criteria)
			.notExists(UserDept.class)
				.equalProperty("userId","username")
				//.equal("deptId", deptId)
			.end()
			.addIfNot(orders)
				.asc("username")
			.endIf()
			.paging(pageable);
	}
	
	@Override
	@Transactional(readOnly = true)
	public void loadUsersWithin(Pageable pageable, Criteria criteria, String deptId) {
		List<Order> orders = new ArrayList<Order>();
		if(criteria != null){
			orders = criteria.getOrders();
		}
		JpaUtil
			.linq(User.class)
			.where(criteria)
			.exists(UserDept.class)
				.equalProperty("userId","username")
				.equal("deptId", deptId)
			.end()
			.addIfNot(orders)
				.asc("username")
			.endIf()
			.paging(pageable);
	}

}
