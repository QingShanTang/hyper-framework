package org.qingshan.utils.quartz;

public class QuartzEnum {
    /**
     * 任务操作类型
     */
    public enum JobOperateType {
        START("启动"),
        STOP("停止"),
        DELETE("删除"),
        RUNONCE("执行一次");

        private String desc;

        JobOperateType(String desc) {
            this.desc = desc;
        }
    }

    /**
     * 时间单位
     */
    public enum DateUnit {

        DAY("天"),
        HOUR("小时"),
        MINUTE("分钟"),
        SECOND("秒"),
        MILLISECOND("毫秒");
        private String desc;

        DateUnit(String desc) {
            this.desc = desc;
        }

        public String getStatus() {
            return this.name();
        }
    }
}
