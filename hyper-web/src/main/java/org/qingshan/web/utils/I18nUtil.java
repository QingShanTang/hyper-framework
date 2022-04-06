package org.qingshan.web.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class I18nUtil {
    @Autowired
    private MessageSource messageSource;

    @Value(value = "${spring.messages.basename}")
    private String basename;

    public String getMessage(String messageKey) {
        String[] basenames = basename.split(",");
        String message = messageSource.getMessage(messageKey, basenames, LocaleContextHolder.getLocale());
        return message;
    }
}

