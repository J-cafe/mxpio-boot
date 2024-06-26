package com.mxpioframework.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.mxpioframework.security.entity.BaseEntity;
import com.mxpioframework.security.entity.DictItem;

@Target({ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Dict {
	
    String dicCode();
    
    Class<? extends BaseEntity> dicEntity() default DictItem.class;

    String dicText() default "";

    String displayProp() default "";
}
