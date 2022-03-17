package org.qingshan.utils.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSONUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public static String toJSONString(Object o) {
        return objectMapper.writeValueAsString(o);
    }

    @SneakyThrows
    public static String toJSONStringWithFormat(Object o) {
        return "\n" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
    }

    @SneakyThrows
    public static void printJSONStringWithFormat(Object o) {
        log.info(toJSONStringWithFormat(o));
    }

    @SneakyThrows
    public static <T> T parseObject(String text, Class<T> clazz) {
        return objectMapper.readValue(text, clazz);
    }

    @SneakyThrows
    public static <T> T parseObject(String text, TypeReference<T> type) {
        return objectMapper.readValue(text, type);
    }

}
