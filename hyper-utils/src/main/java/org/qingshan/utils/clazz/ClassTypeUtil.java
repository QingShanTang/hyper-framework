package org.qingshan.utils.clazz;

import lombok.extern.slf4j.Slf4j;
import org.qingshan.pojo.web.MockCallResult;
import org.qingshan.utils.json.JSONUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
public class ClassTypeUtil {
    /**
     * 记录已解析类型,防止循环引用导致栈溢出
     */
    private static final ThreadLocal<Map<String, Map<String, Object>>> resolvedTypeMap = new ThreadLocal<>();
    /**
     * 基本类型
     */
    private static final Map<String, Class> baseTypeMap = getBaseTypeMap();
    /**
     * 集合类型
     */
    private static final Set<String> collectionTypeSet = getCollectionTypeSet();
    /**
     * map类型
     */
    private static final Set<String> mapTypeSet = getMapTypeSet();

    /**
     * 递归获取class全字段类型信息
     *
     * @param clazz
     * @return
     */
    public static Map<String, Object> getClazzTypeInfo(Class<?> clazz) {
        resolvedTypeMap.set(new HashMap<>());
        Map<String, Object> fieldInfo = getFieldInfo(clazz);
        resolvedTypeMap.remove();
        return fieldInfo;
    }

    private static Map<String, Object> getFieldInfo(Class<?> clazz) {
        if (null != resolvedTypeMap.get().get(clazz.getName())) {
            return resolvedTypeMap.get().get(clazz.getName());
        }
        HashMap<String, Object> map = new HashMap<>();
        resolvedTypeMap.get().put(clazz.getName(), map);
        Field[] fields = clazz.getDeclaredFields();
        map.put("fullPathClassName", clazz.getName());
        for (Field field : fields) {
            Class<?> type = field.getType();
            String fieldName = field.getName();
            if (isCollectionType(type)) {
                // 集合
                map.put(fieldName, getCollectionType(field));
            } else if (type.isArray()) {
                // 数组，仅支持一维数组
                map.put(fieldName, getArrayType(field));
            } else if (isMapType(type)) {
                // map
                map.put(fieldName, getMapType(field));
            } else {
                // 基础类型或自定义
                map.put(fieldName, getValueType(type));
            }
        }
        return map;
    }

    /**
     * 数组，仅支持一维数组
     */
    private static List<Object> getArrayType(Field field) {
        Class<?> type = field.getType();
        String typeName = type.getTypeName();
        //元素类型
        String elementTypeName = typeName.replaceAll("\\[]", "");
        //数组维度
        int dimension = (typeName.length() - elementTypeName.length()) / 2;
        if (dimension == 1) {
            try {
                Class<?> baseClass = isBaseType(elementTypeName);
                if (baseClass != null) {
                    // 基础类型 Class.forName获取不到
                    return getCollectionType(baseClass);
                }
                return getCollectionType(Class.forName(elementTypeName));
            } catch (ClassNotFoundException e) {
                log.error("getArrayType fail.", e);
            }
            return getCollectionType(Object.class);
        } else {
            log.warn("Multidimensional arrays are not supported! type: {} , fieldName: {}", typeName, field.getName());
            return Collections.singletonList(typeName);
        }
    }

    private static List<Object> getCollectionType(Class<?> clazz) {
        return Collections.singletonList(getValueType(clazz));
    }

    private static List<Object> getCollectionType(Field field) {
        Class<?> actualTypeClazz;
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType
                && ((ParameterizedType) genericType).getActualTypeArguments()[0] instanceof Class) {
            // 指定了具体泛型才能转换
            ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
            actualTypeClazz = (Class<?>) parameterizedType.getActualTypeArguments()[0];
        } else {
            actualTypeClazz = Object.class;
        }
        return getCollectionType(actualTypeClazz);
    }

    private static Map<String, Object> getMapType(Field field) {
        HashMap<String, Object> mapTypeMap = new HashMap<>();
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            // 指定了具体泛型才能转换
            //如果泛型里面嵌套泛型，那么仅解析第一层
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            if (((ParameterizedType) genericType).getActualTypeArguments()[0] instanceof Class) {
                Class<?> keyClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                mapTypeMap.put("mapKey", getValueType(keyClass));
            } else {
                mapTypeMap.put("mapKey", parameterizedType.getActualTypeArguments()[0].getTypeName());
            }
            if (((ParameterizedType) genericType).getActualTypeArguments()[1] instanceof Class) {
                Class<?> valueClass = (Class<?>) parameterizedType.getActualTypeArguments()[1];
                mapTypeMap.put("mapValue", getValueType(valueClass));
            } else {
                mapTypeMap.put("mapValue", parameterizedType.getActualTypeArguments()[1].getTypeName());
            }
        } else {
            mapTypeMap.put("mapKey", Object.class.getName());
            mapTypeMap.put("mapValue", Object.class.getName());
        }
        return mapTypeMap;
    }

    private static Object getValueType(Class<?> clazz) {
        if (isBaseType(clazz)) {
            return clazz.getTypeName();
        }
        return getFieldInfo(clazz);
    }

    private static boolean isMapType(Class<?> clazz) {
        return mapTypeSet.contains(clazz.getName());
    }

    private static boolean isCollectionType(Class<?> clazz) {
        return collectionTypeSet.contains(clazz.getName());
    }

    private static Class isBaseType(String clazzType) {
        return baseTypeMap.get(clazzType);
    }

    private static boolean isBaseType(Class<?> clazz) {
        return null != baseTypeMap.get(clazz.getName());
    }

    private static Map<String, Class> getBaseTypeMap() {
        return new HashMap<String, Class>() {{
            put(byte.class.getName(), byte.class);
            put(short.class.getName(), short.class);
            put(int.class.getName(), int.class);
            put(long.class.getName(), long.class);
            put(float.class.getName(), float.class);
            put(double.class.getName(), double.class);
            put(boolean.class.getName(), boolean.class);
            put(char.class.getName(), char.class);
            put(Byte.class.getName(), Byte.class);
            put(Short.class.getName(), Short.class);
            put(Integer.class.getName(), Integer.class);
            put(Long.class.getName(), Long.class);
            put(Float.class.getName(), Float.class);
            put(Double.class.getName(), Double.class);
            put(Boolean.class.getName(), Boolean.class);
            put(String.class.getName(), String.class);
            put(Character.class.getName(), Character.class);
            put(Object.class.getName(), Object.class);
            put(BigDecimal.class.getName(), BigDecimal.class);
            put(Date.class.getName(), Date.class);
        }};
    }

    private static Set<String> getCollectionTypeSet() {
        String[] types = {List.class.getName(), Set.class.getName(), ArrayList.class.getName(), HashSet.class.getName()};
        return new HashSet<>(Arrays.asList(types));
    }

    private static Set<String> getMapTypeSet() {
        String[] types = {Map.class.getName(), HashMap.class.getName()};
        return new HashSet<>(Arrays.asList(types));
    }

    public static void main(String[] args) {
        //如果存在循环引用打印的时候会栈溢出，需要用fastjson打印
        JSONUtil.printJSONStringWithFormat(getClazzTypeInfo(MockCallResult.class));
    }
}

