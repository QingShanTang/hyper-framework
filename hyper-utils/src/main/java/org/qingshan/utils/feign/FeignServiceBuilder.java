package org.qingshan.utils.feign;

import com.netflix.client.ClientFactory;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.IClientConfig;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.hystrix.FallbackFactory;
import feign.hystrix.HystrixFeign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.ribbon.RibbonClient;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;

/**
 * Feign 服务构造器
 */
@Slf4j
public class FeignServiceBuilder {
    public static <T> T buildFeignServiceWithRibbonAndHystrix(String clientName, Class<T> t, FallbackFactory<T> fallbackFactory) {
        //如果配置了连接超时和读写超时,要另外配置
        IClientConfig config = ClientFactory.getNamedConfig(clientName);
        Integer connectTimeout = config.getPropertyAsInteger(CommonClientConfigKey.ConnectTimeout, 10000);
        Integer readTimeout = config.getPropertyAsInteger(CommonClientConfigKey.ReadTimeout, 60000);
        if (null == fallbackFactory) {
            T service = Feign.builder()
                    .client(RibbonClient.create())
                    .encoder(new JacksonEncoder())
                    .decoder(new JacksonDecoder())
                    .retryer(Retryer.NEVER_RETRY)//如果不关闭,ribbon重试机制外面还会套一层feign重试机制
                    .options(new Request.Options(connectTimeout, readTimeout))
                    .target(t, MessageFormat.format("http://{0}", clientName));
            return service;
        } else {
            T service = HystrixFeign.builder()
                    .client(RibbonClient.create())
                    .encoder(new JacksonEncoder())
                    .decoder(new JacksonDecoder())
                    .retryer(Retryer.NEVER_RETRY)//如果不关闭,ribbon重试机制外面还会套一层feign重试机制
                    .options(new Request.Options(connectTimeout, readTimeout))
                    .target(t, MessageFormat.format("http://{0}", clientName), fallbackFactory);
            return service;
        }
    }
}
