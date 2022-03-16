package org.qingshan.utils.validate;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 参数校验器
 */
public class ValidatorUtil {
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> ValidateResult validator(T t, DefaultGroupSequenceProvider<T> provider) {
        if (null == t) {
            return new ValidateResult(false,
                    Arrays.asList("The object to be validated must not be null."));
        }
        List<String> errorMsgList = new ArrayList<>();

        Set<ConstraintViolation<T>> violations;
        if (null != provider) {
            //获取校验组
            Class[] clazzArr = provider.getValidationGroups(t).toArray(new Class[0]);
            violations = validator.validate(t, clazzArr);
        } else {
            violations = validator.validate(t);
        }

        if (violations.size() > 0) {
            violations.stream().map(v -> v.getPropertyPath() + v.getMessage() + ": " + v.getInvalidValue()).forEach(item -> {
                errorMsgList.add(item);
            });
            return new ValidateResult(false, errorMsgList);
        } else {
            return new ValidateResult(true, errorMsgList);
        }
    }

    public static <T> ValidateResult validator(T t) {
        return validator(t, null);
    }
}
