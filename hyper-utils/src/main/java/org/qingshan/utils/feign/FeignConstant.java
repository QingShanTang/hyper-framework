package org.qingshan.utils.feign;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FeignConstant {

    /**
     * 默认配置文件路径
     */
    public static final String defaultConfigPath = "config/default/feign.properties";

    /**
     * 逗号分隔符
     */
    public static final String COMMA_SEPARATOR = ",";

    /**
     * 服务容器
     */
    public static final ConcurrentMap<Class, FeignServicePOJO> serviceContainer = new ConcurrentHashMap();
}
