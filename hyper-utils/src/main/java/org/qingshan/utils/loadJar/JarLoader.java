package org.qingshan.utils.loadJar;

import java.net.URLClassLoader;
import java.util.List;

public interface JarLoader {

    /**
     * 加载
     *
     * @param type
     * @param jarFilePath
     */
    URLClassLoader load(String type, String jarFilePath);

    /**
     * 卸载jar
     *
     * @param type
     */
    void unload(String type);

    URLClassLoader getClassLoader(String type);

    List<ClassLoader> getAllClassLoader();
}
