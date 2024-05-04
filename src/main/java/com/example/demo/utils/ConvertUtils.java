package com.example.demo.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
public class ConvertUtils {

  public static LocalDateTime convertStringToLocalDateTime(String time) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd HH:mm:ss.SSSSSS"
    );
    LocalDateTime localDateTime = LocalDateTime.parse(time, formatter);

    return localDateTime;
  }
}
