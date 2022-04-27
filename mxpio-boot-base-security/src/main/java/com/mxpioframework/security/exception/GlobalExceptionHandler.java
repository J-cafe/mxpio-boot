package com.mxpioframework.security.exception;

import java.net.ConnectException;
import java.security.SignatureException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.vo.Result;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConnectException.class)
    public Result<String> connectException(ConnectException e){
    	Result<String> result = new Result<>();
        result.setSuccess(false);
        result.setCode(CommonConstant.HTTP_SERVER_ERROR);
        result.setMessage("系统之间调用异常");
        return result;
    }

    @ExceptionHandler(ResourceAccessException.class)
    public Result<String> connectException(ResourceAccessException e){
    	Result<String> result = new Result<>();
        result.setSuccess(false);
        result.setCode(CommonConstant.HTTP_SERVER_ERROR);
        result.setMessage("系统之间调用异常");
        return result;
    }

    @ExceptionHandler(value = SignatureException.class)
    public Result<String> signatureException(SignatureException e) {
        Result<String> result = new Result<>();
        result.setSuccess(false);
        result.setCode(CommonConstant.HTTP_NO_AUTHZ_403);
        result.setMessage("Token格式错误");
        return result;
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result<String> illegalArgumentException(IllegalArgumentException e) {
    	Result<String> result = new Result<>();
        result.setSuccess(false);
        result.setCode(CommonConstant.HTTP_NO_AUTHZ_403);
        result.setMessage("Token非法参数异常");
        return result;
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public Result<String> accessDeniedException(AccessDeniedException e) {
    	Result<String> result = new Result<>();
        result.setSuccess(false);
        result.setCode(CommonConstant.HTTP_NO_AUTHZ_403);
        result.setMessage("Token非法参数异常");
        return result;
    }
}
