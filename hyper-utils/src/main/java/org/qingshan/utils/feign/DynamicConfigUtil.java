package org.qingshan.utils.feign;

import cn.hutool.json.JSON;
import com.netflix.config.ConfigurationManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.qingshan.utils.json.JSONUtil;

import java.util.Properties;

@Slf4j
public class DynamicConfigUtil {
    /**
     * 配置服务列表
     *
     * @param listOfServers
     */
    protected static Boolean setListOfServers(String... listOfServers) {
        log.info("配置算法服务列表,listOfServers:{}", listOfServers);
        if (ArrayUtils.isEmpty(listOfServers)) {
            log.error("算法服务列表不可为空");
            return false;
        }
        Properties properties = new Properties();
        properties.setProperty("hyperpulse.ribbon.listOfServers", String.join(FeignConstant.COMMA_SEPARATOR, listOfServers));
        config(properties);
        log.info("配置算法服务列表成功");
        return true;
    }

    /**
     * 配置feign
     *
     * @param properties
     */
    protected static void config(Properties properties) {
        log.info("配置feign,properties:{}", JSONUtil.toJSONString(properties));
        ConfigurationManager.loadProperties(properties);
        log.info("配置feign成功");
    }
}
