package org.qingshan.utils.quartz;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.qingshan.utils.json.JSONUtil;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Quartz工具类
 *
 * @author admin
 */
@Slf4j
public class QuartzUtil {

    private Scheduler scheduler;

    public static QuartzUtil init() throws Exception {
        return init(null);
    }

    public static QuartzUtil init(Scheduler scheduler) throws Exception {
        QuartzUtil quartzUtil = new QuartzUtil();
        if (null != scheduler) {
            quartzUtil.scheduler = scheduler;
        } else {
            quartzUtil.scheduler = new StdSchedulerFactory().getScheduler();
        }
        quartzUtil.start();
        return quartzUtil;
    }

    /**
     * 启动
     *
     * @throws SchedulerException
     */
    private void start() throws SchedulerException {
        if (null != scheduler) {
            if (!scheduler.isStarted()) {
                scheduler.start();
            }
        } else {
            throw new RuntimeException("scheduler 不可为空");
        }
    }

    /**
     * 操作定时任务
     *
     * @param quartzBean
     * @param operateType
     * @throws Exception
     */
    public void operateJob(QuartzBean quartzBean, QuartzEnum.JobOperateType operateType) throws Exception {
        switch (operateType) {
            case START:
                startJob(quartzBean);
                break;
            case STOP:
                pauseJob(quartzBean.getJobKey());
                break;
            case DELETE:
                deleteJob(quartzBean.getJobKey());
                break;
            case RUNONCE:
                runOnce(quartzBean);
                break;
            default:
                throw new Exception("未知操作类型!");
        }
    }

    /**
     * 启动定时任务
     *
     * @param quartzBean
     * @throws Exception
     */
    public void startJob(QuartzBean quartzBean) throws Exception {
        log.info("启动定时任务,quartzBean:{}", JSONUtil.toJSONString(quartzBean));
        if (ifJobExist(quartzBean.getJobKey())) {
            rescheduleJob(quartzBean);
        } else {
            createJob(quartzBean);
        }
        resumeJob(quartzBean.getJobKey());
    }


    /**
     * 创建定时任务
     *
     * @param quartzBean 定时任务信息类
     * @throws Exception
     */
    public void createJob(QuartzBean quartzBean) throws Exception {
        log.info("创建定时任务,quartzBean:{}", JSONUtil.toJSONString(quartzBean));
        if (ifJobExist(quartzBean.getJobKey())) {
            throw new Exception("定时任务已存在!");
        }
        // 构建定时任务信息
        JobDetail jobDetail = JobBuilder.newJob(quartzBean.getJobClazz()).withIdentity(quartzBean.getJobKey()).usingJobData(quartzBean.getJobDataMap()).build();
        // 构建触发器trigger
        Trigger trigger = buildTrigger(quartzBean);
        scheduler.scheduleJob(jobDetail, trigger);
        log.info("创建定时任务结束");
    }

    /**
     * 构建触发器
     *
     * @param quartzBean
     * @return
     * @throws Exception
     */
    private Trigger buildTrigger(QuartzBean quartzBean) throws Exception {
        log.info("构建触发器,quartzBean:{}", quartzBean);
        String cronExpression = quartzBean.getCronExpression();
        QuartzBean.SimpleConfig simpleConfig = quartzBean.getSimpleConfig();
        Trigger trigger = null;
        if (StringUtils.isNotBlank(cronExpression)) {
            if (!CronExpression.isValidExpression(cronExpression)) {
                throw new Exception("无效cron表达式!");
            }
            trigger = TriggerBuilder.newTrigger().withIdentity(quartzBean.getTriggerKey()).withSchedule(CronScheduleBuilder.cronSchedule(quartzBean.getCronExpression())).build();
        } else if (null != simpleConfig) {
            if (null != simpleConfig.getInterval() && null != simpleConfig.getIntervalUnit()) {
                TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger();
                JobDataMap jobDataMap = new JobDataMap();
                jobDataMap.put("simpleConfig", simpleConfig);
                triggerBuilder.usingJobData(jobDataMap);
                SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
                triggerBuilder.withIdentity(quartzBean.getTriggerKey())
                        .withSchedule(scheduleBuilder);
                if (null != simpleConfig.getStartTime()) {
                    triggerBuilder.startAt(simpleConfig.getStartTime());
                } else {
                    triggerBuilder.startNow();
                }
                if (null != simpleConfig.getEndTime()) {
                    triggerBuilder.endAt(simpleConfig.getEndTime());
                }
                if (QuartzEnum.DateUnit.MILLISECOND == simpleConfig.getIntervalUnit()) {
                    scheduleBuilder.withIntervalInMilliseconds(simpleConfig.getInterval());
                } else if (QuartzEnum.DateUnit.SECOND == simpleConfig.getIntervalUnit()) {
                    scheduleBuilder.withIntervalInSeconds(simpleConfig.getInterval());
                } else if (QuartzEnum.DateUnit.MINUTE == simpleConfig.getIntervalUnit()) {
                    scheduleBuilder.withIntervalInMinutes(simpleConfig.getInterval());
                } else if (QuartzEnum.DateUnit.HOUR == simpleConfig.getIntervalUnit()) {
                    scheduleBuilder.withIntervalInHours(simpleConfig.getInterval());
                } else if (QuartzEnum.DateUnit.DAY == simpleConfig.getIntervalUnit()) {
                    scheduleBuilder.withIntervalInHours(24 * simpleConfig.getInterval());
                }
                if (null != simpleConfig.getRepeatCount()) {
                    scheduleBuilder.withRepeatCount(simpleConfig.getRepeatCount());
                } else {
                    scheduleBuilder.repeatForever();
                }
                trigger = triggerBuilder.build();
            }
        }
        if (null == trigger) {
            throw new Exception("触发器创建信息缺失!");
        }
        return trigger;
    }


