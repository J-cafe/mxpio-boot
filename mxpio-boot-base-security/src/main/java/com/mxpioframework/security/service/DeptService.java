package com.mxpioframework.security.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.entity.Dept;

public interface DeptService extends BaseService<Dept> {

	List<Dept> getDeptTree();
	
	void saveDepts(List<Dept> depts);
	
	void loadDeptsWithout(Pageable pageable, Criteria criteria, String roleId);
	
	void loadDeptsWithin(Pageable pageable, Criteria criteria, String roleId);
	
	void loadUsersWithout(Pageable pageable, Criteria criteria, String deptId);
	
	void loadUsersWithin(Pageable pageable, Criteria criteria, String deptId);
}
