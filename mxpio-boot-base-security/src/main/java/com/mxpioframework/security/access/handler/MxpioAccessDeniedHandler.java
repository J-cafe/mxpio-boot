package com.mxpioframework.security.access.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.vo.Result;

@Component
public class MxpioAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception)
			throws IOException, ServletException {
		response.setContentType("application/json;charset=UTF-8");
        Result<String> result = new Result<>();
        result.setSuccess(false);
        result.setCode(CommonConstant.HTTP_NO_AUTHZ);
        result.setMessage("无权限访问");
        response.getWriter().write(JSON.toJSONString(result));
	}

}
