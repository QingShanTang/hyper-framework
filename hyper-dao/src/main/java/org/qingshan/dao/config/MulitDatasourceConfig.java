package org.qingshan.dao.config;

import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 多数据源配置
 */
@Slf4j
@Configuration
public class MulitDatasourceConfig {

    @Value("${spring.datasource.dynamic.primary}")
    private String primary;

    @Bean(name = "datasourceInfos")
    @ConfigurationProperties(prefix = "spring.datasource.dynamic.datasource")
    public Map<String, Map<String, LiquibaseProperties>> datasourceInfos() {
        return new HashMap<>();
    }

    @Bean(name = "liquibaseProperties")
    public LiquibaseProperties liquibaseProperties(@Qualifier(value = "datasourceInfos") Map<String, Map<String, LiquibaseProperties>> datasourceInfos) {
        if (null != datasourceInfos.get(primary) && null != datasourceInfos.get(primary).get("liquibase")) {
            return datasourceInfos.get(primary).get("liquibase");
        } else {
            log.warn("该数据源liquibase未配置,datasource:{}", primary);
            return null;
        }
    }

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource, @Qualifier(value = "liquibaseProperties") LiquibaseProperties liquibaseProperties) {
        if (null == liquibaseProperties) {
            liquibaseProperties = new LiquibaseProperties();
            liquibaseProperties.setEnabled(false);
        }
        return springLiquibase(dataSource, liquibaseProperties);
    }

    private static SpringLiquibase springLiquibase(DataSource dataSource, LiquibaseProperties properties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(properties.getChangeLog());
        liquibase.setShouldRun(properties.isEnabled());
        return liquibase;
    }
}
