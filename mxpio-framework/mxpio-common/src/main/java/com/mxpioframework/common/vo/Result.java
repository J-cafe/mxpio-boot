package com.mxpioframework.common.vo;

import java.io.Serializable;

import com.mxpioframework.common.CommonConstant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description="接口返回对象")
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 成功标志
	 */
	@Schema(description = "成功标志")
	private boolean success = true;

	/**
	 * 返回处理消息
	 */
	@Schema(description = "返回处理消息")
	private String message = "操作成功！";

	/**
	 * 返回代码
	 */
	@Schema(description = "返回代码")
	private Integer code = 0;
	
	/**
	 * 返回数据对象 data
	 */
	@Schema(description = "返回数据对象")
	private T result;
	
	/**
	 * 时间戳
	 */
	@Schema(description = "时间戳")
	private long timestamp = System.currentTimeMillis();

	public Result() {
		
	}
	
	public Result<T> success(String message) {
		this.message = message;
		this.code = CommonConstant.HTTP_OK;
		this.success = true;
		return this;
	}
	
	public static<T> Result<T> OK() {
		Result<T> r = new Result<T>();
		r.setSuccess(true);
		r.setCode(CommonConstant.HTTP_OK);
		r.setMessage("成功");
		return r;
	}

	public static<T> Result<T> OK(T data) {
		Result<T> r = new Result<T>();
		r.setSuccess(true);
		r.setCode(CommonConstant.HTTP_OK);
		r.setResult(data);
		return r;
	}

	public static<T> Result<T> OK(String msg, T data) {
		Result<T> r = new Result<T>();
		r.setSuccess(true);
		r.setCode(CommonConstant.HTTP_OK);
		r.setMessage(msg);
		r.setResult(data);
		return r;
	}
	
	public static<T> Result<T> error(String msg) {
		return error(CommonConstant.HTTP_SERVER_ERROR, msg);
	}
	
	public static<T> Result<T> error(int code, String msg) {
		Result<T> r = new Result<T>();
		r.setCode(code);
		r.setMessage(msg);
		r.setSuccess(false);
		return r;
	}

	public Result<T> error500(String message) {
		this.message = message;
		this.code = CommonConstant.HTTP_SERVER_ERROR;
		this.success = false;
		return this;
	}
	/**
	 * 无权限访问返回结果
	 */
	public static<T> Result<T> noauth401(String msg) {
		return error(CommonConstant.HTTP_NO_AUTHZ_401, msg);
	}
	
	/**
	 * 无权限访问返回结果
	 */
	public static<T> Result<T> noauth403(String msg) {
		return error(CommonConstant.HTTP_NO_AUTHZ_403, msg);
	}

	/**
	 * 用户名、密码、验证码、用户状态异常
	 */
	public static<T> Result<T> noauth40101(String msg) {
		return error(CommonConstant.HTTP_NO_AUTHZ_40101, msg);
	}

}