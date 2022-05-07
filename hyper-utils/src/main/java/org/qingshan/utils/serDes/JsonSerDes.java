package org.qingshan.utils.serDes;


import com.fasterxml.jackson.core.type.TypeReference;
import org.qingshan.utils.json.JSONUtil;

/**
 * Json serDes
 */
public class JsonSerDes implements SerDes {

    @Override
    public Object serialize(Object o) {
        return null == o ? null : JSONUtil.toJSONString(o);
    }

    @Override
    public Object deserialize(Object o) throws Exception {
        throw new Exception("Json序列化器需指定类型参数!");
    }

    @Override
    public Object deserialize(Object o, TypeReference type) {
        return JSONUtil.parseObject(null == o ? null : o.toString(), type);
    }

    @Override
    public Object deserialize(Object o, Class clazz) throws Exception {
        return JSONUtil.parseObject(null == o ? null : o.toString(), clazz);
    }

}
