package org.qingshan.utils.log;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import lombok.extern.slf4j.Slf4j;
import org.qingshan.utils.json.JSONUtil;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志查询工具
 */
@Slf4j
public class LogQueryUtil {

    private static List<String> filterLogFiles(String logRootPath, String formatDateStr) {
        log.info("过滤日志文件。。。");
        List<File> fileList = new ArrayList<>();
        List<File> fileList1 = FileUtil.loopFiles(logRootPath, new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (ReUtil.isMatch("^.*\\/sys\\.log$", pathname.getAbsolutePath())) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        List<File> fileList2 = FileUtil.loopFiles(logRootPath, new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (ReUtil.isMatch("^.*\\/sys\\." + formatDateStr + "\\.\\d\\.log\\.gz$", pathname.getAbsolutePath())) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        fileList.addAll(fileList2);
        fileList.addAll(fileList1);
        List<String> filePathList = new ArrayList<>();
        fileList.forEach(file -> {
            filePathList.add(file.getAbsolutePath());
        });
        log.info("filePathList:{}", JSONUtil.toJSONString(filePathList));
        log.info("过滤日志文件结束");
        return filePathList;
    }

    public static void main(String[] args) {
        filterLogFiles("/Users/mac/fsdownload", "2022-06-05");
    }
}
