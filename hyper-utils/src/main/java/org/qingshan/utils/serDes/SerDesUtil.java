package org.qingshan.utils.serDes;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.qingshan.utils.serDes.annotation.DataSerDes;

import java.util.Base64;


/**
 * serDes工具
 */
@Slf4j
public class SerDesUtil {
    /**
     * 默认serDes
     */
    private static final SerDes DEFAULT_SERDES = new JsonSerDes();

    /**
     * 从serDes目标中获取指定serDes
     * 如果没有指定serDes，默认为JsonSerDes
     *
     * @param target
     * @return
     */
    public static SerDes getSerDesFromTarget(Object target) {
        try {
            if (null != target && target.getClass().isAnnotationPresent(DataSerDes.class)) {
                return target.getClass().getAnnotation(DataSerDes.class).using().newInstance();
            } else {
                return DEFAULT_SERDES;
            }
        } catch (Exception e) {
            log.error("获取序列化反序列器失败,className:{},errorMsg->{}", target.getClass().getTypeName(), e.getLocalizedMessage());
            return DEFAULT_SERDES;
        }
    }


    /**
     * 从类中获取指定serDes
     * 如果没有指定serDes，默认为JsonSerDes
     *
     * @param clazzName
     * @return
     */
    public static SerDes getSerDesFromClazz(String clazzName) {
        try {
            if (StringUtils.isNotBlank(clazzName) && Class.forName(clazzName).isAnnotationPresent(DataSerDes.class)) {
                return Class.forName(clazzName).getAnnotation(DataSerDes.class).using().newInstance();
            } else {
                return DEFAULT_SERDES;
            }
        } catch (Exception e) {
            log.error("获取序列化反序列器失败,className:{},errorMsg->{}", clazzName, e.getLocalizedMessage());
            return DEFAULT_SERDES;
        }
    }

    public static SerDes getSerDesFromClazz(Class clazz) {
        return getSerDesFromClazz(clazz.getTypeName());
    }

    /**
     * 序列化结果转字符串
     *
     * @param o
     * @return
     */
    public static String serResult2Str(Object o) {
        if (null == o) {
            return "";
        } else if ("byte[]".equals(o.getClass().getTypeName())) {
            return Base64.getEncoder().encodeToString((byte[]) o);
        } else if (o instanceof String) {
            return (String) o;
        } else {
            return o.toString();
        }
    }
}
