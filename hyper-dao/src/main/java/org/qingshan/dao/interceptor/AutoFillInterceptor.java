package org.qingshan.dao.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.qingshan.common.service.BaseServiceImpl;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

@Component
@Slf4j
@Intercepts(@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}))
public class AutoFillInterceptor extends BaseServiceImpl implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws IllegalAccessException, InvocationTargetException {
        fillField(invocation);
        return invocation.proceed();
    }

    private void fillField(Invocation invocation) {
        //准备外部数据
        String currentUser = getCurrentUserName();
        currentUser = StringUtils.isNotBlank(currentUser) ? currentUser : "ANON";
        Long nowTime = System.currentTimeMillis();

        Object[] args = invocation.getArgs();
        SqlCommandType sqlCommandType = null;
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            //第一个参数处理。根据它判断是否给“操作属性”赋值。
            if (arg instanceof MappedStatement) {//如果是第一个参数 MappedStatement
                MappedStatement ms = (MappedStatement) arg;
                sqlCommandType = ms.getSqlCommandType();
                if (sqlCommandType == SqlCommandType.INSERT || sqlCommandType == SqlCommandType.UPDATE) {//如果是“增加”或“更新”操作，则继续进行默认操作信息赋值。否则，则退出
                    continue;
                } else {
                    break;
                }
            }

            if (sqlCommandType == SqlCommandType.INSERT) {
                Class tempClass = arg.getClass();
                while (tempClass != null) {
                    for (Field f : tempClass.getDeclaredFields()) {
                        f.setAccessible(true);
                        switch (f.getName()) {
                            case "createdBy":
                                setProperty(arg, "createdBy", currentUser);
                                break;
                            case "createdDate":
                                setProperty(arg, "createdDate", nowTime);
                                break;
                            case "lastModifiedBy":
                                setProperty(arg, "lastModifiedBy", currentUser);
                                break;
                            case "lastModifiedDate":
                                setProperty(arg, "lastModifiedDate", nowTime);
                                break;
                        }
                    }
                    tempClass = tempClass.getSuperclass();
                }
            } else if (sqlCommandType == SqlCommandType.UPDATE) {
                Class tempClass = arg.getClass();
                while (tempClass != null) {
                    for (Field f : arg.getClass().getDeclaredFields()) {
                        f.setAccessible(true);
                        switch (f.getName()) {
                            case "lastModifiedBy":
                                setProperty(arg, "lastModifiedBy", currentUser);
                                break;
                            case "lastModifiedDate":
                                setProperty(arg, "lastModifiedDate", nowTime);
                                break;
                        }
                    }
                    tempClass = tempClass.getSuperclass();
                }
            }
        }
    }

    /**
     * 为对象的操作属性赋值
     *
     * @param bean
     */
    private void setProperty(Object bean, String name, Object value) {
        try {
            //根据需要，将相关属性赋上默认值
            BeanUtils.setProperty(bean, name, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
