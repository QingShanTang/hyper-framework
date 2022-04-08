package org.qingshan.utils.string;

import lombok.extern.slf4j.Slf4j;
import org.qingshan.utils.json.JSONUtil;

@Slf4j
public class StrUtil {
    public static String obj2Str(Object obj) {
        if (null == obj) {
            return null;
        } else if (obj instanceof String) {
            //String类型转json会有双引号
            return (String) obj;
        } else {
            return JSONUtil.toJSONString(obj);
        }
    }
}
