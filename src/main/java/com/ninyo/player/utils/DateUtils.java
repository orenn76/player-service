package com.ninyo.player.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
public class DateUtils {

    private static final List<String> FORMAT_STRINGS = List.of("dd/MM/yyyy", "yyyy-MM-dd");

    public static LocalDate parseDate(String dateString) {
        if (ObjectUtils.isEmpty(dateString)) {
            return null;
        }
        for (String formatString : FORMAT_STRINGS) {
            try {
                return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(formatString));
            } catch (DateTimeParseException e) {
                log.debug("Could not parse '{}' as LocalDate", dateString, e);
            }
        }
        return null;
    }

}
