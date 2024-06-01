package com.example.demo.utils;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Tag("unit-test")
@DisplayName("Unit - Security Utils Test")
@ExtendWith(MockitoExtension.class)
public class SecurityUtilsTest {

  @Mock
  MockHttpServletRequest mockHttpServletRequest;

  @Mock
  MockHttpServletResponse mockHttpServletResponse;

  @Test
  @DisplayName("Send Error Response Test")
  public void should_VerifyCallMethodsOfHttpServletResponse_when_GivenServletAndException()
    throws Exception {
    Exception exception = Instancio.create(Exception.class);
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);

    when(mockHttpServletResponse.getWriter()).thenReturn(printWriter);

    SecurityUtils.sendErrorResponse(
      mockHttpServletRequest,
      mockHttpServletResponse,
      exception,
      "test exception"
    );

    verify(mockHttpServletResponse, times(1)).setStatus(anyInt());
    verify(mockHttpServletResponse, times(1)).setContentType(anyString());
    verify(mockHttpServletResponse, times(1)).getWriter();
  }
}
