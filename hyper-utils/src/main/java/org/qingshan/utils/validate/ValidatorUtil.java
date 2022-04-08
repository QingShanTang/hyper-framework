package org.qingshan.utils.validate;

import cn.hutool.core.util.StrUtil;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 校验器
 */
public class ValidatorUtil {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> ValidateResult validator(T t, DefaultGroupSequenceProvider<T> provider) {
        if (null == t) {
            return new ValidateResult(false,
                    "The object to be validated is null.");
        }

        List<String> msgs = new ArrayList<>();

        Set<ConstraintViolation<T>> violations;
        if (null != provider) {
            //获取校验组
            Class[] clazzArr = provider.getValidationGroups(t).toArray(new Class[0]);
            violations = validator.validate(t, clazzArr);
        } else {
            violations = validator.validate(t);
        }

        if (violations.size() > 0) {
            violations.stream().map(v -> StrUtil.format("{}{} -> {}", v.getPropertyPath(), v.getMessage(), v.getInvalidValue())).forEach(item -> {
                msgs.add(item);
            });
            return new ValidateResult(false, msgs);
        } else {
            return new ValidateResult(true, msgs);
        }
    }

    public static <T> ValidateResult validator(T t) {
        return validator(t, null);
    }
}
