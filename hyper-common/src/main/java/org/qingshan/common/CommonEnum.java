package org.qingshan.common;

public class CommonEnum {

    /**
     * 开关
     */
    public enum Status {

        SUCCESS("SUCCESS", 1, true),
        FAIL("FAIL", 0, false);

        private String str;
        private Integer num;
        private Boolean bool;

        Status(String str, Integer num, Boolean bool) {
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
