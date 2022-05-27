package org.qingshan.web.utils.serviceQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * 基于Spring的服务发现
 */
@Component
public class ServiceQuerier {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 获取指定服务的所有实现
     *
     * @param service
     * @param <T>
     * @return
     */
    public <T> Map<String, T> getServiceAllImpl(Class<T> service) {
        return applicationContext.getBeansOfType(service);
    }

    /**
     * 根据key获取指定服务的指定实现
     *
     * @param service
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getServiceImplByKey(Class<T> service, String key) {
        return getServiceAllImpl(service).get(key);
    }

    /**
     * 获取指定服务所有实现列表
     *
     * @param clazz
     * @param annoClazz
     * @param <T>
     * @return
     */
    public <T> Map<String, String> getServiceTypeList(Class<T> clazz, Class<? extends MyServiceAnno> annoClazz) {
        Map<String, String> typeMap = new HashMap<>();
        Map<String, T> serviceList = getServiceAllImpl(clazz);
        serviceList.forEach((key, service) -> {
            MyServiceAnno anno = service.getClass().getDeclaredAnnotation(MyServiceAnno.class);
            typeMap.put(anno.key(), anno.desc());
        });
        return typeMap;
    }
}
