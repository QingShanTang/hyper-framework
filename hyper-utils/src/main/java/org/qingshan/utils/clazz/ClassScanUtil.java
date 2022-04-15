package org.qingshan.utils.clazz;

import cn.hutool.core.util.ReUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * 根据类名扫描
 */
@Slf4j
public class ClassScanUtil {
    public static List<Class> scan(
            String className,
            boolean fuzzy,
            String[] excludeResources,
            String... basePackages
    ) {
        List<Class> targetList = new ArrayList<>();

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

        if (StringUtils.isBlank(className) || ArrayUtils.isEmpty(basePackages)) {
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
                            .setScanners(new SubTypesScanner(false))
                            .filterInputsBy(filterBuilder));

            Set<Class<?>> scanedClass = reflections.getSubTypesOf(Object.class);
            scanedClass.forEach(item -> {
                String regex;
                if (fuzzy) {
                    regex = "^.+\\.[^.]*{0}[^.]*$";
                } else {
                    regex = "^.+\\.{0}$";
                }

                if (ReUtil.isMatch(MessageFormat.format(regex, className), item.getTypeName())) {
                    targetList.add(item);
                }
            });
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

    public static void main(String[] args) {
        ClassScanUtil.scan("ClassTypeUtil", false, null, "org.qingshan");
    }
}
