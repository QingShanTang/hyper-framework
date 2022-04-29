package org.qingshan.web.utils.job.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class JobExecutorAspect {

    @Pointcut(value = "@annotation(org.qingshan.web.utils.job.anno.JobExecutor) && args(context)", argNames = "context")
    private void aspect(JobExecutionContext context) {
    }

    @Before("aspect(context)")
    public void before(JobExecutionContext context) throws Exception {
        log.info("任务执行。。。");
    }

    @AfterReturning("aspect(context)")
    public void afterReturning(JobExecutionContext context) throws Exception {
        log.info("任务执行成功");
    }

    @AfterThrowing(throwing = "e", pointcut = "aspect(context)")
    public void afterThrowing(Throwable e, JobExecutionContext context) throws Exception {
        log.error("任务执行失败,errorMsg->", e);
    }

}
