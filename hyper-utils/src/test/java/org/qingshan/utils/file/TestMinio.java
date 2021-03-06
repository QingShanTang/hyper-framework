package org.qingshan.utils.file;

import io.minio.StatObjectResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.qingshan.utils.file.minio.MinioProp;
import org.qingshan.utils.file.minio.MinioUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TestMinio {
    private MinioUtil minioUtil;

    @SneakyThrows
    @Before
    public void before() {
        MinioProp minioProp = new MinioProp();
        minioProp.setEndpoint("http://localhost:9078");
        minioProp.setSecretkey("geekplus");
        minioProp.setAccesskey("geekplus");
        minioUtil = MinioUtil.init(minioProp);
    }

    @SneakyThrows
    @Test
    public void testMinio() {

        System.out.println(minioUtil.bucketExists("heihei"));
        minioUtil.makeBucket("haha");
        System.out.println(minioUtil.ifObjectExist("haha", "tt5.json"));
        minioUtil.putObject("haha", "folder10/xii", "/Users/mac/Desktop/upload/DKNOrderAllocationAlgoOutput-1.json");
        minioUtil.putObject("haha", "xixi1.json", new FileInputStream("/Users/mac/Desktop/poppick.txt"));
        minioUtil.putDirObject("haha", "folder1/folder6/");
        minioUtil.removeBucket("xixi");
        System.out.println(minioUtil.getObjectUrl("haha", "xixi1.json", 2, TimeUnit.HOURS));
        InputStream in = minioUtil.getObject("haha", "xixi1.json");
        IOUtils.copy(in, new FileOutputStream("/Users/mac/Desktop/wowo.json"));
        StatObjectResponse response = minioUtil.getObjectInfo("haha", "xixi1.json");
        minioUtil.removeObject("haha", "xixi1.json");
        minioUtil.listObjects("xixi", "folder1", false);
        minioUtil.removeBucket("xixi",true);
        minioUtil.clearBucket("haha");
    }
}
