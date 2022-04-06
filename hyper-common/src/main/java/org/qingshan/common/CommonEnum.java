package org.qingshan.common;

public class CommonEnum {

    /**
     * 开关
     */
    public enum Switch {

        ON("ON", 1, true),
        OFF("OFF", 0, false);

        private String str;
        private Integer num;
        private Boolean bool;

        Switch(String str, Integer num, Boolean bool) {
            this.str = str;
            this.num = num;
            this.bool = bool;
        }

        public String getStr() {
            return str;
        }

        public Integer getNum() {
            return num;
        }

        public Boolean getBool() {
            return bool;
        }
    }
}
