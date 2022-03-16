package org.qingshan.utils.stackTrace;

public class StackTraceEnum {
    /**
     * 追踪目标类型
     */
    public enum targetType {
        METHOD("{}", "方法"),
        BLOCK("()", "代码块"),
        POINT(".", "断点");

        private String icon;
        private String desc;

        targetType(String icon, String desc) {
            this.icon = icon;
            this.desc = desc;
        }

        public String getIcon() {
            return icon;
        }

        public String getDesc() {
            return desc;
        }
    }
}
