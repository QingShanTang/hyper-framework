package org.qingshan.utils.clazz;

import java.lang.reflect.Modifier;

public class ClassUtil {
    /**
     * 获取类型访问标志
     *
     * @param clazz
     * @return
     */
    public static String getModifiers(Class clazz) {
        return Modifier.toString(clazz.getModifiers());
    }

    /**
     * 是否是普通类型
     *
     * @param clazz
     * @return
     */
    public static boolean isCommonClass(Class clazz) {
        return clazz.getModifiers() == 1;
    }
}
