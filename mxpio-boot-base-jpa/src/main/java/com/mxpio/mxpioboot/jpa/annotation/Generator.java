package com.mxpio.mxpioboot.jpa.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.mxpio.mxpioboot.jpa.policy.GeneratorPolicy;
import com.mxpio.mxpioboot.jpa.policy.impl.UUIDPolicy;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Generator {
	Class<? extends GeneratorPolicy> policy() default UUIDPolicy.class;
}
