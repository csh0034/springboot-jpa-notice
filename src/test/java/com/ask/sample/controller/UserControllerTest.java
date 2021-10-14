package com.ask.sample.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ask.sample.common.ControllerSupportTest;
import com.ask.sample.config.security.JwtUser;
import com.ask.sample.doc.RestDocs;
import com.ask.sample.repository.UserRepository;
import com.ask.sample.service.UserService;
import com.ask.sample.util.JwtUtils;
import com.ask.sample.vo.request.UserRequestVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.ResultActions;

class UserControllerTest extends ControllerSupportTest {

  @Autowired
  private UserService userService;

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private ObjectMapper objectMapper;

  private String accessToken;

  @BeforeEach
  void setup() {
    accessToken = jwtUtils.generate(JwtUser.of(GIVEN_USER_ID, GIVEN_EMAIL, "ROLE_USER"));
  }

  @Test
  @DisplayName("유저 등록(A01)")
  void addUser() throws Exception {
    // given
    UserRequestVO requestVO = new UserRequestVO();
    requestVO.setEmail("sample01@gmail.com");
    requestVO.setPassword("sample-password");
    requestVO.setName("sample-name");

    // when
    ResultActions result = mvc.perform(post("/users")
        .content(objectMapper.writeValueAsString(requestVO))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON));

    // then
    result.andExpect(status().isOk())
        .andDo(print())
        .andDo(document("user-add",
            requestHeaders(
                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON)
            ),
            requestFields(
                fieldWithPath("email").description("이메일").attributes(new Attributes.Attribute("validation", "NotBlank")),
                fieldWithPath("password").description("패스워드").attributes(new Attributes.Attribute("validation", "NotBlank")),
                fieldWithPath("name").description("이름").attributes(new Attributes.Attribute("validation", "NotBlank"))
            ),
            responseFields(RestDocs.userResponseDescriptor)
        ));
  }

  @Test
  @DisplayName("유저 수정(A02)")
  void updateUser() throws Exception {
    // given
    UserRequestVO requestVO = new UserRequestVO();
    requestVO.setEmail("update01@gmail.com");
    requestVO.setPassword("update-password");
    requestVO.setName("update-name");

    // when
    ResultActions result = mvc.perform(put("/users/{userId}", GIVEN_USER_ID)
        .content(objectMapper.writeValueAsString(requestVO))
        .header(HttpHeaders.AUTHORIZATION, accessToken)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON));

    // then
    result.andExpect(status().isOk())
        .andDo(print())
        .andDo(document("user-update",
            requestHeaders(
                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON),
                headerWithName(HttpHeaders.AUTHORIZATION).description("Access Token (JWT)")
            ),
            pathParameters(
                parameterWithName("userId").description("유저 ID")
            ),
            requestFields(
                fieldWithPath("email").description("이메일").attributes(new Attributes.Attribute("validation", "NotBlank")),
                fieldWithPath("password").description("패스워드").attributes(new Attributes.Attribute("validation", "NotBlank")),
                fieldWithPath("name").description("이름").attributes(new Attributes.Attribute("validation", "NotBlank"))
            ),
            responseFields(RestDocs.userResponseDescriptor)
        ));
  }

  @Test
  @DisplayName("유저 삭제(A03)")
  void deleteUser() throws Exception {
    // given
    String userId = addSampleUser();

    // when
    ResultActions result = mvc.perform(delete("/users/{userId}", userId)
        .header(HttpHeaders.AUTHORIZATION, accessToken)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON));

    // then
    result.andExpect(status().isOk())
        .andDo(print())
        .andDo(document("user-delete",
            requestHeaders(
                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON),
                headerWithName(HttpHeaders.AUTHORIZATION).description("Access Token (JWT)")
            ),
            pathParameters(
                parameterWithName("userId").description("유저 ID")
            ),
            responseFields(
                fieldWithPath("timestamp").description("응답시간"),
                fieldWithPath("code").description("응답코드")
            )
        ));

    assertThat(userRepository.existsById(userId)).isFalse();
  }

  private String addSampleUser() {
    UserRequestVO requestVO = new UserRequestVO();
    requestVO.setEmail("sample01@gmail.com");
    requestVO.setPassword("sample-password");
    requestVO.setName("sample-name");
    return userService.addUser(requestVO);
  }
}