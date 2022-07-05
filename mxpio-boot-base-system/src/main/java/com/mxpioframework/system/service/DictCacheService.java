package com.mxpioframework.system.service;

public interface DictCacheService {
    <T> T get(Object key);

    <T> T put(Object key, T value);
  }
