package org.qingshan.utils.feign;

import feign.hystrix.FallbackFactory;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * feign服务对象
 */
@Data
@AllArgsConstructor
public class FeignServicePOJO<T> {
    /**
     * 实体
     */
    private T service;

    /**
     * 客户端名称
     */
    private String clientName;

    /**
     * 类型
     */
    private Class<T> clazz;

    /**
     * hystrix回调器
     */
    private FallbackFactory<T> fallbackFactory;
}
