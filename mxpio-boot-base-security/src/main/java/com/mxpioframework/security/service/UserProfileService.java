package com.mxpioframework.security.service;

import com.mxpioframework.security.entity.UserProfile;

import java.util.List;

public interface UserProfileService extends BaseService<UserProfile> {


    List<UserProfile> listByPageKey(String pageKey);
}
