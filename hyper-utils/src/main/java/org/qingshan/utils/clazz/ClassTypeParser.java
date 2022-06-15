package org.qingshan.utils.clazz;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 类信息
 * java类字段信息解析工具
 */
@Slf4j
public class ClassTypeParser {
    /**
     * 记录已解析类型,防止循环引用导致栈溢出
     */
    private final Map<Class, Object> resolvedTypeMap = new HashMap<>();

    /**
     * 解析结果
     */
    private Object typeInfo;
    /**
     * 基本类型
     */
    private static final Map<String, Class> baseTypeMap = getBaseTypeMap();
    /**
     * 集合类型
     */
    private static final Set<Class> collectionTypeSet = getCollectionTypeSet();
    /**
     * map类型
     */
    private static final Set<Class> mapTypeSet = getMapTypeSet();

    public ClassTypeParser(Class<?> clazz) {
        this.typeInfo = getTypeInfo(clazz);
    }

    public Map<Class, Object> getResolvedTypeMap() {
        return resolvedTypeMap;
    }

    public Object getTypeInfo() {
        return typeInfo;
    }

    /**
     * 获取类型的信息
     *
     * @param clazz
     * @return
     */
    private Object getTypeInfo(Class<?> clazz) {
        if (null != resolvedTypeMap.get(clazz)) {
            return resolvedTypeMap.get(clazz);
        }

        HashMap<String, Object> map = new HashMap<>();

        //如果是基础类型，那么直接返回该类型信息,否则解析类型内部信息
        if (isBaseType(clazz)) {
            resolvedTypeMap.put(clazz, clazz.getTypeName());
            return clazz.getTypeName();
        } else {
            resolvedTypeMap.put(clazz, map);
            return getFieldsInfo(clazz, map);
        }
    }


    /**
     * 获取字段类型信息
     *
     * @param clazz
     * @return
     */
    private Map<String, Object> getFieldsInfo(Class<?> clazz, HashMap<String, Object> map) {
        Field[] fields = clazz.getDeclaredFields();
        map.put("this", clazz.getName());
        if (isBaseType(clazz)) {
            return map;
        }
        for (Field field : fields) {
            Class<?> type = field.getType();
            String fieldName = field.getName();
            if (type.isArray()) {
                // 数组，仅支持一维数组
                map.put(fieldName, getArrayType(field));
            } else if (isCollectionType(type)) {
                // 集合
                getTypeInfo(field.getType());
                map.put(fieldName, getCollectionType(field));
            } else if (isMapType(type)) {
                // map
                getTypeInfo(field.getType());
                map.put(fieldName, getMapType(field));
            } else {
                // 基础类型或自定义
                map.put(fieldName, getTypeInfo(type));
            }
        }
        return map;
    }

    /**
     * 数组，仅支持一维数组
     */
    private List<Object> getArrayType(Field field) {
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
                log.error("getArrayType fail,errorMsg->", e);
                return getCollectionType(Object.class);
            }
        } else {
            log.warn("Multidimensional arrays are not supported! className: {},fieldName: {},type: {}", field.getDeclaringClass().getTypeName(), field.getName(), typeName);
            return Collections.singletonList(typeName);
        }
    }

    private List<Object> getCollectionType(Class<?> clazz) {
        return Collections.singletonList(getTypeInfo(clazz));
    }

    /**
     * 集合
     *
     * @param field
     * @return
     */
    private List<Object> getCollectionType(Field field) {
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

    /**
     * Map
     *
     * @param field
     * @return
     */
    private Map<String, Object> getMapType(Field field) {
        HashMap<String, Object> mapTypeMap = new HashMap<>();
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            // 指定了具体泛型才能转换
            //如果泛型里面嵌套泛型，那么仅解析第一层
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            if (((ParameterizedType) genericType).getActualTypeArguments()[0] instanceof Class) {
                Class<?> keyClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                mapTypeMap.put("mapKey", getTypeInfo(keyClass));
            } else {
                mapTypeMap.put("mapKey", parameterizedType.getActualTypeArguments()[0].getTypeName());
            }
            if (((ParameterizedType) genericType).getActualTypeArguments()[1] instanceof Class) {
                Class<?> valueClass = (Class<?>) parameterizedType.getActualTypeArguments()[1];
                mapTypeMap.put("mapValue", getTypeInfo(valueClass));
            } else {
                mapTypeMap.put("mapValue", parameterizedType.getActualTypeArguments()[1].getTypeName());
            }
        } else {
            mapTypeMap.put("mapKey", Object.class.getName());
            mapTypeMap.put("mapValue", Object.class.getName());
        }
        return mapTypeMap;
    }

    private static boolean isMapType(Class<?> clazz) {
        return mapTypeSet.contains(clazz);
    }

    private static boolean isCollectionType(Class<?> clazz) {
        return collectionTypeSet.contains(clazz);
    }

    private static Class isBaseType(String clazzType) {
        return baseTypeMap.get(clazzType);
    }

    private static boolean isBaseType(Class<?> clazz) {
        return null != baseTypeMap.get(clazz.getName());
    }

    /**
     * 获取基本类型列表
     *
     * @return
     */
    private static Map<String, Class> getBaseTypeMap() {
        Class[] types = {
                byte.class, short.class, int.class, long.class, float.class, double.class, boolean.class, char.class,
                Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class, String.class,
                Character.class, Object.class, BigDecimal.class, Date.class
        };
        return Arrays.asList(types).stream().collect(Collectors.toMap(Class::getName, Function.identity(), (key1, key2) -> key2));
    }

    /**
     * 获取集合类型列表
     *
     * @return
     */
    private static Set<Class> getCollectionTypeSet() {
        Class[] types = {List.class, Set.class, ArrayList.class, HashSet.class};
        return Arrays.asList(types).stream().collect(Collectors.toSet());
    }

    /**
     * 获取map类型列表
     *
     * @return
     */
    private static Set<Class> getMapTypeSet() {
        Class[] types = {Map.class, HashMap.class};
        return Arrays.asList(types).stream().collect(Collectors.toSet());
    }
}

