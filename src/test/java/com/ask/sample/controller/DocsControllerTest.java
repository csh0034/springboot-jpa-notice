package com.ask.sample.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ask.sample.common.ControllerSupportTest;
import com.ask.sample.restdocs.util.CustomResponseFieldsSnippet;
import com.ask.sample.constant.ResponseCode;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

class DocsControllerTest extends ControllerSupportTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void docs() throws Exception {
    // when
    ResultActions result = mockMvc.perform(get("/docs")
        .accept(MediaType.APPLICATION_JSON));

    // then
    result.andExpect(status().isOk())
        .andDo(document("common",
            customResponseFields("custom-response",
                beneathPath("result.responseCodes").withSubsectionId("responseCodes"),
                enumConvertFieldDescriptor(ResponseCode.values())
            )
        ));
  }

  private FieldDescriptor[] enumConvertFieldDescriptor(ResponseCode[] enumTypes) {
    return Arrays.stream(enumTypes)
        .map(enumType -> fieldWithPath(enumType.getCode()).description(enumType.getMessage()))
        .toArray(FieldDescriptor[]::new);
  }

  public static CustomResponseFieldsSnippet customResponseFields(String type,
      PayloadSubsectionExtractor<?> subsectionExtractor, FieldDescriptor... descriptors) {
    return new CustomResponseFieldsSnippet(type, subsectionExtractor, Arrays.asList(descriptors), true);
  }
}
