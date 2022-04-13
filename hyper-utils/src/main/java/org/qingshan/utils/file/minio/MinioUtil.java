package org.qingshan.utils.file.minio;

import cn.hutool.core.util.ReUtil;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.qingshan.utils.json.JSONUtil;
import org.qingshan.utils.string.StringTemplateUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Slf4j
public class MinioUtil {

    private MinioClient minioClient;

    public static MinioUtil init(MinioProp minioProp) throws Exception {
        MinioUtil minioUtil = new MinioUtil();
        minioUtil.buildClient(minioProp);
        if (CollectionUtils.isNotEmpty(minioProp.getBuckets())) {
            for (String bucket : minioProp.getBuckets()) {
                minioUtil.makeBucket(bucket);
            }
        }

        return minioUtil;
    }

    public void buildClient(MinioProp minioProp) {
        this.minioClient = MinioClient.builder()
                .endpoint(minioProp.getEndpoint())
                .credentials(minioProp.getAccesskey(), minioProp.getSecretkey())
                .build();
    }


    public boolean bucketExists(String bucketName) throws Exception {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 创建bucket
     *
     * @param bucketName
     */
    public void makeBucket(String bucketName) throws Exception {
        if (!bucketExists(bucketName)) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }


    /**
     * 删除bucket
     * 不可删除非空bucket
     *
     * @param bucketName
     * @throws Exception
     */
    public void removeBucket(String bucketName) throws Exception {
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    }

    /**
     * 强制删除bucket(慎用!!!)
     *
     * @param bucketName
     * @param force
     * @throws Exception
     */
    public void removeBucket(String bucketName, boolean force) throws Exception {
        if (force) {
            clearBucket(bucketName);
        }
        removeBucket(bucketName);
    }


    /**
     * 判断文件是否存在
     *
     * @param bucketName
     * @param objectName
     * @return
     */
    public boolean ifObjectExist(String bucketName, String objectName) {
        boolean exist = true;
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (Exception e) {
            exist = false;
        }
        return exist;
    }


    /**
     * 上传本地文件
     *
     * @param bucketName 存储桶
     * @param objectName 对象名称
     * @param fileName   本地文件路径
     */
    public ObjectWriteResponse putObject(String bucketName, String objectName,
                                         String fileName) throws Exception {
        return minioClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket(bucketName).object(objectName).filename(fileName).build());
    }

    /**
     * 上传本地文件
     *
     * @param bucketName
     * @param pathTemp
     * @param pathTempParams
     * @param fileName
     * @return
     * @throws Exception
     */
    public ObjectWriteResponse putObject(String bucketName,
                                         String pathTemp,
                                         Map<String, String> pathTempParams,
                                         boolean checkExist,
                                         String fileName
    ) throws Exception {
        String filePath = StringTemplateUtil.fill(pathTemp, pathTempParams, true);
        filePath = StringUtils.removeStart(filePath, "/");
        if (!ReUtil.isMatch("^(?!\\/)(.+\\/)?([^(\\.|\\/)]+?)\\.([^\\/]+)$", filePath)) {
            throw new Exception(MessageFormat.format("非标准文件路径格式,path:{0}", filePath));
        }
        if (checkExist && ifObjectExist(bucketName, filePath)) {
            throw new Exception("该对象已存在,谨慎操作!objectName:" + filePath);
        }
        return putObject(bucketName, filePath, fileName);
    }

    public List<ObjectWriteResponse> putObjects(String bucketName,
                                                String pathTemp,
                                                Map<String, String> pathTempParams,
                                                boolean checkExist,
                                                String... path
    ) throws Exception {
        List<ObjectWriteResponse> responseList = new ArrayList<>();
        String folderPath = StringTemplateUtil.fill(pathTemp, pathTempParams, true);
        folderPath = StringUtils.removeStart(folderPath, "/");
        String finalFolderPath = StringUtils.appendIfMissing(folderPath, "/", "/");
        if (!ReUtil.isMatch("^(?!\\/)(.+\\/)$", finalFolderPath)) {
            throw new Exception(MessageFormat.format("非标准文件夹路径格式,path:{0}", finalFolderPath));
        }
        //扫描文件列表
        List<File> fileList = scanFiles(path);
        if (checkExist && ifObjectsExist(bucketName, fileList.stream().map(file -> finalFolderPath + file.getName()).toArray(String[]::new))) {
            throw new Exception("对象已存在,谨慎操作!");
        }
        for (File file : fileList) {
            responseList.add(putObject(bucketName, finalFolderPath + file.getName(), file.getAbsolutePath()));
        }
        return responseList;
    }

