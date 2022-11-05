package com.mxpioframework.module.cache.redis.provider;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import com.mxpioframework.cache.provider.CacheProvider;

import lombok.extern.slf4j.Slf4j;

/**
 * @author /
 */
@Slf4j
@Component
public class RedisCacheProvider implements CacheProvider {
	private RedisTemplate<Object, Object> redisTemplate;

	public RedisCacheProvider(RedisTemplate<Object, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/**
	 * 判断key是否存在
	 *
	 * @param key
	 *            键
	 * @return true 存在 false不存在
	 */
	@Override
	public boolean hasKey(String key) {
		try {
			return redisTemplate.hasKey(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * 删除缓存
	 *
	 * @param keys
	 *            可以传一个值 或多个
	 */
	@Override
	public void del(String... keys) {
		if (keys != null && keys.length > 0) {
			if (keys.length == 1) {
				boolean result = redisTemplate.delete(keys[0]);
				log.debug("--------------------------------------------");
				log.debug(new StringBuilder("删除缓存：").append(keys[0]).append("，结果：").append(result).toString());
				log.debug("--------------------------------------------");
			} else {
				Set<Object> keySet = new HashSet<>();
				for (String key : keys) {
					keySet.addAll(redisTemplate.keys(key));
				}
				long count = redisTemplate.delete(keySet);
				log.debug("--------------------------------------------");
				log.debug("成功删除缓存：" + keySet.toString());
				log.debug("缓存删除数量：" + count + "个");
				log.debug("--------------------------------------------");
			}
		}
	}
	/**
	 * 普通缓存获取
	 *
	 * @param key
	 *            键
	 * @return 值
	 */
	@Override
	public Object get(String key) {
		return key == null ? null : redisTemplate.opsForValue().get(key);
	}

	/**
	 * 普通缓存放入
	 *
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return true成功 false失败
	 */
	@Override
	public boolean set(String key, Object value) {
		try {
			redisTemplate.opsForValue().set(key, value);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * 普通缓存放入并设置时间
	 *
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param time
	 *            时间(秒) time要大于0 如果time小于等于0 将设置无限期
	 * @return true成功 false 失败
	 */
	public boolean set(String key, Object value, long time) {
		try {
			if (time > 0) {
				redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
			} else {
				set(key, value);
			}
			return true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * 普通缓存放入并设置时间
	 *
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param time
	 *            时间
	 * @param timeUnit
	 *            类型
	 * @return true成功 false 失败
	 */
	public boolean set(String key, Object value, long time, TimeUnit timeUnit) {
		try {
			if (time > 0) {
				redisTemplate.opsForValue().set(key, value, time, timeUnit);
			} else {
				set(key, value);
			}
			return true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}
}
