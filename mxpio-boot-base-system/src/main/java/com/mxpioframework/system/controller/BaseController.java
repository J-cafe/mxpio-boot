package com.mxpioframework.system.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ModelAttribute;

import com.mxpioframework.security.entity.BaseEntity;

public class BaseController<E extends BaseEntity> {

	protected HttpServletRequest request;

	protected HttpServletResponse response;

	protected HttpSession session;

	@ModelAttribute
	public void setBaseBizController(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.session = request.getSession();
	}

}
