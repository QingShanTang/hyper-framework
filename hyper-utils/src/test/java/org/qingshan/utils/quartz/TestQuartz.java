package org.qingshan.utils.quartz;


import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

@Slf4j
public class TestQuartz {

    /**
     * 测试job是否存在
     *
     * @throws SchedulerException
     */
    @Test
    public void testCheckExists() throws SchedulerException {
        Assert.assertTrue(QuartzUtil.ifJobExist(JobKey.jobKey("name", "group")));
    }

    /**
     * 测试运行一次
     *
     * @throws Exception
     */
    @Test
    public void testRunOnce() throws Exception {
        QuartzBean quartzBean = QuartzBean.builder()
                .jobName("runOnce")
                .jobClazz(PrintTimeJob.class)
                .build();
        QuartzUtil.operateJob(quartzBean, QuartzEnum.JobOperateType.RUNONCE);
        System.in.read();
    }

    /**
     * 测试重启job
     *
     * @throws Exception
     */
    @Test
    public void testResumeJob() throws Exception {
        QuartzUtil.resumeJob(JobKey.jobKey("name", "group"));
    }

    @Test
    public void testAll() throws Exception {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobName", "printTime");
        QuartzBean bean1 = QuartzBean.builder()
                .jobName("printTime")
                .jobClazz(PrintTimeJob.class)
                .cronExpression("*/10 * * * * ?")
                .jobDataMap(jobDataMap)
                .build();
        QuartzUtil.operateJob(bean1, QuartzEnum.JobOperateType.START);
        Thread.sleep(24000);
        QuartzUtil.operateJob(bean1, QuartzEnum.JobOperateType.STOP);
        Thread.sleep(30000);
        QuartzUtil.operateJob(bean1, QuartzEnum.JobOperateType.START);
        Thread.sleep(40000);
        QuartzUtil.operateJob(bean1, QuartzEnum.JobOperateType.DELETE);
        Thread.sleep(8000);
        QuartzUtil.operateJob(bean1, QuartzEnum.JobOperateType.RUNONCE);
        Thread.sleep(8000);
        QuartzUtil.operateJob(bean1, QuartzEnum.JobOperateType.DELETE);
        Thread.sleep(8000);
        QuartzUtil.operateJob(bean1, QuartzEnum.JobOperateType.START);
        Thread.sleep(8000);
        bean1 = QuartzBean.builder()
                .jobName("printTime")
                .jobClazz(PrintTimeJob.class)
                .simpleConfig(QuartzBean.SimpleConfig
                        .builder()
                        .interval(5)
                        .intervalUnit(QuartzEnum.DateUnit.SECOND)
                        .repeatCount(3)
                        .build()
                )
                .jobDataMap(jobDataMap)
                .build();
        QuartzUtil.operateJob(bean1, QuartzEnum.JobOperateType.START);
        Thread.sleep(180000);
    }

}
