package org.qingshan.utils.yaml;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.qingshan.utils.json.JSONUtil;

import java.util.List;
import java.util.Map;

@Slf4j
public class TestYaml {
    @Test
    public void testLoadYaml() {
        JSONUtil.printJSONStringWithFormat(YamlLoader.loadClassPathFile("yaml.yaml", new TypeReference<List<Map>>() {
        }));
        JSONUtil.printJSONStringWithFormat(YamlLoader.loadClassPathFile("yaml.yaml", List.class));
    }
}
