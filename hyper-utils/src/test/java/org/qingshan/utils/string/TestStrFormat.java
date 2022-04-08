package org.qingshan.utils.string;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TestStrFormat {
    @Test
    public void testFormat() {
        //格式化文本, {} 表示占位符
        System.out.println(StrFormatUtil.format("param1:{},param2:{}", "p1", "p2"));
        //有序的格式化文本，使用{number}做为占位符
        System.out.println(StrFormatUtil.indexedFormat("param2:{1},param1:{0}", "p1", "p2"));
        Map<String, String> map = new HashMap<String, String>() {{
            put("p1", "p1");
            put("p2", "p2");
        }};
        //格式化文本，使用 {varName} 占位
        System.out.println(StrFormatUtil.format("param1:{p1},param2:{p2}", map));
        map = new HashMap<String, String>() {{
            put("p1", "p1");
            put("p2", null);
        }};
        System.out.println(StrFormatUtil.format("param1:{p1},param2:{p2}", map, false));
        System.out.println(StrFormatUtil.format("param1:{p1},param2:{p2}", map, true));
    }
}
