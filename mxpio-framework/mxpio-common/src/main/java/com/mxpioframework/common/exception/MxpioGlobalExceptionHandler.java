package com.mxpioframework.common.exception;

import com.mxpioframework.common.vo.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Order
public class MxpioGlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(MxpioGlobalExceptionHandler.class);

    @ExceptionHandler(value = MBootException.class)
    public Result<?> mbootExceptionHandler(MBootException e, HttpServletRequest request){
        logger.error("MBootException,url:{}",request.getRequestURI(),e);
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(value = RuntimeException.class)
    public Result<?> runtimeExceptionHandler(RuntimeException e, HttpServletRequest request){
        logger.error("RuntimeException,url:{}",request.getRequestURI(),e);
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public Result<?> exceptionHandler(Exception e, HttpServletRequest request){
        logger.error("系统异常,url:{}",request.getRequestURI(),e);
        return Result.error("系统异常");
    }
}
