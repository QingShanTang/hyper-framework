package org.qingshan.utils.loadJar;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URLClassLoader;

/**
 * jar信息
 */
@Data
@AllArgsConstructor
public class JarPOJO {
    /**
     * 类型
     */
    private String type;

    /**
     * jar文件路径
     */
    private String jarFilePath;

    /**
     * classLoader
     */
    private URLClassLoader classLoader;

}
