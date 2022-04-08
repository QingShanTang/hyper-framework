package org.qingshan.utils.yaml;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.qingshan.utils.json.JSONUtil;

import java.util.List;

@Slf4j
public class TestYaml {
    @Test
    public void testLoadYaml() {
        List<User> userList = YamlLoader.loadClassPathFile("test.yaml", new TypeReference<List<User>>() {
        });
        JSONUtil.printJSONStringWithFormat(userList);
        List list = YamlLoader.loadClassPathFile("test.yaml", List.class);
        JSONUtil.printJSONStringWithFormat(list);
    }
}
