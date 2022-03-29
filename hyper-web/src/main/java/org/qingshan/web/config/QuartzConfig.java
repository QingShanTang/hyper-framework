package org.qingshan.web.config;

import lombok.SneakyThrows;
import org.qingshan.utils.quartz.QuartzUtil;
import org.quartz.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @SneakyThrows
    @Bean
    public QuartzUtil quartzUtil(Scheduler scheduler) {
        return QuartzUtil.init(scheduler);
    }

}
