package org.qingshan.utils.stringTemplate;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.qingshan.utils.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TestStringTemplate {
    private static final Map<String, String> map = new HashMap<>();

    static {
        try {
            map.putAll(StringTemplateUtil.load("StringTemplate.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    @Test
    public void testLoad() {
        Map<String, String> map = StringTemplateUtil.load("StringTemplate.xml");
        log.info(JSONUtil.toJSONString(map));
    }

    @SneakyThrows
    @Test
    public void testFill() {
        Map<String, String> params = new HashMap<>();
        params.put("id", "1");
        params.put("name", "xixi");
        String result = StringTemplateUtil.getTemplateAndFill(map, "demo", params);
        log.info(JSONUtil.toJSONString(result));
    }
}
