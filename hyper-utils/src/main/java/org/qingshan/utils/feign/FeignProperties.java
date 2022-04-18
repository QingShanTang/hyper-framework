package org.qingshan.utils.feign;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Properties;

/**
 * feign 配置
 */
@Data
@Accessors(chain = true)
public class FeignProperties {
    /**
     * 配置文件路径
     */
    private String configPath;

    /**
     * 通过properties注入配置属性
     */
    private Properties configProperties = new Properties();
}
