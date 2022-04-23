package org.qingshan.utils.feign;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.qingshan.utils.json.JSONUtil;

import java.util.Properties;

/**
 * 动态配置工具
 */
@Slf4j
public class FeignDynamicConfigUtil {

    private static final String clientName = "hyper";

    /**
     * 配置服务列表
     *
     * @param listOfServers
     */
    public static Boolean setListOfServers(String... listOfServers) {
        log.info("配置算法服务列表,listOfServers:{}", listOfServers);
        if (ArrayUtils.isEmpty(listOfServers)) {
            log.error("算法服务列表不可为空");
            return false;
        }
        Properties properties = new Properties() {{
            setProperty(clientName + "." + "ribbon.listOfServers", String.join(FeignConstant.COMMA_SEPARATOR, listOfServers));
        }};
        FeignServiceFactory.config(properties);
        log.info("配置算法服务列表成功");
        return true;
    }

    /**
     * 配置feign
     *
     * @param properties
     */
    public static void config(Properties properties) {
        log.info("配置feign,properties:{}", JSONUtil.toJSONString(properties));
        FeignServiceFactory.config(properties);
        log.info("配置feign成功");
    }

    /**
     * 设置超时时间
     *
     * @param connectTimeout
     * @param readTimeout
     */
    public static void setTimeout(int connectTimeout, int readTimeout) {
        log.info("配置超时时间,connectTimeout:{},readTimeout:{}", connectTimeout, readTimeout);
        Properties properties = new Properties() {{
            setProperty(clientName + "." + "ribbon.ConnectTimeout", String.valueOf(connectTimeout));
            setProperty(clientName + "." + "ribbon.ReadTimeout", String.valueOf(readTimeout));
        }};
        FeignServiceFactory.config(properties);
        log.info("配置超时时间成功");
    }

}