    /**
     * 检查对象是否存在
     *
     * @param bucketName
     * @param objectNames
     * @return
     */
    private boolean ifObjectsExist(String bucketName, String... objectNames) {
        log.info("检查对象是否存在...");
        List<String> existObj = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(objectNames)) {
            for (String path : objectNames) {
                if (ifObjectExist(bucketName, path)) {
                    existObj.add(path);
                }
            }
        }
        if (CollectionUtils.isEmpty(existObj)) {
            log.info("无已存在对象");
            return false;
        } else {
            log.info("以下对象已存在,existObj:{}", JSONUtil.toJSONString(existObj));
            return true;
        }
    }


    /**
     * 扫描文件列表
     *
     * @param paths
     * @return
     * @throws Exception
     */
    private List<File> scanFiles(String... paths) throws Exception {
        log.info("扫描文件列表...");
        List<File> fileList = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(paths)) {
            for (String path : paths) {
                File file = new File(path);
                if (!file.exists()) {
                    log.info("该路径不存在,path:{}", path);
                    throw new Exception("该路径不存在,path:" + path);
                }
                if (file.isFile()) {
                    fileList.add(file);
                } else if (file.isDirectory()) {
                    Collection<File> files = FileUtils.listFiles(file, null, false);
                    fileList.addAll(files);
                }
            }
        }
        log.info("扫描文件列表结束,fileList:{}", JSONUtil.toJSONString(fileList.stream().map(file -> file.getAbsolutePath()).toArray()));
        return fileList;
    }

    /**
     * 通过流上传文件
     *
     * @param bucketName  存储桶
     * @param objectName  文件对象
     * @param inputStream 文件流
     */
    public ObjectWriteResponse putObject(String bucketName, String objectName, InputStream inputStream) throws Exception {
        return minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
                        inputStream, inputStream.available(), -1)
                        .build());
    }

    /**
     * 创建文件夹或目录
     *
     * @param bucketName 存储桶
     * @param objectName 目录路径(以 / 结尾)
     */
    public ObjectWriteResponse putDirObject(String bucketName, String objectName) throws Exception {
        return minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
                        new ByteArrayInputStream(new byte[]{}), 0, -1)
                        .build());
    }


    /**
     * 获取⽂件外链
     *
     * @param bucketName
     * @param objectName
     * @param expires    过期时间 <=7
     * @return
     * @throws Exception
     */
    public String getObjectUrl(String bucketName, String objectName, Integer expires, TimeUnit unit) throws Exception {
        return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(objectName).expiry(expires, unit).method(Method.GET).build());
    }

    /**
     * 获取文件
     *
     * @param bucketName
     * @param objectName
     * @return
     * @throws Exception
     */
    public InputStream getObject(String bucketName, String objectName) throws Exception {
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }


    /**
     * 批量获取对象
     *
     * @param bucketName
     * @param prefix
     * @throws Exception
     */
    public Map<String, InputStream> listObjects(String bucketName, String prefix, boolean recursive) throws Exception {
        if (StringUtils.startsWith(prefix, "/")) {
            prefix = StringUtils.removeStart(prefix, "/");
        }
        Map<String, InputStream> objects = new HashMap<>();
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucketName).prefix(prefix).recursive(recursive).build());
        for (Result<Item> result : results) {
            if (result.get().isDir()) {
                if (!StringUtils.endsWith(prefix, "/")) {
                    prefix += "/";
                }
                return listObjects(bucketName, prefix, recursive);
            } else {
                String objectName = result.get().objectName();
                objects.put(objectName, getObject(bucketName, objectName));
            }
        }
        return objects;
    }

    public Map<String, InputStream> listObjects(String bucketName, String pathTemp, Map<String, String> pathTempParams, boolean recursive) throws Exception {
        String prefix = StringTemplateUtil.fill(pathTemp, pathTempParams, true);
        return listObjects(bucketName, prefix, recursive);
    }


    /**
     * 获取文件信息
     *
     * @param bucketName
     * @param objectName
     * @return
     * @throws Exception
     */
    public StatObjectResponse getObjectInfo(String bucketName, String objectName) throws Exception {
        return minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    /**
     * 删除文件
     *
     * @param bucketName
     * @param objectName
     * @throws Exception
     */
    public void removeObject(String bucketName, String objectName) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    /**
     * 清空bucket
     *
     * @param bucketName
     * @throws Exception
     */
    public void clearBucket(String bucketName) throws Exception {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            // 递归列举某个bucket下的所有文件，然后循环删除
            Iterable<Result<Item>> iterable = minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(bucketName)
                    .recursive(true)
                    .build());
            for (Result<Item> itemResult : iterable) {
                removeObject(bucketName, itemResult.get().objectName());
            }
        }
    }
}


