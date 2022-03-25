package org.qingshan.dao.config;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.geekplus.hyperpulsealgorithm.ApplicationEnum;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 多数据源配置
 */
@Slf4j
@Configuration
public class MultiDatasourceConfig {

    //数据源类型
    private ApplicationEnum.DatasourceType type;

    @Autowired
    private DataSource dataSource;

    @Value("${spring.datasource.dynamic.primary}")
    private String primary;

    @Autowired
    public void changePrimary() {
        if (StringUtils.isNotBlank(primary)) {
            type = Enum.valueOf(ApplicationEnum.DatasourceType.class, primary.toUpperCase());
        }
        if (null == type) {
            log.error("无法确认数据源信息!");
            throw new RuntimeException("无法确认数据源信息!");
        }

        ((DynamicRoutingDataSource) dataSource).setPrimary(type.getName());
        log.info("当前数据源是:{}", type.getName());
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.dynamic.datasource.sqlite.liquibase")
    public LiquibaseProperties sqliteLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.dynamic.datasource.mysql.liquibase")
    public LiquibaseProperties mysqlLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean(name = "liquibaseProperties")
    public LiquibaseProperties liquibaseProperties() {
        if (ApplicationEnum.DatasourceType.MYSQL == type) {
            return mysqlLiquibaseProperties();
        } else if (ApplicationEnum.DatasourceType.SQLITE == type) {
            return sqliteLiquibaseProperties();
        }
        return null;
    }

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        return springLiquibase(dataSource, liquibaseProperties());
    }

    private static SpringLiquibase springLiquibase(DataSource dataSource, LiquibaseProperties properties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(properties.getChangeLog());
        liquibase.setShouldRun(properties.isEnabled());
        return liquibase;
    }
}
