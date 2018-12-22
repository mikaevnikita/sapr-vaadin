package ru.mikaev.sapr.mapping;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CreationMapper {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String creationToString(LocalDateTime creation) {
        return formatter.format(creation);
    }
}
