package com.ask.sample.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

public final class ResponseUtils {

  public static void writeJson(HttpServletResponse response, Object obj) throws IOException {
    response.setCharacterEncoding("UTF-8");
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
    response.getWriter().write(writer.writeValueAsString(obj));
    response.flushBuffer();
  }
}
