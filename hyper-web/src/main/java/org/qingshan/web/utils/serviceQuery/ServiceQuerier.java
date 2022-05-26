package org.qingshan.web.utils.serviceQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 基于Spring服务发现
 */
@Component
public class ServiceQuerier {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 获取所有该类型服务
     *
     * @param clazz
     * @return
     */
    public <T> Map<String, T> getAllService(Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz);
    }

    /**
     * 根据key获取指定该类型服务
     *
     * @param clazz
     * @param key
     * @return
     */
    public <T> T getService(Class<T> clazz, String key) {
        return getAllService(clazz).get(key);
    }
}
