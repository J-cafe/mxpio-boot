package com.mxpioframework.security.service.impl;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.security.entity.UserProfile;
import com.mxpioframework.security.service.UserProfileService;
import com.mxpioframework.security.util.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service("mxpio.security.userProfileService")
public class UserProfileServiceImpl extends BaseServiceImpl<UserProfile> implements UserProfileService {

    @Override
    @Transactional(readOnly = true)
    public List<UserProfile> listByPageKey(String pageKey) {
        return JpaUtil.linq(UserProfile.class).equal("pageKey", pageKey).equal("userId", SecurityUtils.getLoginUsername()).list();
    }
}
