package org.qingshan.web.config;

import lombok.SneakyThrows;
import org.qingshan.utils.file.minio.MinioProp;
import org.qingshan.utils.file.minio.MinioUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Bean
    @ConfigurationProperties(prefix = "minio")
    public MinioProp minioProp() {
        return new MinioProp();
    }

    @SneakyThrows
    @Bean
    public MinioUtil minioUtil(MinioProp minioProp) {
        return MinioUtil.init(minioProp);
    }
}
