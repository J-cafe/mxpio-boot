package com.mxpioframework.security.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mxpioframework.security.util.SecurityUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.policy.CrudContext;
import com.mxpioframework.jpa.policy.impl.CrudType;
import com.mxpioframework.jpa.policy.impl.SmartCrudPolicyAdapter;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.jpa.query.Order;
import com.mxpioframework.security.cache.SecurityCacheEvict;
import com.mxpioframework.security.common.Constants;
import com.mxpioframework.security.entity.Dept;
import com.mxpioframework.security.entity.RoleGrantedAuthority;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.entity.UserDept;
import com.mxpioframework.security.processor.Context;
import com.mxpioframework.security.processor.UserDeptProcessor;
import com.mxpioframework.security.service.DeptService;

@Service("mxpio.security.deptService")
@Transactional(readOnly = true)
public class DeptServiceImpl extends BaseServiceImpl<Dept> implements DeptService {
	
	@Autowired(required = false)
	private Collection<UserDeptProcessor> userDeptProcessors;

	@Override
	public List<Dept> getDeptTree(Criteria c) {
		List<Dept> result = new ArrayList<Dept>();
		Map<String, List<Dept>> childrenMap = new HashMap<String, List<Dept>>();
		List<Dept> depts = JpaUtil.linq(Dept.class).where(c).list();
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
	public Dept getDeptWithBranchByCode(String deptCode) {
		Map<String, List<Dept>> childrenMap = new HashMap<String, List<Dept>>();
		List<Dept> depts = JpaUtil.linq(Dept.class).list();
		Dept returnDept = null;
		for (Dept dept : depts) {
			if (childrenMap.containsKey(dept.getId())) {
				dept.setChildren(childrenMap.get(dept.getId()));
			} else {
				dept.setChildren(new ArrayList<Dept>());
				childrenMap.put(dept.getId(), dept.getChildren());
			}

			if (dept.getFaDeptId() != null) {
				List<Dept> children;
				if (childrenMap.containsKey(dept.getFaDeptId())) {
					children = childrenMap.get(dept.getFaDeptId());
				} else {
					children = new ArrayList<Dept>();
					childrenMap.put(dept.getFaDeptId(), children);
				}
				children.add(dept);
			}
			if (dept.getDeptCode().equals(deptCode)){
				returnDept = dept;
			}
		}
		return returnDept;
	}
	@Override
	@Transactional(readOnly = true)
	public Dept getUserDeptTree(String username){//查询用户所属部门的部门树
		if (StringUtils.isBlank(username)){
			username = SecurityUtils.getLoginUsername();//支持传参和当前用户
		}
		Map<String, Set<String>> allDeptCodeGroupByUser = getAllDeptCodeGroupByUser();
		Set<String> deptCodeSet = allDeptCodeGroupByUser.get(username);
		if (deptCodeSet.isEmpty()){
			return null;
		}
		String deptCode = deptCodeSet.iterator().next();//用户只能有一个部门
		return getDeptWithBranchByCode(deptCode);
	}
	@Override
	public List<String> getDeptCodeWithSubByCode(String deptCode) {//查询当前部门及子部门所有的编码
		Dept deptWithBranchByCode = this.getDeptWithBranchByCode(deptCode);
		List<String> deptCodes = new ArrayList<>();
		if (deptWithBranchByCode!=null){
			getDeptCode(deptWithBranchByCode,deptCodes);
		}
		return deptCodes;
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

	@SecurityCacheEvict
	@Override
	@Transactional(readOnly = false)
	public void saveUserDepts(List<UserDept> userDepts) {
		boolean process = CollectionUtils.isNotEmpty(userDeptProcessors);
		Context<UserDept> context = new Context<>(userDepts, CrudType.SAVE_OR_UPDATE);
		if(process){
			for(UserDeptProcessor processor : userDeptProcessors){
				processor.preProcess(context);
			}
		}
		JpaUtil.save(userDepts);
		if(process){
			for(UserDeptProcessor processor : userDeptProcessors){
				processor.postProcess(context);
			}
		}
	}

	@SecurityCacheEvict
	@Override
	@Transactional(readOnly = false)
	public int deleteUserDepts(String deptId, String userIds) {
		String[] userId = userIds.split(",");
		List<UserDept> userDepts = JpaUtil.linq(UserDept.class).equal("deptId", deptId).in("userId",(Object[]) userId).list();
		boolean process = CollectionUtils.isNotEmpty(userDeptProcessors);
		Context<UserDept> context = new Context<>(userDepts, CrudType.DELETE);
		if(process){
			for(UserDeptProcessor processor : userDeptProcessors){
				processor.preProcess(context);
			}
		}
		JpaUtil.delete(userDepts);
		
		if(process){
			for(UserDeptProcessor processor : userDeptProcessors){
				processor.postProcess(context);
			}
		}
		return userDepts.size();
	}

	@SecurityCacheEvict
	@Override
	@Transactional(readOnly = false)
	public void saveRoleDepts(List<RoleGrantedAuthority> roleDepts) {
		JpaUtil.save(roleDepts);
	}

	@SecurityCacheEvict
	@Override
	@Transactional(readOnly = false)
	public int deleteRoleDepts(String roleId, String deptIds) {
		String[] deptId = deptIds.split(",");
		return JpaUtil.lind(RoleGrantedAuthority.class).equal("roleId", roleId).in("actorId",(Object[]) deptId).delete();
	}

	@Override
	@Transactional(readOnly = true)
	public Set<String> getDeptKeysByUser(String username, String key) {
		if("code".equals(key)){
			Set<String> deptCodes = getAllDeptCodeGroupByUser().get(username);
			return deptCodes==null?new HashSet<String>():deptCodes;
		}else if("id".equals(key)){
			Set<String> deptIds = getAllDeptIdGroupByUser().get(username);
			return deptIds==null?new HashSet<String>():deptIds;
		}else{
			return new HashSet<String>();
		}
		
	}
	
	@Override
	@Transactional(readOnly = true)
	@Cacheable(cacheNames = Constants.USER_DEPT_CODE_CACHE_KEY, keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
	public Map<String, Set<String>> getAllDeptCodeGroupByUser(){
		List<UserDept> userDepts = JpaUtil.linq(UserDept.class).list();
		Map<String, Set<String>> result = new HashMap<String, Set<String>>();
		userDepts.forEach(userDept -> {
			String username = userDept.getUserId();
			Dept dept = getDeptMap().get(userDept.getDeptId());
			if(dept == null){
				return;
			}
			String deptCode = getDeptMap().get(userDept.getDeptId()).getDeptCode();
			Set<String> deptCodes = result.get(username);
			if(CollectionUtils.isEmpty(deptCodes)){
				deptCodes = new HashSet<>();
			}
			deptCodes.add(deptCode);
			result.put(username, deptCodes);
		});
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(cacheNames = Constants.DEPT_CACHE_KEY, keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
	public Map<String, Dept> getDeptMap() {
		List<Dept> depts = JpaUtil.linq(Dept.class).list();
		return JpaUtil.index(depts);
	}


	@Override
	@Transactional(readOnly = true)
	@Cacheable(cacheNames = Constants.USER_DEPT_ID_CACHE_KEY, keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
	public Map<String, Set<String>> getAllDeptIdGroupByUser() {
		List<UserDept> userDepts = JpaUtil.linq(UserDept.class).list();
		Map<String, Set<String>> result = new HashMap<String, Set<String>>();
		userDepts.forEach(userDept -> {
			String username = userDept.getUserId();
			String deptId = userDept.getDeptId();
			Set<String> deptIds = result.get(username);
			if(CollectionUtils.isEmpty(deptIds)){
				deptIds = new HashSet<>();
			}
			deptIds.add(deptId);
			result.put(username, deptIds);
		});
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(cacheNames = Constants.USER_DEPT_ID_FA_CACHE_KEY, keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
	public Map<String, Set<String>> getAllDeptIdWithFatherGroupByUser() {
		List<UserDept> userDepts = JpaUtil.linq(UserDept.class).list();
		Map<String, Set<String>> result = new HashMap<String, Set<String>>();
		for (UserDept userDept : userDepts) {
			String username = userDept.getUserId();
			String deptId = userDept.getDeptId();
			Set<String> deptIds = result.get(username);
			if(CollectionUtils.isEmpty(deptIds)){
				deptIds = new HashSet<>();
			}
			Map<String, Dept> deptMap = getDeptMap();
			addFaDept(deptIds, deptId, deptMap);
			deptIds.add(deptId);
			result.put(username, deptIds);
		}
		return null;
	}

	private void addFaDept(Set<String> deptIds, String deptId, Map<String, Dept> deptMap) {
		String faDeptId = deptMap.get(deptId).getFaDeptId();
		if (StringUtils.isNotEmpty(faDeptId)){
			addFaDept(deptIds, faDeptId, deptMap);
			deptIds.add(faDeptId);
		}
	}

	private void getDeptCode(Dept dept,List<String> deptCodes){
		deptCodes.add(dept.getDeptCode());
		if (dept.getChildren()!=null&&dept.getChildren().size()>0){
			for (Dept per:dept.getChildren()){
				getDeptCode(per,deptCodes);
			}
		}
	}
}
