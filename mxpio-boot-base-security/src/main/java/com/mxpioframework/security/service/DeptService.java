package com.mxpioframework.security.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.entity.Dept;
import com.mxpioframework.security.entity.RoleDept;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.entity.UserDept;

public interface DeptService extends BaseService<Dept> {

	/**
	 * 获取部门树
	 * @return
	 */
	List<Dept> getDeptTree();
	
	/**
	 * 新增部门信息
	 * @param depts
	 */
	void saveDepts(List<Dept> depts);
	
	/**
	 * 新增部门信息
	 * @param dept
	 */
	void saveDept(Dept dept);
	
	/**
	 * 更新部门信息
	 * @param depts
	 */
	void updateDepts(List<Dept> depts);
	
	/**
	 * 更新部门信息
	 * @param dept
	 */
	void updateDept(Dept dept);
	
	/**
	 * 删除部门信息
	 * @param deptId
	 */
	void deleteDepts(String[] deptId);
	
	/**
	 * 分页获取未绑定角色ID的部门
	 * @param pageable
	 * @param criteria
	 * @param roleId
	 * @return
	 */
	Page<Dept> loadDeptsWithout(Pageable pageable, Criteria criteria, String roleId);
	
	/**
	 * 分页获取绑定角色ID的部门
	 * @param pageable
	 * @param criteria
	 * @param roleId
	 * @return
	 */
	Page<Dept> loadDeptsWithin(Pageable pageable, Criteria criteria, String roleId);
	
	/**
	 * 分页获取未关联部门ID的用户
	 * @param pageable
	 * @param criteria
	 * @param deptId
	 * @return
	 */
	Page<User> loadUsersWithout(Pageable pageable, Criteria criteria, String deptId);
	
	/**
	 * 分页获取关联部门ID的用户
	 * @param pageable
	 * @param criteria
	 * @param deptId
	 * @return
	 */
	Page<User> loadUsersWithin(Pageable pageable, Criteria criteria, String deptId);

	/**
	 * 添加关联用户
	 * @param userDepts
	 */
	void saveUserDepts(List<UserDept> userDepts);

	/**
	 * 删除关联用户
	 * @param deptId
	 * @param userIds
	 * @return
	 */
	int deleteUserDepts(String deptId, String userIds);

	/**
	 * 添加关联角色
	 * @param userDepts
	 */
	void saveRoleDepts(List<RoleDept> userDepts);

	/**
	 * 删除关联角色
	 * @param roleId
	 * @param deptIds
	 * @return
	 */
	int deleteRoleDepts(String roleId, String deptIds);
}
