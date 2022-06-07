package org.qingshan.utils.clazz;

import org.junit.Test;

public class TestClassUtil {

    @Test
    public void testGetModifiers() {
        System.out.println(ClassUtil.getModifiers(Object.class));
        System.out.println(ClassUtil.isCommonClass(Object.class));
        System.out.println(ClassUtil.getModifiers(Override.class));
        System.out.println(ClassUtil.isCommonClass(Override.class));
        System.out.println(ClassUtil.getModifiers(Readable.class));
        System.out.println(ClassUtil.getModifiers(Number.class));
        System.out.println(ClassUtil.getModifiers(Short.class));
        System.out.println(ClassUtil.getModifiers(TestEnum.class));
        System.out.println(TestEnum.class.isEnum());
        System.out.println(Short.class.isEnum());
        System.out.println(Readable.class.isInterface());
        System.out.println(Object.class.isInterface());
        System.out.println(Override.class.isAnnotation());
        System.out.println(Readable.class.isAnnotation());
    }
}
