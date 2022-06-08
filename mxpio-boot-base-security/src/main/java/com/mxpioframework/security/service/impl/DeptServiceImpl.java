package com.mxpioframework.security.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
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
import com.mxpioframework.security.entity.RoleDept;
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
	@SecurityCacheEvict
	@Transactional(readOnly = false)
	public void saveDept(Dept dept) {
		JpaUtil.save(dept, new SmartCrudPolicyAdapter() {
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
	@SecurityCacheEvict
	@Transactional(readOnly = false)
	public void updateDepts(List<Dept> depts) {
		JpaUtil.update(depts, new SmartCrudPolicyAdapter() {
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
	@SecurityCacheEvict
	@Transactional(readOnly = false)
	public void updateDept(Dept dept) {
		JpaUtil.update(dept, new SmartCrudPolicyAdapter() {
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
	@SecurityCacheEvict
	@Transactional(readOnly = false)
	public void deleteDepts(String[] deptIds) {
		for(String deptId : deptIds){
			Dept dept = JpaUtil.getOne(Dept.class, deptId);
			JpaUtil.delete(dept, new SmartCrudPolicyAdapter() {
				@Override
				public boolean beforeDelete(CrudContext context) {
					Dept dept = context.getEntity();
					JpaUtil.lind(UserDept.class).equal("deptId", dept.getId()).delete();
					return true;
				}
			});
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Dept> loadDeptsWithout(Pageable pageable, Criteria criteria, String roleId) {
		List<Order> orders = new ArrayList<Order>();
		if(criteria != null){
			orders = criteria.getOrders();
		}
		return JpaUtil
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
	public Page<Dept> loadDeptsWithin(Pageable pageable, Criteria criteria, String roleId) {
		List<Order> orders = new ArrayList<Order>();
		if(criteria != null){
			orders = criteria.getOrders();
		}
		return JpaUtil
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
	public Page<User> loadUsersWithout(Pageable pageable, Criteria criteria, String deptId) {
		List<Order> orders = new ArrayList<Order>();
		if(criteria != null){
			orders = criteria.getOrders();
		}
		return JpaUtil
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
	public Page<User> loadUsersWithin(Pageable pageable, Criteria criteria, String deptId) {
		List<Order> orders = new ArrayList<Order>();
		if(criteria != null){
			orders = criteria.getOrders();
		}
		return JpaUtil
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

	@Override
	@Transactional(readOnly = false)
	public void saveUserDepts(List<UserDept> userDepts) {
		JpaUtil.save(userDepts);
	}

	@Override
	@Transactional(readOnly = false)
	public int deleteUserDepts(String deptId, String userIds) {
		String[] userId = userIds.split(",");
		return JpaUtil.lind(UserDept.class).equal("deptId", deptId).in("userId", Arrays.asList(userId)).delete();
	}

	@Override
	@Transactional(readOnly = false)
	public void saveRoleDepts(List<RoleDept> userDepts) {
		JpaUtil.save(userDepts);
	}

	@Override
	@Transactional(readOnly = false)
	public int deleteRoleDepts(String roleId, String deptIds) {
		String[] deptId = deptIds.split(";");
		return JpaUtil.lind(RoleDept.class).equal("roleId", roleId).in("deptId", Arrays.asList(deptId)).delete();
	}

}
