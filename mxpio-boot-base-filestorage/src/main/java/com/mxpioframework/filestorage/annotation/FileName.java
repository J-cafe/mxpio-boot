package com.mxpioframework.filestorage.annotation;

import com.mxpioframework.filestorage.entity.MxpioFileInfo;
import com.mxpioframework.security.annotation.Dict;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Dict(dicCode = "fileNo", dicEntity = MxpioFileInfo.class, dicText = "fileName")
public @interface FileName {
}
