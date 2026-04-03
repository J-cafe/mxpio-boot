package com.mxpioframework.jpa.utils;



import com.mxpioframework.jpa.support.IdeaProxyLambdaMeta;
import com.mxpioframework.jpa.support.LambdaMeta;
import com.mxpioframework.jpa.support.ReflectLambdaMeta;
import com.mxpioframework.jpa.support.SFunction;
import com.mxpioframework.jpa.support.ShadowLambdaMeta;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class LambdaUtils {

    public static <T> LambdaMeta extract(SFunction<T, ?> func) {
        // 1. IDEA 调试模式下 lambda 表达式是一个代理
        if (func instanceof Proxy) {
            return new IdeaProxyLambdaMeta((Proxy) func);
        }
        // 2. 反射读取
        try {
            Method method = func.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            return new ReflectLambdaMeta((SerializedLambda) method.invoke(func), func.getClass().getClassLoader());
        } catch (Throwable e) {
            // 3. 反射失败使用序列化的方式读取
            return new ShadowLambdaMeta(com.mxpioframework.jpa.support.SerializedLambda.extract(func));
        }
    }
}
