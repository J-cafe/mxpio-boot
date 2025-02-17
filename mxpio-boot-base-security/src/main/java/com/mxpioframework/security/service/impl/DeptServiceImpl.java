package com.mxpioframework.security.service.impl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.security.enums.SecurityEnums;
import com.mxpioframework.security.service.RbacCacheService;
import com.mxpioframework.security.util.SecurityUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private RbacCacheService rbacCacheService;

	@Override
	public List<Dept> getDeptTree(Criteria c) {
		List<Dept> result = new ArrayList<>();
		Map<String, List<Dept>> childrenMap = new HashMap<>();
		List<Dept> depts = JpaUtil.linq(Dept.class).where(c).list();
		for (Dept dept : depts) {

			if (childrenMap.containsKey(dept.getId())) {
				dept.setChildren(childrenMap.get(dept.getId()));
			} else {
				dept.setChildren(new ArrayList<>());
				childrenMap.put(dept.getId(), dept.getChildren());
			}

			if (dept.getFaDeptId() == null) {
				result.add(dept);
			} else {
				List<Dept> children;
				if (childrenMap.containsKey(dept.getFaDeptId())) {
					children = childrenMap.get(dept.getFaDeptId());
				} else {
					children = new ArrayList<>();
					childrenMap.put(dept.getFaDeptId(), children);
				}
				children.add(dept);
			}
		}
		return result;
	}

	@Override
	public Dept getDeptWithBranchByCode(String deptCode) {
		Map<String, List<Dept>> childrenMap = new HashMap<>();
		List<Dept> depts = JpaUtil.linq(Dept.class).list();
		Dept returnDept = null;
		for (Dept dept : depts) {
			if (childrenMap.containsKey(dept.getId())) {
				dept.setChildren(childrenMap.get(dept.getId()));
			} else {
				dept.setChildren(new ArrayList<>());
				childrenMap.put(dept.getId(), dept.getChildren());
			}

			if (dept.getFaDeptId() != null) {
				List<Dept> children;
				if (childrenMap.containsKey(dept.getFaDeptId())) {
					children = childrenMap.get(dept.getFaDeptId());
				} else {
					children = new ArrayList<>();
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
		Map<String, Set<String>> allDeptCodeGroupByUser = rbacCacheService.getAllDeptCodeGroupByUser();
		Set<String> deptCodeSet = allDeptCodeGroupByUser.get(username);
		if (CollectionUtils.isEmpty(deptCodeSet)){
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
	@Transactional
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
	@Transactional
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
	@Transactional
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
	@Transactional
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
	@Transactional
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
		List<Order> orders = new ArrayList<>();
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
		List<Order> orders = new ArrayList<>();
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
		List<Order> orders = new ArrayList<>();
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
		List<Order> orders = new ArrayList<>();
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
	@Transactional
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
	@Transactional
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
	@Transactional
	public void saveRoleDepts(List<RoleGrantedAuthority> roleDepts) {
		JpaUtil.save(roleDepts);
	}

	@SecurityCacheEvict
	@Override
	@Transactional
	public int deleteRoleDepts(String roleId, String deptIds) {
		String[] deptId = deptIds.split(",");
		return JpaUtil.lind(RoleGrantedAuthority.class).equal("roleId", roleId).in("actorId",(Object[]) deptId).delete();
	}

	@Override
	@Transactional(readOnly = true)
	public Set<String> getDeptKeysByUser(String username, String key) {
		if("code".equals(key)){
			Set<String> deptCodes = rbacCacheService.getAllDeptCodeGroupByUser().get(username);
			return deptCodes==null?new HashSet<>():deptCodes;
		}else if("id".equals(key)){
			Set<String> deptIds = rbacCacheService.getAllDeptIdGroupByUser().get(username);
			return deptIds==null?new HashSet<>():deptIds;
		}else{
			return new HashSet<>();
		}

	}


	@Override
	@Transactional(readOnly = true)
	public Dept getLevelOneDept(String deptCode) {
		return getOneLevel(deptCode);
	}

	private Dept getOneLevel(String deptCode) {
		Dept dept = JpaUtil.linq(Dept.class).equal("deptCode", deptCode).findOne();
		if (dept != null){
			if (dept.getDeptLevel().equals(SecurityEnums.DeptLevel.ONE.getCode())){
				return dept;
			}else if(StringUtils.isNotBlank(dept.getFaDeptId())){
				Dept parent = JpaUtil.linq(Dept.class).equal("id", dept.getFaDeptId()).findOne();
				return getOneLevel(parent.getDeptCode());
			}
		}
		return null;
	}


	private void getDeptCode(Dept dept, List<String> deptCodes){
		deptCodes.add(dept.getDeptCode());
		if (dept.getChildren()!=null&& !dept.getChildren().isEmpty()){
			for (Dept per:dept.getChildren()){
				getDeptCode(per,deptCodes);
			}
		}
	}

	@Override
	@Transactional
	public Result<Dept> disableById(String deptId) {
		Dept dept = this.getById(Dept.class, deptId);
		if (dept==null){
			return Result.error("部门不存在");
		}
		if (dept.getDisabled()!=null && dept.getDisabled()){
			return Result.error("部门已经为禁用状态");
		}
		//存在生效的子部门
		Dept deptWithBranchByCode = getDeptWithBranchByCode(dept.getDeptCode());
		boolean existsEnableSubDept = existsEnableSubDept(deptWithBranchByCode);
		if (existsEnableSubDept){
			return Result.error("存在生效的子部门，请先禁用子部门");
		}
		//部门下存在人员
		Long count = JpaUtil.linq(UserDept.class).equal("deptId", deptId).count();
		if (count>0){
			return Result.error("部门下存在人员，请先删除人员");
		}
		dept.setDisabled(Boolean.TRUE);
		JpaUtil.update(dept);
		return Result.OK(dept);
	}

	@Override
	@Transactional
	public Result<Dept> enableById(String deptId) {
		Dept dept = this.getById(Dept.class, deptId);
		if (dept==null){
			return Result.error("部门不存在");
		}
		if (dept.getDisabled()!=null && !dept.getDisabled()){
			return Result.error("部门已经为启用状态");
		}
		//存在未生效的父部门
		boolean existsDisableParentDept = existsDisableParentDept(dept);
		if (existsDisableParentDept){
			return Result.error("存在未启用的父部门，请先启用");
		}
		dept.setDisabled(Boolean.FALSE);
		JpaUtil.update(dept);
		return Result.OK(dept);
	}

	@Override
	@Transactional
	public List<Dept> getDeptTreeWithDisableFilter(Criteria criteria) {
		List<Dept> result = new ArrayList<>();
		Map<String, List<Dept>> childrenMap = new ConcurrentHashMap<>();
		List<Dept> depts = JpaUtil.linq(Dept.class).where(criteria).list();

		for (Dept dept : depts) {
			if (dept.getDisabled()!=null && dept.getDisabled()) {
				continue;
			}
			// Initialize children list for this department
			List<Dept> children = childrenMap.computeIfAbsent(dept.getId(), k -> new ArrayList<>());
			dept.setChildren(children);

			if (dept.getFaDeptId() == null) {
				result.add(dept);
			} else {
				// Ensure the parent department's children list is initialized
				List<Dept> parentChildren = childrenMap.computeIfAbsent(dept.getFaDeptId(), k -> new ArrayList<>());
				parentChildren.add(dept);
			}
		}

		return result;
	}

	private boolean existsEnableSubDept(Dept dept) {
		List<Dept> children = dept.getChildren();
		if (children == null) {
			return false;
		}

		for (Dept child : children) {
			if (!Boolean.TRUE.equals(child.getDisabled())) {
				return true;
			} else if (existsEnableSubDept(child)) {
				return true;
			}
		}
		return false;
	}


	private boolean existsDisableParentDept(Dept dept) {
		String faDeptId = dept.getFaDeptId();
		if (StringUtils.isNotBlank(faDeptId)) {
			Dept currentDept = dept;
			while (StringUtils.isNotBlank(currentDept.getFaDeptId())) {
				String parentId = currentDept.getFaDeptId();
				Dept parent = JpaUtil.linq(Dept.class).equal("id", parentId).findOne();
				if (parent == null) {
					// 处理找不到父部门的情况
					return false;
				}
				if (parent.getDisabled() != null && parent.getDisabled()) {
					return true;
				}
				currentDept = parent;
			}
		}
		return false;
	}
}
