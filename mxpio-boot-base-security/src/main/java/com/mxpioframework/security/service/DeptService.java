package com.mxpioframework.security.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.entity.Dept;
import com.mxpioframework.security.entity.RoleGrantedAuthority;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.entity.UserDept;

public interface DeptService extends BaseService<Dept> {

	/**
	 * 获取部门树
	 * @param c 查询构造器
	 * @return 部门树
	 */
	List<Dept> getDeptTree(Criteria c);

	Dept getDeptWithBranchByCode(String deptCode);

    Dept getUserDeptTree(String username);

    List<String> getDeptCodeWithSubByCode(String deptCode);
	/**
	 * 新增部门信息
	 * @param depts 部门实体集合
	 */
	void saveDepts(List<Dept> depts);
	
	/**
	 * 新增部门信息
	 * @param dept 部门实体
	 */
	void saveDept(Dept dept);
	
	/**
	 * 更新部门信息
	 * @param depts 部门实体集合
	 */
	void updateDepts(List<Dept> depts);
	
	/**
	 * 更新部门信息
	 * @param dept 部门实体
	 */
	void updateDept(Dept dept);
	
	/**
	 * 删除部门信息
	 * @param deptId 部门ID
	 */
	void deleteDepts(String[] deptId);
	
	/**
	 * 分页获取未绑定角色ID的部门
	 * @param pageable 分页插件
	 * @param criteria 查询构造器
	 * @param roleId 角色ID
	 * @return 未绑定角色部门列表
	 */
	Page<Dept> loadDeptsWithout(Pageable pageable, Criteria criteria, String roleId);
	
	/**
	 * 分页获取绑定角色ID的部门
	 * @param pageable 分页插件
	 * @param criteria 查询构造器
	 * @param roleId 角色ID
	 * @return 角色对应部门列表
	 */
	Page<Dept> loadDeptsWithin(Pageable pageable, Criteria criteria, String roleId);
	
	/**
	 * 分页获取未关联部门ID的用户
	 * @param pageable 分页插件
	 * @param criteria 查询构造器
	 * @param deptId 部门ID
	 * @return 未关联的用户列表
	 */
	Page<User> loadUsersWithout(Pageable pageable, Criteria criteria, String deptId);
	
	/**
	 * 分页获取关联部门ID的用户
	 * @param pageable 分页插件
	 * @param criteria 查询构造器
	 * @param deptId 部门ID
	 * @return 关联用户列表
	 */
	Page<User> loadUsersWithin(Pageable pageable, Criteria criteria, String deptId);

	/**
	 * 添加关联用户
	 * @param userDepts 用户部门关系集合
	 */
	void saveUserDepts(List<UserDept> userDepts);

	/**
	 * 删除关联用户
	 * @param deptId 部门ID
	 * @param userIds 用户ID集合
	 * @return 执行数量统计
	 */
	int deleteUserDepts(String deptId, String userIds);

	/**
	 * 添加关联角色
	 * @param userDepts 用户部门关系集合
	 */
	void saveRoleDepts(List<RoleGrantedAuthority> userDepts);

	/**
	 * 删除关联角色
	 * @param roleId 角色
	 * @param deptIds 部门ID集合
	 * @return 删除计数
	 */
	int deleteRoleDepts(String roleId, String deptIds);

	/**
	 * 根据用户名获取部门Key（缓存）
	 * @param username 用户名
	 * @param key code|id
	 * @return 查询结果集合
	 */
	Set<String> getDeptKeysByUser(String username, String key);

	Dept getLevelOneDept(String deptCode);
}
