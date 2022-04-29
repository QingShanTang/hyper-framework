package org.qingshan.web;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.qingshan.utils.quartz.QuartzBean;
import org.qingshan.utils.quartz.QuartzEnum;
import org.qingshan.utils.quartz.QuartzUtil;
import org.qingshan.web.utils.job.DemoJob;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestJobAspect {

    private QuartzUtil quartzUtil;

    @Autowired
    private Scheduler scheduler;

    @SneakyThrows
    @Before
    public void before() {
        //scheduler必须是spring的bean
        quartzUtil = QuartzUtil.init(scheduler);
    }

    /**
     * 测试运行一次
     *
     * @throws Exception
     */
    @Test
    public void testJobAspect() throws Exception {
        QuartzBean quartzBean = QuartzBean.builder()
                .jobName("runOnce")
                .jobClazz(DemoJob.class)
                .build();
        quartzUtil.operateJob(quartzBean, QuartzEnum.JobOperateType.RUNONCE);
        System.in.read();
    }
}
