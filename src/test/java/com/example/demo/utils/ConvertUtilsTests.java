package com.example.demo.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Tag("unit-test")
@DisplayName("Unit - Convert Utils Test")
@ExtendWith(MockitoExtension.class)
public class ConvertUtilsTests {

  private final String defaultWrongPattern = "wrong_pattern";

  @Nested
  @DisplayName("Convert String to LocalDateTime Test")
  class ConvertStringToLocalDateTimeFormatTest {

    @Test
    @DisplayName(
      "Convert current string datetime & current pattern to LocalDateTime"
    )
    public void should_AssertLocalDateTime_when_GivenCurrentStringDateTimeAndCurrentPattern() {
      String stringDateTime = LocalDateTime.now().withNano(0).toString();

      LocalDateTime localDateTime = ConvertUtils.convertStringToLocalDateTimeFormat(
        stringDateTime,
        "yyyy-MM-dd'T'HH:mm:ss"
      );

      assertEquals(localDateTime.getClass(), LocalDateTime.class);
    }

    @Test
    @DisplayName(
      "Failed Convert current string datetime & wrong pattern to LocalDateTime"
    )
    public void should_AssertIllegalArgumentException_when_GivenCurrentStringDateTimeAndWrongPattern() {
      assertThrows(
        IllegalArgumentException.class,
        () ->
          ConvertUtils.convertStringToLocalDateTimeFormat(
            LocalDateTime.now().toString(),
            defaultWrongPattern
          )
      );
    }

    @Test
    @DisplayName(
      "Failed Convert blank string datetime & current pattern to LocalDateTime"
    )
    public void should_AssertDateTimeParseException_when_GivenBlankStringDateTimeAndCurrentPattern() {
      assertThrows(
        DateTimeParseException.class,
        () ->
          ConvertUtils.convertStringToLocalDateTimeFormat(
            "",
            "yyyy-MM-dd'T'HH:mm:ss"
          )
      );
    }
  }

  @Nested
  @DisplayName("Convert LocalDateTime to String Test")
  class ConvertLocalDateTimeToStringFormatTest {

    @Test
    @DisplayName(
      "Convert current local datetime & current pattern to string datetime"
    )
    public void should_AssertStringDateTime_when_GivenCurrentLocalDateTimeAndCurrentPattern() {
      String stringDateTime = ConvertUtils.convertLocalDateTimeToStringFormat(
        LocalDateTime.now().withNano(0),
        "yyyy-MM-dd'T'HH:mm:ss"
      );

      assertEquals(stringDateTime.getClass(), String.class);
    }

    @Test
    @DisplayName(
      "Failed Convert Current local datetime & wrong pattern to string datetime"
    )
    public void should_AssertIllegalArgumentException_when_GivenWrongLocalDateTimeOrWrongPattern() {
      assertThrows(
        IllegalArgumentException.class,
        () ->
          ConvertUtils.convertLocalDateTimeToStringFormat(
            LocalDateTime.now(),
            defaultWrongPattern
          )
      );
    }
  }
}
