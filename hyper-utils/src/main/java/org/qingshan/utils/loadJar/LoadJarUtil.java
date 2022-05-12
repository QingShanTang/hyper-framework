package org.qingshan.utils.loadJar;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class LoadJarUtil {

    /**
     * 动态加载本地jar，参数是jar绝对物理路径
     */
    public static void loadJar(String jarPath) {

        URL url = null;
        try {
            // 加载本地jar
            File jarFile = new File(jarPath);
            url = jarFile.toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (url != null) {
            // 从URLClassLoader类中获取类所在文件夹的方法，jar也可以认为是一个文件夹
            Method addURL = null;
            try {
                addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            // 获取方法的访问权限以便写回
            boolean accessible = addURL.isAccessible();
            try {
                addURL.setAccessible(true);

                // 获取系统类加载器
                URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
                addURL.invoke(classLoader, url);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                addURL.setAccessible(accessible);
            }
        }
    }
}
