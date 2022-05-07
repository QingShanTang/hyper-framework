package org.qingshan.utils.serDes.annotation;

import org.qingshan.utils.serDes.JsonSerDes;
import org.qingshan.utils.serDes.SerDes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 数据序列化反序列器注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSerDes {
    Class<? extends SerDes> using() default JsonSerDes.class;
}
