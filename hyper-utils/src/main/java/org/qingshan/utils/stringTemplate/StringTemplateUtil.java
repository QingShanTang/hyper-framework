package org.qingshan.utils.stringTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.qingshan.utils.json.JSONUtil;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串模板加载工具类
 */
@Slf4j
public class StringTemplateUtil {

    /**
     * 加载
     *
     * @param path
     * @return
     * @throws DocumentException
     */
    public static Map<String, String> load(String path) throws Exception {
        log.info("加载字符串模板。。。");
        log.info("path->{}", path);
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isNotBlank(path)) {
            //取消!DOCTYPE验证
            SAXReader reader = new SAXReader(false);
            reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            //加载xml
            InputStream inputStream = StringTemplateUtil.class.getClassLoader().getResourceAsStream(path);
            Document document = reader.read(inputStream);
            //获取根节点
            Element rootElement = document.getRootElement();
            Iterator<Element> iterator = rootElement.elements("sql").iterator();
            while (iterator.hasNext()) {
                Element stu = iterator.next();
                if (null != stu.attribute("id") && StringUtils.isNotBlank(stu.attribute("id").getValue())) {
                    String id = stu.attribute("id").getValue();
                    String template = null == stu.getStringValue() ? null : stu.getStringValue().trim().replaceAll(" +", " ").replace("\n", "");
                    if (StringUtils.isNotBlank(template)) {
                        map.put(id, template);
                    }
                }
            }
        } else {
            log.warn("路径为空!");
        }
        log.info("map->{}", JSONUtil.toJSONString(map));
        return map;
    }

    /**
     * 批量加载
     *
     * @param pathList
     * @return
     * @throws DocumentException
     */
    public static Map<String, String> load(List<String> pathList) throws Exception {
        Map<String, String> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(pathList)) {
            for (String path : pathList
            ) {
                map.putAll(load(path));
            }
        }
        return map;
    }


    /**
     * 对象转map
     *
     * @param obj
     * @return
     */
    private static Map<String, Object> objToMap(Object obj) {
        Map<String, Object> map = JSONUtil.parseObject(JSONUtil.toJSONString(obj), new TypeReference<Map<String, Object>>() {
        });
        return null == map ? new HashMap<>() : map;
    }

    /**
     * 填充字符串模板中参数
     *
     * @param template
     * @param paramsObj
     * @return
     */
    public static String fill(String template, Object paramsObj) throws Exception {
        //将参数解析为map
        Map<String, Object> params = objToMap(paramsObj);

        //获取以 #{ 开始，不是 } 结尾的多个字符 (其中()用来分组)
        Set<String> templateParams = parseTempParams(template, "#\\{([^}]+)");

        //替换#{xxx}，其中xxx是参数
        for (String templateParam : templateParams
        ) {
            if (null == params.get(templateParam)) {
                template = template.replaceAll("#\\{" + templateParam + "\\}", "");
            } else {
                template = template.replaceAll("#\\{" + templateParam + "\\}", params.get(templateParam).toString());
            }
        }

        return template;
    }

    /**
     * 解析模板参数
     *
     * @param template
     * @param regex
     * @return
     * @throws Exception
     */
    public static Set<String> parseTempParams(String template, String regex) throws Exception {
        if (StringUtils.isBlank(template)) {
            throw new Exception("模板不可为空!");
        }
        Set<String> templateParams = new HashSet<>();

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(template);

        //获取匹配的内容
        while (m.find()) {
            templateParams.add(m.group(1));
        }
        return templateParams;
    }

    /**
     * 根据id获取模板
     *
     * @param templateMap
     * @param id
     * @return
     */
    public static String getTemplate(Map<String, String> templateMap, String id) throws Exception {
        if (MapUtils.isNotEmpty(templateMap) && StringUtils.isNotBlank(templateMap.get(id))) {
            return templateMap.get(id);
        } else {
            throw new Exception("该模板未配置,id->" + id);
        }
    }

    /**
     * 根据id获取模板并填充
     *
     * @param templateMap
     * @param id
     * @param paramsObj
     * @return
     */
    public static String getTemplateAndFill(Map<String, String> templateMap, String id, Object paramsObj) throws Exception {
        return fill(getTemplate(templateMap, id), paramsObj);
    }
}
