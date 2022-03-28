package org.qingshan.web.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.qingshan.utils.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Slf4j
@Component
public class EnvPropertyUtil {

    private static Environment env;

    @Autowired
    public void setEnv(Environment env) {
        EnvPropertyUtil.env = env;
    }

    public static void updateEnv(String source, String key, String value, Boolean status) throws Exception {
        log.info("更新环境变量,source:{},key:{},value:{},status:{}", source, key, value, status);
        if (status && null == value) {
            throw new Exception("已启动的参数值不可为空");
        }
        Assert.assertNotNull("环境变量获取失败", env);
        if (env instanceof ConfigurableEnvironment) {
            PropertySource propertySource = ((ConfigurableEnvironment) env).getPropertySources().get(source);
            if (null == propertySource) {
                propertySource = new PropertiesPropertySource(source, new Properties());
                ((ConfigurableEnvironment) env).getPropertySources().addLast(propertySource);
            }
            if (propertySource instanceof PropertiesPropertySource) {
                PropertiesPropertySource propertiesPropertySource = ((PropertiesPropertySource) propertySource);
                if (status) {
                    propertiesPropertySource.getSource().put(key, value);
                } else {
                    propertiesPropertySource.getSource().remove(key);
                }
                log.info("刷新后环境变量,env:{}", JSONUtil.toJSONString(propertiesPropertySource.getSource()));
            }
        }
    }
}
