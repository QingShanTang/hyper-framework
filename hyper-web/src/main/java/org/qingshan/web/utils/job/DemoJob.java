package org.qingshan.web.utils.job;

import lombok.extern.slf4j.Slf4j;
import org.qingshan.web.utils.job.anno.JobExecutor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

@Slf4j
public class DemoJob implements Job {

    @Autowired
    private Environment env;

    @JobExecutor
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("任务逻辑");
    }
}
