package com.mxpioframework.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.jpa.query.CriteriaUtils;
import com.mxpioframework.security.entity.Dept;
import com.mxpioframework.security.entity.RoleGrantedAuthority;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.entity.UserDept;
import com.mxpioframework.security.service.DeptService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "DeptController", tags = {"部门管理"})
@RestController
@RequestMapping("/dept/")
public class DeptController {
	
	@Autowired
	private DeptService deptService;
	
	@GetMapping("/tree")
	@ApiOperation(value = "部门列表", notes = "获取部门列表", httpMethod = "GET")
	public Result<List<Dept>> tree() throws Exception {
		
		return Result.OK(deptService.getDeptTree());
	}
	
	@GetMapping("/role/without/{roleId}")
	@ApiOperation(value = "未绑定部门", notes = "分页获取未绑定角色ID的部门", httpMethod = "GET")
	public Result<Page<Dept>> without(@RequestParam("criteria") String criteria,
			@PathVariable(value = "roleId") String roleId,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws Exception {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Criteria c = CriteriaUtils.json2Criteria(criteria);
		return Result.OK(deptService.loadDeptsWithout(pageAble, c, roleId));
	}
	
	@GetMapping("/role/within/{roleId}")
	@ApiOperation(value = "绑定部门", notes = "分页获取绑定角色ID的部门", httpMethod = "GET")
	public Result<Page<Dept>> within(@RequestParam("criteria") String criteria,
			@PathVariable(value = "roleId") String roleId,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws Exception {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Criteria c = CriteriaUtils.json2Criteria(criteria);
		return Result.OK(deptService.loadDeptsWithin(pageAble, c, roleId));
	}
	
	@PostMapping("/role/add")
	@ApiOperation(value = "关联角色", notes = "关联角色", httpMethod = "POST")
	public Result<List<RoleGrantedAuthority>> roleAdd(@RequestBody List<RoleGrantedAuthority> roleDepts) throws Exception {
		deptService.saveRoleDepts(roleDepts);
		return Result.OK("关联角色成功",roleDepts);
	}
	
	@DeleteMapping("/role/delete/{roleId}/{deptIds}")
	@ApiOperation(value = "删除关联角色", notes = "删除关联角色", httpMethod = "DELETE")
	public Result<List<RoleGrantedAuthority>> roleDelete(@PathVariable(name = "roleId", required = true) String roleId,
			@PathVariable(name = "deptIds", required = true) String deptIds) throws Exception {
		deptService.deleteRoleDepts(roleId,deptIds);
		return Result.OK("删除关联用户成功", null);
	}
	
	@GetMapping("/user/without/{deptId}")
	@ApiOperation(value = "未关联用户", notes = "分页获取未关联部门ID的用户", httpMethod = "GET")
	public Result<Page<User>> userWithout(@RequestParam("criteria") String criteria,
			@PathVariable(value = "deptId") String deptId,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws Exception {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Criteria c = CriteriaUtils.json2Criteria(criteria);
		return Result.OK(deptService.loadUsersWithout(pageAble, c, deptId));
	}
	
	@GetMapping("/user/within/{deptId}")
	@ApiOperation(value = "关联用户", notes = "分页获取关联部门ID的用户", httpMethod = "GET")
	public Result<Page<User>> userWithin(@RequestParam("criteria") String criteria,
			@PathVariable(value = "deptId") String deptId,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws Exception {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Criteria c = CriteriaUtils.json2Criteria(criteria);
		return Result.OK(deptService.loadUsersWithin(pageAble, c, deptId));
	}
	
	@PostMapping("/user/add")
	@ApiOperation(value = "关联用户", notes = "关联用户", httpMethod = "POST")
	public Result<List<UserDept>> userAdd(@RequestBody List<UserDept> userDepts) throws Exception {
		deptService.saveUserDepts(userDepts);
		return Result.OK("关联用户成功",userDepts);
	}
	
	@DeleteMapping("/user/delete/{deptId}/{userIds}")
	@ApiOperation(value = "删除关联用户", notes = "删除关联用户", httpMethod = "DELETE")
	public Result<List<UserDept>> userDelete(@PathVariable(name = "deptId", required = true) String deptId,
			@PathVariable(name = "userIds", required = true) String userIds) throws Exception {
		deptService.deleteUserDepts(deptId,userIds);
		return Result.OK("删除关联用户成功", null);
	}
	
	@PostMapping("/add")
	@ApiOperation(value = "添加部门", notes = "添加部门信息", httpMethod = "POST")
	public Result<List<Dept>> add(@RequestBody List<Dept> depts) throws Exception {
		deptService.saveDepts(depts);;
		return Result.OK("添加部门成功",depts);
	}
	
	@PutMapping("/edit")
	@ApiOperation(value = "更新部门", notes = "更新部门信息", httpMethod = "PUT")
	public Result<List<Dept>> edit(@RequestBody List<Dept> depts) throws Exception {
		deptService.updateDepts(depts);;
		return Result.OK("更新部门成功",depts);
	}
	
	@DeleteMapping("/remove/{deptIds}")
	@ApiOperation(value = "删除部门", notes = "删除部门信息", httpMethod = "DELETE")
	public Result<List<Dept>> remove(@PathVariable(name = "deptIds", required = true) String deptIds) throws Exception {
		String[] deptId = deptIds.split(",");
		deptService.deleteDepts(deptId);
		return Result.OK("删除部门成功",null);
	}
	

}
