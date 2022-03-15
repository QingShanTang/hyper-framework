package org.qingshan.utils.quartz;

import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.TriggerKey;

import java.util.Date;
import java.util.Objects;

@Data
@Builder
public class QuartzBean {

    /**
     * 任务组名
     */
    @Builder.Default
    private String groupName = QuartzConstant.DEFAULT;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务执行类
     */
    private String jobClazzName;

    /**
     * 任务执行类
     */
    private Class<? extends Job> jobClazz;

    /**
     * 任务运行时间表达式
     * 与simpleConfig二选一,cronExpression优先级更高
     */
    private String cronExpression;

    /**
     * SimpleTrigger配置
     * 与cronExpression二选一,cronExpression优先级更高
     */
    private SimpleConfig simpleConfig;

    /**
     * job数据
     */
    @Builder.Default
    private JobDataMap jobDataMap = new JobDataMap();

    @SneakyThrows
    public Class<? extends Job> getJobClazz() {
        if (null != jobClazz) {
            return jobClazz;
        } else if (StringUtils.isNotBlank(jobClazzName)) {
            return (Class<? extends Job>) Class.forName(jobClazzName);
        } else {
            throw new RuntimeException("job class 不可为空!");
        }
    }

    public JobKey getJobKey() {
        return JobKey.jobKey(jobName, groupName);
    }

    public TriggerKey getTriggerKey() {
        return TriggerKey.triggerKey(jobName, groupName);
    }

    @Data
    @Builder
    public static class SimpleConfig {
        /**
         * 开始时间
         */
        private Date startTime;

        /**
         * 结束时间
         */
        private Date endTime;

        /**
         * 间隔
         */
        private Integer interval;

        /**
         * 间隔单位
         * DAY("天"),
         * HOUR("小时"),
         * MINUTE("分钟"),
         * SECOND("秒");
         */
        private QuartzEnum.DateUnit intervalUnit;

        /**
         * 重复次数
         */
        private Integer repeatCount;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SimpleConfig that = (SimpleConfig) o;
            return Objects.equals(startTime, that.startTime) &&
                    Objects.equals(endTime, that.endTime) &&
                    Objects.equals(interval, that.interval) &&
                    intervalUnit == that.intervalUnit &&
                    Objects.equals(repeatCount, that.repeatCount);
        }

        @Override
        public int hashCode() {
            return Objects.hash(startTime, endTime, interval, intervalUnit, repeatCount);
        }
    }
}
