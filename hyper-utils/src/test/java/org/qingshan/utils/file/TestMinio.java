package org.qingshan.utils.file;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.qingshan.utils.file.minio.MinioProp;
import org.qingshan.utils.file.minio.MinioUtil;

@Slf4j
public class TestMinio {

    @SneakyThrows
    @Before
    public void before() {
        MinioProp minioProp = new MinioProp();
        minioProp.setEndpoint("http://localhost:9078");
        minioProp.setSecretkey("geekplus");
        minioProp.setAccesskey("geekplus");
        minioProp.setBucketName("xixi");
        MinioUtil.initClient(minioProp);
    }

    @SneakyThrows
    @Test
    public void testMinio() {
//        System.out.println(MinioUtil.bucketExists("heihei"));
//        MinioUtil.makeBucket("haha");
//        System.out.println(MinioUtil.ifObjectExist("haha","tt5.json"));
//        MinioUtil.putObject("haha","folder10/xixi1.json","/Users/mac/Desktop/poppick.txt");
//        MinioUtil.putObject("haha","xixi1.json",new FileInputStream("/Users/mac/Desktop/poppick.txt"));
//        MinioUtil.putDirObject("haha", "folder1/folder6/");
//        MinioUtil.removeBucket("xixi");
//        System.out.println(MinioUtil.getObjectUrl("haha", "xixi1.json", 2, TimeUnit.HOURS));
//        InputStream in = MinioUtil.getObject("haha","xixi1.json");
//        IOUtils.copy(in,new FileOutputStream("/Users/mac/Desktop/wowo.json"));
//        StatObjectResponse response = MinioUtil.getObjectInfo("haha", "xixi1.json");
//        MinioUtil.removeObject("haha","xixi1.json");
    }
}
