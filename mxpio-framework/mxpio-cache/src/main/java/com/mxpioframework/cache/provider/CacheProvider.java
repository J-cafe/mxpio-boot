package com.mxpioframework.cache.provider;

import java.util.concurrent.TimeUnit;

public interface CacheProvider {

	/**
	 * 判断key是否存在
	 *
	 * @param key 键
	 * @return true 存在 false不存在
	 */
	boolean hasKey(String key);
	
	/**
	 * 删除缓存
	 * @param keys 可以传一个值 或多个
	 */
	void del(String... keys);
	
	/**
	 * 普通缓存获取
	 * @param key 键
	 * @return 值
	 */
	Object get(String key);

	/**
	 * 普通缓存放入
	 *
	 * @param key 键
	 * @param value 值
	 * @return true成功 false失败
	 */
	boolean set(String key, Object value);

	/**
	 * 普通缓存放入
	 *
	 * @param key 键
	 * @param value 值
	 * @param time 时间，秒
	 * @return true成功 false失败
	 */
	boolean set(String key, Object value,long time);

	/**
	 * 普通缓存放入
	 *
	 * @param key 键
	 * @param value 值
	 * @param time 时间
	 * @param timeUnit 类型
	 * @return true成功 false失败
	 */
	boolean set(String key, Object value, long time, TimeUnit timeUnit);

	/**
	 * 清除当前应用的所有缓存
	 * <p>
	 * 仅清除带本应用key前缀的缓存，不影响共用缓存实例的其他应用
	 *
	 * @return 清除的缓存数量
	 */
	long clear();

}
