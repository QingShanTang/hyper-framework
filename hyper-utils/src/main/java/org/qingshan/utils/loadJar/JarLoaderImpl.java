package org.qingshan.utils.loadJar;

import cn.hutool.core.io.FileUtil;
import lombok.SneakyThrows;

import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class JarLoaderImpl implements JarLoader {

    private static Map<String, JarPOJO> jarMap = new ConcurrentHashMap<>();

    public static Map<String, JarPOJO> getJarMap() {
        return jarMap;
    }


    /**
     * 加载
     *
     * @param type
     * @param jarFilePath
     */
    @SneakyThrows
    @Override
    public URLClassLoader load(String type, String jarFilePath) {
        JarPOJO pojo = jarMap.get(type);
        if (null != pojo) {
            throw new RuntimeException(MessageFormat.format("已存在该类型jar,请先卸载!type:{0}", type));
        }
        //加载jar
        URLClassLoader classLoader = JarLoadUtil.loadJar(jarFilePath);
        jarMap.put(type, new JarPOJO(type, jarFilePath, classLoader));

        //TODO 业务逻辑

        return classLoader;
    }

    @SneakyThrows
    public void unload(String type) {
        JarPOJO pojo = jarMap.get(type);
        if (null == pojo) {
            throw new RuntimeException(MessageFormat.format("无此类型jar! type:{0}", type));
        }
        URLClassLoader classLoader = getClassLoader(type);
        // 卸载动态加载进来的jar
        JarLoadUtil.unloadJar(classLoader);

        //TODO 业务逻辑

        //删除文件
        FileUtil.del(pojo.getJarFilePath());
        //删除map中对应信息
        jarMap.remove(type);
    }

    /**
     * 获取classLoader
     *
     * @param type
     * @return
     */
    @Override
    public URLClassLoader getClassLoader(String type) {
        return null == jarMap.get(type) ? null : jarMap.get(type).getClassLoader();
    }

    /**
     * 获取所有classLoader
     *
     * @return
     */
    @Override
    public List<ClassLoader> getAllClassLoader() {
        return jarMap.values().stream().map(item -> item.getClassLoader()).collect(Collectors.toList());
    }
}
