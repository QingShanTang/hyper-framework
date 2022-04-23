package org.qingshan.utils.feign;

import com.netflix.config.ConfigurationManager;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.qingshan.utils.json.JSONUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.qingshan.utils.feign.FeignConstant.defaultConfigPath;

/**
 * feign service工厂类
 */
@Slf4j
public class FeignServiceFactory {

    /**
     * 加载默认配置文件
     */
    static {
        log.info("加载Feign默认配置,defaultConfigPath:{}", defaultConfigPath);
        try (InputStream defaultConfig = FeignServiceFactory.class.getClassLoader().getResourceAsStream(defaultConfigPath)) {
            if (null != defaultConfig) {
                Properties defaultProperties = new Properties() {{
                    load(defaultConfig);
                }};
                log.info("defaultProperties:{}", JSONUtil.toJSONString(defaultProperties));
                config(defaultProperties);
            } else {
                log.warn("无默认配置!");
            }
        } catch (IOException e) {
            log.error("加载Feign默认配置异常!errorMsg->", e);
        }
    }

    /**
     * 初始化自定义配置
     *
     * @param configProperties
     */
    public static void customConfig(FeignProperties configProperties) {
        log.info("加载Feign自定义配置,configProperties:{}", JSONUtil.toJSONString(configProperties));
        try {
            Properties customProperties = new Properties();
            //加载自定义文件配置
            if (StringUtils.isNotBlank(configProperties.getConfigPath())) {
                try (InputStream customConfig = FeignServiceFactory.class.getClassLoader().getResourceAsStream(configProperties.getConfigPath())) {
                    customProperties.load(customConfig);
                }
            }
            //如果存在properties注入属性，则覆盖自定义文件配置
            customProperties.putAll(configProperties.getConfigProperties());
            log.info("加载Feign自定义配置成功,customProperties->{}", JSONUtil.toJSONString(customProperties));
            config(customProperties);
        } catch (IOException e) {
            log.error("Abnormal reading of Feign configuration file,errorMsg->", e);
            throw new RuntimeException("Abnormal reading of Feign configuration file,errorMsg->" + e.getLocalizedMessage());
        }
    }

    /**
     * 加载配置
     *
     * @param properties
     */
    public static void config(Properties properties) {
        log.info("加载Feign配置,properties:{}", JSONUtil.toJSONString(properties));
        ConfigurationManager.loadProperties(properties);
        FeignConstant.serviceContainer.forEach((key, value) -> {
            buildService(value.getClientName(), value.getClazz(), value.getFallbackFactory());
        });
    }


    /**
     * @param clientName      客户端名称(配置文件属性名称前缀)
     * @param serviceClazz    服务类型
     * @param fallbackFactory 自定义降级方法
     * @return
     */
    public static <T> T buildService(
            String clientName,
            Class<T> serviceClazz,
            FallbackFactory<T> fallbackFactory
    ) {
        T t = FeignServiceBuilder.buildFeignServiceWithRibbonAndHystrix(clientName, serviceClazz, fallbackFactory);
        FeignConstant.serviceContainer.put(serviceClazz, new FeignServicePOJO(t, clientName, serviceClazz, fallbackFactory));
        return t;
    }
}
