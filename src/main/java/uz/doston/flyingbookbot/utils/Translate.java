package uz.doston.flyingbookbot.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Locale;
import java.util.Objects;

@Component
public final class Translate {

    @Resource
    MessageSource messageSource;

    public String getTranslation(String code, String language) {
        Locale locale = Locale.forLanguageTag(language);
        return getTranslation(code, locale);
    }

    public String getTranslation(String code, Locale locale) {
        return getTranslation(code, locale, (Object) null);
    }

    public String getTranslation(String code, Locale locale, Object... args) {
        return messageSource.getMessage(code, args, locale);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Translate) obj;
        return Objects.equals(this.messageSource, that.messageSource);
    }

    @Override
    public String toString() {
        return "Translate[" +
                "messageSource=" + messageSource + ']';
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageSource);
    }

}
