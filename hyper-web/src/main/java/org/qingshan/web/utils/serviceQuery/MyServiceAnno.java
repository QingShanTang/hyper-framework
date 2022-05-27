package org.qingshan.web.utils.serviceQuery;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 组合注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Service
public @interface MyServiceAnno {
    String desc();

    //属性关联 && 重命名
    @AliasFor(annotation = Service.class, value = "value")
    String key();
}
