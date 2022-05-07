package org.qingshan.utils.serDes;


import com.fasterxml.jackson.core.type.TypeReference;

/**
 * 序列化反序列化器
 */
public interface SerDes {

    /**
     * 序列化
     *
     * @param o
     * @return
     * @throws Exception
     */
    Object serialize(Object o) throws Exception;

    /**
     * 反序列化
     *
     * @param o
     * @return
     * @throws Exception
     */
    Object deserialize(Object o) throws Exception;

    default Object deserialize(Object o, TypeReference type) throws Exception {
        return deserialize(o);
    }

    default Object deserialize(Object o, Class clazz) throws Exception {
        return deserialize(o);
    }
}
