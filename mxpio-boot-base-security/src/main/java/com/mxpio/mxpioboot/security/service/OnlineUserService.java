package com.mxpio.mxpioboot.security.service;

import org.springframework.scheduling.annotation.Async;

import com.mxpio.mxpioboot.common.redis.RedisUtils;
import com.mxpio.mxpioboot.security.entity.User;

public interface OnlineUserService {
	
	/**
     * 保存在线用户信息
     * @param jwtUserDto /
     * @param token /
     * @param request /
     */
	public void save(User user, String token, RedisUtils redisUtil);
	
	/**
     * 踢出用户
     * @param key /
     */
	public void kickOut(String key, RedisUtils redisUtil);
	
	/**
     * 退出登录
     * @param token /
     */
    public void logout(String token, RedisUtils redisUtil);
    
    /**
     * 查询用户
     * @param key /
     * @return /
     */
    public User getOne(String key, RedisUtils redisUtil);
    
    /**
     * 根据用户名强退用户
     * @param username /
     */
    @Async
    public void kickOutForUsername(String username, RedisUtils redisUtil) throws Exception;
    
    /**
     * 检测用户是否在之前已经登录，已经登录踢下线
     * @param userName 用户名
     */
    public void checkLoginOnUser(String userName, String igoreToken, RedisUtils redisUtil);

}
