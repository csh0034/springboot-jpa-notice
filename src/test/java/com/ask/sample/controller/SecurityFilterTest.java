package com.ask.sample.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ask.sample.common.ControllerSupportTest;
import com.ask.sample.constant.Constants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class SecurityFilterTest extends ControllerSupportTest {

  @Test
  @DisplayName("유저 로그인(A01)")
  void userLogin() throws Exception {
    // given
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add(Constants.EMAIL_PARAMETER, GIVEN_EMAIL);
    params.add(Constants.PASSWORD_PARAMETER, GIVEN_PASSWORD);

    // when
    ResultActions result = mvc.perform(post("/login")
        .params(params)
        .accept(MediaType.APPLICATION_JSON));

    // then
    result.andExpect(status().isOk())
        .andDo(print())
        .andDo(document("user-login",
            requestParameters(
                parameterWithName(Constants.EMAIL_PARAMETER).description("이메일")
                    .attributes(new Attributes.Attribute("validation", "NotBlank")),
                parameterWithName(Constants.PASSWORD_PARAMETER).description("패스워드")
                    .attributes(new Attributes.Attribute("validation", "NotBlank"))
            ),
            responseFields(
                fieldWithPath("timestamp").description("응답시간"),
                fieldWithPath("code").description("응답코드"),
                fieldWithPath("result").description("Access Token (JWT)"))
        ));
  }
}
