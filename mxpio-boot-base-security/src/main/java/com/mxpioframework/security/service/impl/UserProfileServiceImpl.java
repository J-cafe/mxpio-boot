package com.mxpioframework.security.service.impl;

import com.mxpioframework.security.entity.UserProfile;
import com.mxpioframework.security.service.UserProfileService;
import org.springframework.stereotype.Service;

@Service("mxpio.security.userProfileService")
public class UserProfileServiceImpl extends BaseServiceImpl<UserProfile> implements UserProfileService {

}
