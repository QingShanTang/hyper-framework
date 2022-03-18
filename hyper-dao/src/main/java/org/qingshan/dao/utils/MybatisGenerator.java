package org.qingshan.dao.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * mybatisGenerator
 */
@Slf4j
public class MybatisGenerator {
    @SneakyThrows
    public static void main(String[] args) {
        //MBG执行过程中的警告信息
        List<String> warnings = new ArrayList<String>();
        //当生成的代码重复时，覆盖原代码
        boolean overwrite = true;
        //读取MBG配置文件
        InputStream is = null;
        try {
            is = MybatisGenerator.class.getClassLoader().getResourceAsStream("mybatis-generator-customize-config.xml");
            ConfigurationParser cp = new ConfigurationParser(warnings);
            Configuration config = cp.parseConfiguration(is);
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            //执行生成代码
            myBatisGenerator.generate(null);
            //输出警告信息
            for (String warning : warnings) {
                log.warn(warning);
            }
        } catch (Exception e) {
            log.error("执行异常,errorMsg->\n{}", e);
        } finally {
            if (null != is) {
                is.close();
            }
        }
    }
}
