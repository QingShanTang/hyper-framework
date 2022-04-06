package org.qingshan.utils.file.minio;

public class MinioEnum {
    public enum PathTemp {
        INPUT("输入", "#{folder1}/#{folder2}/");
        private String desc;
        private String pathTemp;

        PathTemp(String desc, String pathTemp) {
            this.desc = desc;
            this.pathTemp = pathTemp;
        }

        public String getDesc() {
            return desc;
        }

        public String getPathTemp() {
            return pathTemp;
        }
    }
}
