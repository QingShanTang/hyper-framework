package org.qingshan.utils.yaml;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.qingshan.utils.json.JSONUtil;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

import java.io.InputStream;


/**
 * yml 加载器
 */
@Slf4j
public class YamlLoader {

    private static Object load(InputStream in) {
        Yaml yaml = new Yaml();
        yaml.setBeanAccess(BeanAccess.FIELD);
        return yaml.load(in);
    }

    public static <T> T loadClassPathFile(String path, Class<T> clazz) {
        try (InputStream in = YamlLoader.class.getClassLoader().getResourceAsStream(path)) {
            return JSONUtil.parseObject(JSONUtil.toJSONString(load(in)), clazz);
        } catch (Exception e) {
            log.error("加载yml文件异常,errorMsg->", e);
            throw new RuntimeException("加载yml文件异常,errorMsg->", e);
        }
    }

    public static <T> T loadClassPathFile(String path, TypeReference<T> typeReference) {
        try (InputStream in = YamlLoader.class.getClassLoader().getResourceAsStream(path)) {
            return JSONUtil.parseObject(JSONUtil.toJSONString(load(in)), typeReference);
        } catch (Exception e) {
            log.error("加载yml文件异常,errorMsg->", e);
            throw new RuntimeException("加载yml文件异常,errorMsg->", e);
        }
    }
}
