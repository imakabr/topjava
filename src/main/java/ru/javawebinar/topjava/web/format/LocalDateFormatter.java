package ru.javawebinar.topjava.web.format;

import org.springframework.format.Formatter;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

public class LocalDateFormatter implements Formatter<LocalDate> {

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        return DateTimeUtil.parseLocalDate(text);
    }

    @Override
    public String print(LocalDate a, Locale l) {
        return a.toString();
    }
}
