package com.mxpioframework.security.access.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.alibaba.fastjson.JSON;
import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.vo.Result;

public class MxpioAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest arg0, HttpServletResponse response, AccessDeniedException arg2)
			throws IOException, ServletException {
		response.setContentType("application/json;charset=UTF-8");
        Result<String> result = new Result<>();
        result.setSuccess(false);
        result.setCode(CommonConstant.HTTP_NO_AUTHZ_403);
        result.setMessage("登陆失效，请重新登陆");
        response.getWriter().write(JSON.toJSONString(result));
	}

}
