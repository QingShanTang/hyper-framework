package org.qingshan.utils.loadJar;

import org.junit.Test;
import org.qingshan.utils.json.JSONUtil;

import java.lang.reflect.Method;

public class TestLoadJar {
    @Test
    public void testLoadJar() {
        // jar 路径
        String jarPath = "/Users/mac/projects/demo/hephaestus-mock-core/out/artifacts/hephaestus_mock_core_jar/hephaestus-mock-core.jar";
        // 加载
        LoadJarUtil.loadJar(jarPath);
        // 类路径
        String classPath = "com.geekplus.hephaestus.mock.core.mock.impl.MockAlgorithmImpl";
        try {
            Class<?> clazz = Class.forName(classPath);
            // 创建实例
            Object checkService = clazz.getDeclaredConstructor().newInstance();
            // 获取check方法，方法参数为String类型
            Method check = checkService.getClass().getMethod("calculate", Class.forName("com.geekplus.hephaestus.mock.core.mock.MockContext"));
            // 调用check方法，传入参数，强转返回值
            Object result = check.invoke(checkService, Class.forName("com.geekplus.hephaestus.mock.core.mock.MockContext").newInstance());
            System.out.println(JSONUtil.toJSONString(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
