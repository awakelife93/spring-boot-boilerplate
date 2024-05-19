package com.example.demo.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConvertUtils {

  public static LocalDateTime convertStringToLocalDateTimeFormat(
    String time,
    String pattern
  ) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    LocalDateTime localDateTime = LocalDateTime.parse(time, formatter);

    return localDateTime;
  }

  public static String convertLocalDateTimeToStringFormat(
    LocalDateTime time,
    String pattern
  ) {
    return time.format(DateTimeFormatter.ofPattern(pattern));
  }
}
