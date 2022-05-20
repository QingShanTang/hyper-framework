package org.qingshan.utils.loadJar;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import sun.misc.ClassLoaderUtil;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;

/**
 * jar load 工具
 */
@Slf4j
public class JarLoadUtil {
    /**
     * 卸载当前jar
     */
    @SneakyThrows
    public static void unloadJar(URLClassLoader classLoader) {
        if (null != classLoader) {
            ClassLoaderUtil.releaseLoader(classLoader);
        }
    }

    /**
     * 动态加载本地jar
     */
    @SneakyThrows
    public static URLClassLoader loadJar(String jarPath) {
        // 加载本地jar
        File jarFile = new File(jarPath);
        // 如果不存在该文件
        if (!jarFile.exists()) {
            log.error("指定加载的jar不存在, jarPath:{}", jarPath);
            throw new RuntimeException(MessageFormat.format("指定加载的jar不存在, jarPath:{0}", jarPath));
        }
        URL url = jarFile.toURI().toURL();
        return new URLClassLoader(new URL[]{url});
    }
}
