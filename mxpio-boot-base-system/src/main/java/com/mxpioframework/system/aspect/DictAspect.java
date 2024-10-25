package com.mxpioframework.system.aspect;

import com.mxpioframework.common.vo.Result;

import com.mxpioframework.system.service.PojoDictParseService;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DictAspect {

    @Autowired
    private PojoDictParseService dictService;

    // 定义切点Pointcut
    @Pointcut("execution(public * *..*.*Controller.*(..))")
    public void excudeService() {
    }

    @SuppressWarnings("rawtypes")
    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Object result = pjp.proceed();
        if (result instanceof Result) {
            dictService.parseDictResult((Result) result);
        }
        return result;
    }
}