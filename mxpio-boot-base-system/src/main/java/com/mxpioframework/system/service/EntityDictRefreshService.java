package com.mxpioframework.system.service;


public interface EntityDictRefreshService {
    <T> void deleteCache(T entity);
}
