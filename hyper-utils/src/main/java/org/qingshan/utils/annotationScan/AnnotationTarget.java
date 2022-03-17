package org.qingshan.utils.annotationScan;

import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 注解目标对象
 *
 * @param <T>
 */
@Data
public class AnnotationTarget<T extends Annotation> {
    /**
     * 注解类型
     */
    private ElementType elementType;

    /**
     * 注解标注的包
     */
    private Package pkg;

    /**
     * 注解标注的类
     */
    private Class clazz;

    /**
     * 注解标注的方法
     */
    private Method method;

    /**
     * 注解标注的字段
     */
    private Field field;

    /**
     * 注解的信息
     */
    private T annotation;
}
