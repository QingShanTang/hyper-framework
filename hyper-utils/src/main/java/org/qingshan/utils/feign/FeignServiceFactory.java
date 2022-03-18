package org.qingshan.utils.feign;

import com.netflix.config.ConfigurationManager;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.qingshan.utils.json.JSONUtil;

import java.io.IOException;
import java.util.Properties;

/**
 * feign service工厂类
 */
@Slf4j
public class FeignServiceFactory {

    private FeignProperties configProperties;

    /**
     * feign ribbon hystrix 配置文件路径
     *
     * @param configProperties
     */
    public FeignServiceFactory(FeignProperties configProperties) {
        try {
            Properties properties = new Properties();
            //先加载默认的配置文件
            properties.load(this.getClass().getClassLoader().getResourceAsStream("config/default/feign.properties"));
            //如果存在客户配置,则覆盖默认配置的属性
            if (StringUtils.isNotBlank(configProperties.getConfigPath())) {
                properties.load(this.getClass().getClassLoader().getResourceAsStream(configProperties.getConfigPath()));
            }
            //如果存在properties注入属性，则覆盖以上配置
            properties.putAll(configProperties.getConfigProperties());
            log.info("加载feign配置信息成功,feignProperties->{}", JSONUtil.toJSONString(properties));
            ConfigurationManager.loadProperties(properties);
        } catch (IOException e) {
            log.error("Abnormal reading of Feign configuration file,errorMsg->{}", e);
            throw new RuntimeException("Abnormal reading of Feign configuration file,errorMsg->" + e.getLocalizedMessage());
        }
    }


    /**
     * @param clientName      客户端名称(配置文件属性名称前缀)
     * @param serviceClazz    服务类型
     * @param fallbackFactory 自定义降级方法
     * @return
     */
    public <T> T buildService(
            String clientName,
            Class<T> serviceClazz,
            FallbackFactory<T> fallbackFactory
    ) {
        return FeignServiceBuilder.buildFeignServiceWithRibbonAndHystrix(clientName, serviceClazz, fallbackFactory);
    }
}
