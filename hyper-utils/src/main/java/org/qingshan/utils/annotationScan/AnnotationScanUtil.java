package org.qingshan.utils.annotationScan;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;


/**
 * 注解扫描工具类
 */
@Slf4j
public class AnnotationScanUtil {

    /**
     * 扫描
     *
     * @param annoClazz
     * @param excludeResources
     * @param basePackages
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T extends Annotation> List<AnnotationTarget<T>> scan(
            Class<T> annoClazz,
            String[] excludeResources,
            String... basePackages
    ) {
        //获取注解的elementType
        List<ElementType> elementTypeList = getElementType(annoClazz);
        List<AnnotationTarget<T>> targetList = new ArrayList<>();

        excludeResources = ArrayUtils.isEmpty(excludeResources) ? new String[]{} : Stream.of(excludeResources).filter(item -> {
            return StringUtils.isNotBlank(item);
        }).map(item -> {
            return item.trim();
        }).toArray(String[]::new);
        basePackages = ArrayUtils.isEmpty(basePackages) ? new String[]{} : Stream.of(basePackages).filter(item -> {
            return StringUtils.isNotBlank(item);
        }).map(item -> {
            return item.trim();
        }).toArray(String[]::new);

        if (CollectionUtils.isEmpty(elementTypeList) || ArrayUtils.isEmpty(basePackages)) {
            return targetList;
        }

        for (String basePackage : basePackages
        ) {
            FilterBuilder filterBuilder = new FilterBuilder();
            filterBuilder.includePackage(basePackage);
            List<String> excludeResourcesRegexList = getExcludeResourcesRegexList(excludeResources);
            if (CollectionUtils.isNotEmpty(excludeResourcesRegexList)) {
                excludeResourcesRegexList.forEach(item -> {
                    filterBuilder.exclude(item);
                });
            }

            Reflections reflections = new Reflections(
                    new ConfigurationBuilder()
                            .addUrls(ClasspathHelper.forPackage(basePackage))
                            .setScanners(new SubTypesScanner(), new TypeAnnotationsScanner(), new MethodAnnotationsScanner(), new FieldAnnotationsScanner())
                            .filterInputsBy(filterBuilder));

            if (elementTypeList.contains(ElementType.PACKAGE)) {
                Set<Class<?>> resources = reflections.getTypesAnnotatedWith(annoClazz);
                for (Class resource : resources
                ) {
                    targetList.addAll(scanPackage(annoClazz, resource));
                }
            }
            if (elementTypeList.contains(ElementType.TYPE)) {
                Set<Class<?>> resources = reflections.getTypesAnnotatedWith(annoClazz);
                for (Class resource : resources
                ) {
                    targetList.addAll(scanType(annoClazz, resource));
                }
            }
            if (elementTypeList.contains(ElementType.METHOD)) {
                Set<Method> resources = reflections.getMethodsAnnotatedWith(annoClazz);
                for (Method resource : resources
                ) {
                    targetList.addAll(scanMethod(annoClazz, resource));
                }
            }

            if (elementTypeList.contains(ElementType.FIELD)) {
                Set<Field> resources = reflections.getFieldsAnnotatedWith(annoClazz);
                for (Field resource : resources
                ) {
                    targetList.addAll(scanField(annoClazz, resource));
                }
            }
        }

        return targetList;
    }

    private static List<String> getExcludeResourcesRegexList(String[] excludeResources) {
        List<String> regexList = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(excludeResources)) {
            Arrays.stream(excludeResources).forEach(item -> {
                regexList.add(item.replace(".", "\\.") + ".*");
            });
        }
        return regexList;
    }


    /**
     * 从注解class中获取elementType
     *
     * @param t
     * @param <T>
     * @return
     */
    private static <T extends Annotation> List<ElementType> getElementType(Class<T> t) {
        Target target = t.getDeclaredAnnotation(Target.class);
        if (null == target) {
            return new ArrayList<>();
        }
        ElementType[] elementTypes = target.value();
        return elementTypes.length == 0 ? new ArrayList<>() : Arrays.asList(elementTypes);
    }


    /**
     * 扫描包
     *
     * @param annoClazz
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T extends Annotation> List<AnnotationTarget<T>> scanPackage(
            Class<T> annoClazz,
            Class clazz) {
        List<AnnotationTarget<T>> targetList = new ArrayList<>();
        T[] annotations = clazz.getPackage().getDeclaredAnnotationsByType(annoClazz);
        for (T annotation : annotations
        ) {
            AnnotationTarget<T> target = new AnnotationTarget<>();
            target.setElementType(ElementType.PACKAGE);
            target.setPkg(clazz.getPackage());
            target.setAnnotation(annotation);
            targetList.add(target);
        }
        return targetList;
    }

    /**
     * 扫描类
     *
     * @param annoClazz
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T extends Annotation> List<AnnotationTarget<T>> scanType(
            Class<T> annoClazz,
            Class clazz) {
        List<AnnotationTarget<T>> targetList = new ArrayList<>();

        //类上注解
        T[] annotations = (T[]) clazz.getDeclaredAnnotationsByType(annoClazz);
        for (T annotation : annotations
        ) {
            AnnotationTarget<T> target = new AnnotationTarget<>();
            target.setElementType(ElementType.TYPE);
            target.setClazz(clazz);
            target.setAnnotation(annotation);
            targetList.add(target);
        }

        return targetList;
    }


    /**
     * 扫描方法
     *
     * @param annoClazz
     * @param method
     * @param <T>
     * @return
     */
    private static <T extends Annotation> List<AnnotationTarget<T>> scanMethod(
            Class<T> annoClazz,
            Method method) {
        List<AnnotationTarget<T>> targetList = new ArrayList<>();

        //方法上注解
        T[] annotations = method.getDeclaredAnnotationsByType(annoClazz);
        for (T annotation : annotations
        ) {
            AnnotationTarget<T> target = new AnnotationTarget<>();
            target.setElementType(ElementType.METHOD);
            target.setClazz(method.getDeclaringClass());
            target.setMethod(method);
            target.setAnnotation(annotation);
            targetList.add(target);
        }

        return targetList;
    }

    /**
     * 扫描字段
     *
     * @param annoClazz
     * @param field
     * @param <T>
     * @return
     */
    private static <T extends Annotation> List<AnnotationTarget<T>> scanField(
            Class<T> annoClazz,
            Field field) {
        List<AnnotationTarget<T>> targetList = new ArrayList<>();

        //字段上注解
        T[] annotations = field.getDeclaredAnnotationsByType(annoClazz);
        for (T annotation : annotations
        ) {
            AnnotationTarget<T> target = new AnnotationTarget<>();
            target.setElementType(ElementType.FIELD);
            target.setClazz(field.getDeclaringClass());
            target.setField(field);
            target.setAnnotation(annotation);
            targetList.add(target);
        }

        return targetList;
    }
}
