package com.mxpioframework.jpa.support;

import com.mxpioframework.jpa.utils.ClassUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.invoke.SerializedLambda;

/**
 * @author HCL
 * Created at 2021/5/14
 */
@Slf4j
public class ReflectLambdaMeta implements LambdaMeta {
    private final SerializedLambda lambda;

    private final ClassLoader classLoader;

    public ReflectLambdaMeta(SerializedLambda lambda, ClassLoader classLoader) {
        this.lambda = lambda;
        this.classLoader = classLoader;
    }

    @Override
    public String getImplMethodName() {
        return lambda.getImplMethodName();
    }

    @Override
    public Class<?> getInstantiatedClass() {
        String instantiatedMethodType = lambda.getInstantiatedMethodType();
        String instantiatedType = instantiatedMethodType.substring(2, instantiatedMethodType.indexOf(";")).replace("/", ".");
        return ClassUtils.toClassConfident(instantiatedType, this.classLoader);
    }

}
