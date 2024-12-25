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
import com.mxpioframework.security.entity.Dept;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.entity.UserDept;
import com.mxpioframework.security.service.DeptService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "DeptController", description = "部门管理")
@RestController("mxpio.security.deptController")
@RequestMapping("/dept/")
public class DeptController {

	@Autowired
	private DeptService deptService;

	@GetMapping("tree")
	@Operation(summary = "部门列表", description = "获取部门列表", method = "GET")
	public Result<List<Dept>> tree(Criteria criteria) throws Exception {
		return Result.OK(deptService.getDeptTree(criteria));
	}

	@GetMapping("role/without/{roleId}")
	@Operation(summary = "未绑定部门", description = "分页获取未绑定角色ID的部门", method = "GET")
	public Result<Page<Dept>> without(Criteria criteria,
			@PathVariable(value = "roleId") String roleId,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws Exception {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		return Result.OK(deptService.loadDeptsWithout(pageAble, criteria, roleId));
	}

	@GetMapping("role/within/{roleId}")
	@Operation(summary = "绑定部门", description = "分页获取绑定角色ID的部门", method = "GET")
	public Result<Page<Dept>> within(Criteria criteria,
			@PathVariable(value = "roleId") String roleId,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws Exception {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		return Result.OK(deptService.loadDeptsWithin(pageAble, criteria, roleId));
	}

	@GetMapping("user/without/{deptId}")
	@Operation(summary = "未关联用户", description = "分页获取未关联部门ID的用户", method = "GET")
	public Result<Page<User>> userWithout(Criteria criteria,
			@PathVariable(value = "deptId") String deptId,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws Exception {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		return Result.OK(deptService.loadUsersWithout(pageAble, criteria, deptId));
	}

	@GetMapping("user/within/{deptId}")
	@Operation(summary = "关联用户", description = "分页获取关联部门ID的用户", method = "GET")
	public Result<Page<User>> userWithin(Criteria criteria,
			@PathVariable(value = "deptId") String deptId,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws Exception {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		return Result.OK(deptService.loadUsersWithin(pageAble, criteria, deptId));
	}

	@PostMapping("user/add")
	@Operation(summary = "关联用户", description = "关联用户", method = "POST")
	public Result<List<UserDept>> userAdd(@RequestBody List<UserDept> userDepts) throws Exception {
		deptService.saveUserDepts(userDepts);
		return Result.OK("关联用户成功",userDepts);
	}

	@DeleteMapping("user/delete/{deptId}/{userIds}")
	@Operation(summary = "删除关联用户", description = "删除关联用户", method = "DELETE")
	public Result<List<UserDept>> userDelete(@PathVariable(name = "deptId", required = true) String deptId,
			@PathVariable(name = "userIds", required = true) String userIds) throws Exception {
		deptService.deleteUserDepts(deptId,userIds);
		return Result.OK("删除关联用户成功", null);
	}

	@PostMapping("add")
	@Operation(summary = "添加部门", description = "添加部门信息", method = "POST")
	public Result<List<Dept>> add(@RequestBody List<Dept> depts) throws Exception {
		deptService.saveDepts(depts);
		return Result.OK("添加部门成功",depts);
	}

	@PutMapping("edit")
	@Operation(summary = "更新部门", description = "更新部门信息", method = "PUT")
	public Result<List<Dept>> edit(@RequestBody List<Dept> depts) throws Exception {
		deptService.updateDepts(depts);
		return Result.OK("更新部门成功",depts);
	}

	@DeleteMapping("remove/{deptIds}")
	@Operation(summary = "删除部门", description = "删除部门信息", method = "DELETE")
	public Result<List<Dept>> remove(@PathVariable(name = "deptIds", required = true) String deptIds) throws Exception {
		String[] deptId = deptIds.split(",");
		deptService.deleteDepts(deptId);
		return Result.OK("删除部门成功",null);
	}

	@GetMapping("user_dept_tree")
	@Operation(summary = "用户所属部门的部门树（部门及子部门，username为空时按当前用户查询", description = "用户所属部门的部门树（部门及子部门，username为空时按当前用户查询）", method = "GET")
	public Result<Dept> getUserDeptTree( @RequestParam(value="username", defaultValue = "") String username) {
		Dept dept = deptService.getUserDeptTree(username);
		if(dept==null){
			return Result.error("用户未配置所属部门");
		}
		return Result.OK(dept);
	}


	@GetMapping("getLevelOneDept/{deptCode}")
	@Operation(summary = "根据部门编码查询对应一级部门", description = "根据部门编码查询对应一级部门", method = "GET")
	public Result<Dept> getTopDept(@PathVariable(name = "deptCode", required = true) String deptCode) {
		Dept dept = deptService.getLevelOneDept(deptCode);
		if(dept==null){
			return Result.error("未匹配到对应一级部门");
		}
		return Result.OK(dept);
	}


}
