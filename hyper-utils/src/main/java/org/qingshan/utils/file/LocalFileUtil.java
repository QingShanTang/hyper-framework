package org.qingshan.utils.file;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class LocalFileUtil {
    private static final DateFormat df = new SimpleDateFormat("yyyyMMdd");
    private static final DateFormat df1 = new SimpleDateFormat("HHmmss");

    /**
     * 根据文件名称,根路径生成本地存储路径
     */
    public static String generateFileLocalSavePath(String fileName, String rootFolder) throws Exception {
        if (StringUtils.isBlank(fileName) || StringUtils.isBlank(rootFolder)) {
            throw new Exception("fileName and rootFolder cannot be null");
        }

        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        StringBuffer fileFinalName = new StringBuffer(fileName)
                .insert(fileName.indexOf("."), "_" + uuid)
                .insert(fileName.indexOf("."), "_" + df1.format(currentDate));

        String[] pathNode = new String[]{
                new File(rootFolder).getAbsolutePath(),
                df.format(currentDate),
                String.valueOf(c.get(Calendar.HOUR_OF_DAY)),
                fileFinalName.toString()
        };

        return StringUtils.join(pathNode, File.separator);
    }

    /**
     * 保存文件至本地
     *
     * @param file
     * @param rootFolder
     * @return
     * @throws Exception
     */
    public static String saveFile2Local(File file, String rootFolder) throws Exception {
        if (null == file || StringUtils.isBlank(rootFolder)) {
            throw new Exception("file and rootFolder cannot be null");
        }
        String finalPath = generateFileLocalSavePath(file.getName(), rootFolder);
        FileUtils.copyFile(file, new File(finalPath));
        return finalPath;
    }

}
