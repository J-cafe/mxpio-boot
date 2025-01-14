package com.mxpioframework.security.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.security.entity.Dept;
import com.mxpioframework.security.entity.UserDept;
import com.mxpioframework.security.vo.UpatePassVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mxpioframework.cache.provider.CacheProvider;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.initiator.JpaUtilAble;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.service.OnlineUserService;
import com.mxpioframework.security.service.UserService;

@Service("mxpio.security.userService")
public class UserServiceImpl implements UserService, JpaUtilAble {
	
	@Autowired
	protected PasswordEncoder passwordEncoder;
	
	@Autowired
	private OnlineUserService onlineUserService;
	
	@Autowired
	private CacheProvider cacheProvider;
	
	@Override
	@Transactional
	public User create(User user) {
		user.setPassword(passwordEncoder.encode("123456"));
		JpaUtil.save(user);
		return user;
	}

	@Override
	@Transactional
	public void update(User user) throws Exception {
		User temp = JpaUtil.getOne(User.class, user.getUsername());
		user.setPassword(temp.getPassword());
		JpaUtil.update(user);
		onlineUserService.kickOutForUsername(user.getUsername(), cacheProvider);
	}

	@Override
	@Transactional
	public void delete(Set<String> usernames) {
		JpaUtil.lind(User.class).in("username", usernames).delete();
		for(String username : usernames){
			onlineUserService.kickOutForUsername(username, cacheProvider);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public User findByName(String username) {
		return JpaUtil.getOne(User.class, username);
	}

	@Override
	@Transactional
	public void updatePass(String username, String newPassword) {
		User u = JpaUtil.getOne(User.class, username);
		u.setPassword(passwordEncoder.encode(newPassword));
		u.setPwdUpdateTime(new Date());
	}

	@Override
	@Transactional
	public Map<String, String> updateAvatar(MultipartFile file) {
		return null;
	}

	@Override
	@Transactional
	public void updateEmail(String username, String email) {
		
	}

	@Override
	@Transactional(readOnly = true)
	public Page<User> queryAll(Criteria criteria, Pageable pageable) {
		return JpaUtil.linq(User.class).where(criteria).paging(pageable);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<User> queryAllWithDept(Criteria criteria, Pageable pageable) {
		Page<User> paging = JpaUtil.linq(User.class).where(criteria).paging(pageable);
		if (paging.getContent().isEmpty()){
			return paging;
		}
		List<User> userList = paging.getContent();
		List<String> username = userList.stream().map(User::getUsername).collect(Collectors.toList());
		List<UserDept> userDeptList = JpaUtil.linq(UserDept.class).collect("id",Dept.class,"deptId").in("userId", username).list();
		if (!userDeptList.isEmpty()){
			Map<String, UserDept> userDeptMap = JpaUtil.index(userDeptList, "userId");
			for (User user:userList){
				if (userDeptMap.containsKey(user.getUsername())){
					user.setDept(userDeptMap.get(user.getUsername()).getDept());
				}
			}
		}
		return paging;
	}
	@Transactional(readOnly = true)
	@Override
	public List<User> queryAllWithDept(Criteria criteria) {
		List<User> userList = JpaUtil.linq(User.class).where(criteria).list();
		if (userList.isEmpty()){
			return userList;
		}
		List<String> username = userList.stream().map(User::getUsername).collect(Collectors.toList());
		List<UserDept> userDeptList = JpaUtil.linq(UserDept.class).collect("id",Dept.class,"deptId").in("userId", username).list();
		if (!userDeptList.isEmpty()){
			Map<String, UserDept> userDeptMap = JpaUtil.index(userDeptList, "userId");
			for (User user:userList){
				if (userDeptMap.containsKey(user.getUsername())){
					user.setDept(userDeptMap.get(user.getUsername()).getDept());
				}
			}
		}
		return userList;
	}


	@Override
	@Transactional(readOnly = true)
	public List<User> queryAll(Criteria criteria) {
		return JpaUtil.linq(User.class).where(criteria).list();
	}
	
	@Override
	@Transactional(readOnly = true)
	public void download(List<User> queryAll, HttpServletResponse response) throws IOException {

	}

	@Override
	@Transactional
	public void updateCenter(User resources) {

	}

	@Override
	@Transactional(readOnly = true)
	public boolean isAdministrator() {
		return isAdministrator(null);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isAdministrator(String username) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof User) {
			if (((User) principal).getUsername().equals(username)) {
				return ((User) principal).isAdministrator();
			} 
			if (username == null) {
				return ((User) principal).isAdministrator();
			}
		}
		User user = JpaUtil.linq(User.class).idEqual(username).findOne();
		return user.isAdministrator();
	}

	@Override
	@Transactional
	public void afterPropertiesSet(ApplicationContext applicationContext) {
		
		/*List<User> users = JpaUtil.linq(User.class).aliasToBean().select("username", "nickname").list();
		for(User user : users){
			System.out.println(user.toString());
		}*/
		/*Linq linq = JpaUtil.linq(User.class);
		linq.aliasToBean().select("username", linq.criteriaBuilder().sum(linq.root().get("quantity")).alias("quantity")).groupBy("username").list();*/
		
		/*
		 * SimpleCriterion sc = new SimpleCriterion("resourceType", Operator.EQ,
		 * ResourceType.ELEMENT); SimpleCriterion sc1 = new SimpleCriterion("roleId",
		 * Operator.EQ, "111"); SimpleCriterion sc2 = new SimpleCriterion("id",
		 * Operator.EQ, "p111");
		 * 
		 * Criteria c =
		 * Criteria.create().addCriterion(sc).or().addCriterion(sc1).addCriterion(sc2);
		 */
	
		// String json = "{\"criterions\":[{\"fieldName\":\"resourceType\",\"operator\":\"EQ\",\"value\":\"ELEMENT\"},{\"criterions\":[{\"fieldName\":\"roleId\",\"operator\":\"EQ\",\"value\":\"111\"},{\"fieldName\":\"id\",\"operator\":\"EQ\",\"value\":\"p111\"}],\"type\":\"OR\"}],\"orders\":[{\"desc\":false,\"fieldName\":\"createTime\"}]}";
		
		// Criteria c2 = CriteriaUtils.json2Criteria(json);
		/*
		 * Pageable page = PageRequest.of(0, 10);
		 * 
		 * JpaUtil.linq(Permission.class) .where(c) .collect(Role.class, "roleId")
		 * .collect(Element.class, "resourceId") .exists(RoleGrantedAuthority.class)
		 * .equalProperty("roleId", "roleId") .equal("actorId", "admin") .end()
		 * .paging(page);
		 */	
	}

	@Override
	@Transactional
	public Result<User> updatePassWithCheck(UpatePassVo upatePassVo) {
		if (StringUtils.isBlank(upatePassVo.getUsername())
				||StringUtils.isBlank(upatePassVo.getNewPassword())
				||StringUtils.isBlank(upatePassVo.getOldPassword())){
			return Result.error("提交数据不完整，请确认");
		}
		User u = JpaUtil.getOne(User.class, upatePassVo.getUsername());
		if (u==null){
			return Result.error("用户名不存在，请确认");
		}
		String dbpassword = u.getPassword();
		if (!passwordEncoder.matches(upatePassVo.getOldPassword(),dbpassword)){
			return Result.error("原密码不正确，请确认");
		}
		u.setPassword(passwordEncoder.encode(upatePassVo.getNewPassword()));
		u.setPwdUpdateTime(new Date());
		JpaUtil.update(u);
		return Result.OK("修改成功",u);
	}

	@Override
	@Transactional(readOnly = true)
	public long count() {
		return JpaUtil.count(User.class);
	}
}
