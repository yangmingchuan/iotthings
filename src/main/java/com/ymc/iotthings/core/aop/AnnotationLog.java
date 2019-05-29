package com.ymc.iotthings.core.aop;

import java.lang.annotation.*;

/**
 * 切面类
 *
 * package name: com.ymc.iotthings.core.aop
 * date :2019/5/29
 * author : ymc
 **/

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnnotationLog {
    String remark() default "";

}
