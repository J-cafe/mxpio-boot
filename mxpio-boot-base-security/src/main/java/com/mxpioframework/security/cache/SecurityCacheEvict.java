package com.mxpioframework.security.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import com.mxpioframework.security.common.Constants;

@Target({ElementType.METHOD, ElementType.TYPE})  
@Retention(RetentionPolicy.RUNTIME)  
@Inherited 
@Caching(evict = {
		@CacheEvict(cacheNames = {
				Constants.URL_TREE_CACHE_KEY,
				Constants.DATA_LIST_CACHE_KEY,
				Constants.REQUEST_MAP_CACHE_KEY,
				Constants.ELEMENT_MAP_CACHE_KEY,
				Constants.ELEMENT_ATTRIBUTE_MAP_CACHE_KEY,
				Constants.DATA_ATTRIBUTE_MAP_CACHE_KEY,
				Constants.DATA_RESOURCE_CACHE_KEY,
				Constants.USER_DEPT_CACHE_KEY,
		}, keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
})
public @interface SecurityCacheEvict {

}