    /**
     * 获取trigger状态
     *
     * @param triggerKey
     * @return
     * @throws SchedulerException
     */
    public Trigger.TriggerState getTriggerState(TriggerKey triggerKey) throws SchedulerException {
        Trigger.TriggerState state = scheduler.getTriggerState(triggerKey);
        return state;
    }

    /**
     * 重置触发器
     *
     * @param quartzBean
     * @throws Exception
     */
    public void rescheduleJob(QuartzBean quartzBean) throws Exception {
        log.info("重置触发器,quartzBean:{}", JSONUtil.toJSONString(quartzBean));
        if (ifTriggerChange(quartzBean)) {
            Trigger trigger = buildTrigger(quartzBean);
            scheduler.rescheduleJob(quartzBean.getTriggerKey(), trigger);
        }
    }

    /**
     * 暂停所有定时任务
     *
     * @throws SchedulerException
     */
    public void pauseAllJob() throws SchedulerException {
        log.info("暂停所有定时任务");
        scheduler.pauseAll();
    }

    /**
     * 恢复所有定时任务
     *
     * @throws SchedulerException
     */
    public void resumeAllJob() throws SchedulerException {
        log.info("恢复所有定时任务");
        scheduler.resumeAll();
    }


    /**
     * 暂停定时任务
     *
     * @param jobKey
     * @throws Exception
     */
    public void pauseJob(JobKey jobKey) throws Exception {
        log.info("暂停定时任务,jobKey:{}", JSONUtil.toJSONString(jobKey));
        scheduler.pauseJob(jobKey);
    }


    /**
     * 恢复定时任务
     *
     * @param jobKey
     * @throws Exception
     */
    public void resumeJob(JobKey jobKey) throws Exception {
        log.info("恢复定时任务,jobKey:{}", JSONUtil.toJSONString(jobKey));
        if (!ifJobExist(jobKey)) {
            throw new Exception("定时任务不存在!");
        }
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除定时任务
     *
     * @param jobKey
     * @throws Exception
     */
    public void deleteJob(JobKey jobKey) throws Exception {
        log.info("删除定时任务,jobKey:{}", JSONUtil.toJSONString(jobKey));
        scheduler.deleteJob(jobKey);
    }


    /**
     * 执行一次定时任务
     *
     * @param quartzBean
     * @throws Exception
     */
    public void runOnce(QuartzBean quartzBean) throws Exception {
        log.info("执行一次定时任务,quartzBean:{}", JSONUtil.toJSONString(quartzBean));
        if (ifJobExist(quartzBean.getJobKey())) {
            scheduler.triggerJob(quartzBean.getJobKey());
        } else {
            // 构建定时任务信息
            JobDetail jobDetail = JobBuilder.newJob(quartzBean.getJobClazz()).withIdentity(quartzBean.getJobKey()).usingJobData(quartzBean.getJobDataMap()).build();
            SimpleTrigger simpleTrigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(quartzBean.getTriggerKey())
                    .startNow()
                    .withSchedule(
                            SimpleScheduleBuilder.simpleSchedule()
                                    .withIntervalInMilliseconds(1)
                                    .withRepeatCount(0))
                    .build();
            scheduler.scheduleJob(jobDetail, simpleTrigger);
        }
    }

    /**
     * 执行一次定时任务
     *
     * @param jobKey
     * @throws Exception
     */
    public void runOnce(JobKey jobKey) throws Exception {
        log.info("执行一次定时任务,jobKey:{}", JSONUtil.toJSONString(jobKey));
        if (!ifJobExist(jobKey)) {
            throw new Exception("定时任务不存在!");
        }
        scheduler.triggerJob(jobKey);
    }


    /**
     * 判断job是否运行
     *
     * @param triggerKey
     * @return
     * @throws SchedulerException
     */
    public Boolean ifTriggerRun(TriggerKey triggerKey) throws SchedulerException {
        Trigger.TriggerState state = getTriggerState(triggerKey);
        if (state == Trigger.TriggerState.NORMAL) {
            return true;
        }
        return false;
    }

    /**
     * 判断job是否存在
     *
     * @param jobKey
     * @return
     * @throws SchedulerException
     */
    public Boolean ifJobExist(JobKey jobKey) throws SchedulerException {
        return scheduler.checkExists(jobKey);
    }

    /**
     * 判断trigger是否变化
     *
     * @param quartzBean
     * @return
     * @throws SchedulerException
     */
    public Boolean ifTriggerChange(QuartzBean quartzBean) throws SchedulerException {
        Boolean ifChange = true;
        Object oldConfig = null;
        String cronExpression = quartzBean.getCronExpression();
        QuartzBean.SimpleConfig simpleConfig = quartzBean.getSimpleConfig();
        Trigger trigger = scheduler.getTrigger(quartzBean.getTriggerKey());
        if (StringUtils.isBlank(cronExpression) && null == simpleConfig) {
            ifChange = false;
        } else if (StringUtils.isNotBlank(cronExpression) && trigger instanceof CronTrigger) {
            String oldCron = ((CronTrigger) trigger).getCronExpression();
            oldConfig = oldCron;
            if (cronExpression.equals(oldCron)) {
                ifChange = false;
            }
        } else if (null != simpleConfig && trigger instanceof SimpleTrigger) {
            QuartzBean.SimpleConfig oldSimpleConfig = (QuartzBean.SimpleConfig) trigger.getJobDataMap().get("simpleConfig");
            oldConfig = oldSimpleConfig;
            if (simpleConfig.equals(oldSimpleConfig)) {
                ifChange = false;
            }
        }
        return ifChange;
    }
}
