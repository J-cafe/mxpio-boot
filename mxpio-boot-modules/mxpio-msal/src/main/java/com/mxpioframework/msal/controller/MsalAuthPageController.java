// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.mxpioframework.msal.controller;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.msal.utils.AuthHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;

/**
 * Controller exposing application endpoints
 */
@Tag(name = "MsalAuthPageController", description = "微软masl认证接口")
@RestController
@RequestMapping("/msal")
public class MsalAuthPageController {

    @Autowired
    AuthHelper authHelper;


    @RequestMapping("/authredirecturl")//该接口需要配置到mxpio.customAnonymous白名单中（目前由前端固定拼接，未使用该接口）
    @Operation(summary = "系统未登录，重定向微软认证登录页面url链接地址", description = "系统未登录，重定向微软认证登录页面url链接地址", method = "GET")
    public Result getSendAuthRedirectUrl(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws MalformedURLException {
        return Result.OK(authHelper.getSendAuthRedirectUrl(authHelper.getRedirectUriSignIn()));
    }


}
