package com.ask.sample.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.fileUpload;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ask.sample.common.ControllerSupportTest;
import com.ask.sample.config.security.JwtUser;
import com.ask.sample.constant.Constants;
import com.ask.sample.doc.RestDocs;
import com.ask.sample.domain.Notice;
import com.ask.sample.domain.User;
import com.ask.sample.repository.NoticeRepository;
import com.ask.sample.repository.UserRepository;
import com.ask.sample.service.NoticeService;
import com.ask.sample.util.JwtUtils;
import com.ask.sample.vo.request.NoticeRequestVO;
import com.ask.sample.vo.response.NoticeResponseVO;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.snippet.Attributes.Attribute;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@TestPropertySource(properties = "setting.upload-dir=~/DEV/upload-test")
class NoticeControllerTest extends ControllerSupportTest {

  @Autowired
  private NoticeService noticeService;

  @Autowired
  private NoticeRepository noticeRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtUtils jwtUtils;

  private String accessToken;

  @BeforeEach
  void setup() {
    accessToken = jwtUtils.generate(JwtUser.of(GIVEN_USER_ID, GIVEN_LOGIN_ID, "ROLE_USER"));
  }

  @Test
  @DisplayName("???????????? ??????(B01)")
  void addNotice() throws Exception {

    // GIVEN
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("title", "Test Title");
    params.add("content", "Test Content");

    MockMultipartFile mockFile1 = new MockMultipartFile("multipartFiles", "File_1.txt", MediaType.TEXT_PLAIN_VALUE,
        "File 1".getBytes());
    MockMultipartFile mockFile2 = new MockMultipartFile("multipartFiles", "File_2.txt", MediaType.TEXT_PLAIN_VALUE,
        "File 2".getBytes());
    MockMultipartFile mockFile3 = new MockMultipartFile("multipartFiles", "File_3.txt", MediaType.TEXT_PLAIN_VALUE,
        "File 3".getBytes());

    // WHEN
    ResultActions result = mvc.perform(fileUpload("/notice")
        .file(mockFile1)
        .file(mockFile2)
        .file(mockFile3)
        .header(HttpHeaders.AUTHORIZATION, accessToken)
        .params(params)
        .accept(MediaType.APPLICATION_JSON));

    // THEN
    result.andExpect(status().isOk())
        .andDo(print())
        .andDo(document("notice-add",
            requestHeaders(
                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.MULTIPART_FORM_DATA),
                headerWithName(HttpHeaders.AUTHORIZATION).description("Access Token (JWT)")
            ),
            requestParameters(
                parameterWithName("title").description("??????").attributes(new Attribute("validation", "NotBlank")),
                parameterWithName("content").description("??????").attributes(new Attribute("validation", "NotBlank"))
            ),
            requestParts(
                partWithName("multipartFiles").description("???????????????[]").optional()
            ),
            responseFields(RestDocs.noticeFindResponseDescriptor)
        ));
  }

  @Test
  @DisplayName("???????????? ?????? ??????(B02)")
  void findNotice() throws Exception {

    // GIVEN
    NoticeRequestVO noticeRequestVO = new NoticeRequestVO();
    noticeRequestVO.setTitle("New Title");
    noticeRequestVO.setContent("New Content");
    noticeRequestVO.setMultipartFiles(
        new MockMultipartFile[]{
            new MockMultipartFile("multipartFiles", "File_1.txt", MediaType.TEXT_PLAIN_VALUE, "File 1".getBytes()),
            new MockMultipartFile("multipartFiles", "File_2.txt", MediaType.TEXT_PLAIN_VALUE, "File 2".getBytes())
        }
    );
    String noticeId = noticeService.addNotice(GIVEN_LOGIN_ID, noticeRequestVO);

    // WHEN
    ResultActions result = mvc.perform(get("/notice/{noticeId}", noticeId)
        .header(HttpHeaders.AUTHORIZATION, accessToken)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON));

    // THEN
    result.andExpect(status().isOk())
        .andDo(print())
        .andDo(document("notice-find",
            requestHeaders(
                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON),
                headerWithName(HttpHeaders.AUTHORIZATION).description("Access Token (JWT)")
            ),
            pathParameters(
                parameterWithName("noticeId").description("???????????? ID")
            ),
            responseFields(RestDocs.noticeFindResponseDescriptor)
        ));
  }

  @Test
  @DisplayName("???????????? ??????(B03)")
  void findAllNotice() throws Exception {

    // GIVEN
    User user = userRepository.findByLoginIdAndEnabledIsTrue(GIVEN_LOGIN_ID).orElse(null);

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("page", "0");
    params.add("size", "15");
    params.add("title", "New");

    for (int i = 1; i <= 30; i++) {
      Notice notice = Notice.create(user, "New Title : " + i, "New Content : " + i, Collections.emptyList());
      noticeRepository.save(notice);
      Thread.sleep(1);
    }

    // WHEN
    ResultActions result = mvc.perform(get("/notice")
        .header(HttpHeaders.AUTHORIZATION, accessToken)
        .queryParams(params)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON));

    // THEN
    result.andExpect(status().isOk())
        .andDo(print())
        .andDo(document("notice-all-find",
            requestHeaders(
                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON),
                headerWithName(HttpHeaders.AUTHORIZATION).description("Access Token (JWT)")
            ),
            requestParameters(
                parameterWithName("page").description("?????? ????????? (default 0)").optional()
                    .attributes(new Attribute("validation", "Number")),
                parameterWithName("size").description("???????????? ????????? (default 20)").optional()
                    .attributes(new Attribute("validation", "Number")),
                parameterWithName("title").description("?????? (????????????)").optional()
            ),
            responseFields(
                fieldWithPath("timestamp").description("????????????"),
                fieldWithPath("code").description("????????????"),
                fieldWithPath("result[].id").description("???????????? ID"),
                fieldWithPath("result[].title").description("??????"),
                fieldWithPath("result[].content").description("??????"),
                fieldWithPath("result[].fileCnt").description("?????????"),
                fieldWithPath("result[].readCnt").description("?????????"),
                fieldWithPath("result[].createdBy").description("?????????"),
                fieldWithPath("result[].createdDt").description("????????? " + Constants.DATE_FORMAT),
                fieldWithPath("page.size").description("???????????? ?????????"),
                fieldWithPath("page.totalElements").description("????????? ?????? ?????? ??????"),
                fieldWithPath("page.totalPages").description("?????? ????????? ???"),
                fieldWithPath("page.currentPage").description("?????? ???????????? ?????? (0?????? ??????)"))

        ));
  }

  @Test
  @DisplayName("???????????? ???????????? ????????????(B04)")
  void downloadAttachment() throws Exception {

    // GIVEN
    User user = userRepository.findByLoginIdAndEnabledIsTrue(GIVEN_LOGIN_ID).orElse(null);
    assert user != null;

    NoticeRequestVO noticeRequestVO = new NoticeRequestVO();
    noticeRequestVO.setTitle("New Title");
    noticeRequestVO.setContent("New Content");
    noticeRequestVO.setMultipartFiles(
        new MockMultipartFile[]{
            new MockMultipartFile("multipartFiles", "File_1.txt", MediaType.TEXT_PLAIN_VALUE, "File 1".getBytes())
        }
    );
    String noticeId = noticeService.addNotice(user.getId(), noticeRequestVO);
    NoticeResponseVO notice = noticeService.findNotice(noticeId, false);
    String attachmentId = notice.getFiles().get(0).getId();

    // WHEN
    ResultActions result = mvc.perform(get("/notice/{noticeId}/attachment/{attachmentId}", noticeId, attachmentId)
        .header(HttpHeaders.AUTHORIZATION, accessToken)
        .contentType(MediaType.APPLICATION_JSON));

    // THEN
    MvcResult mvcResult = result.andExpect(status().isOk())
        .andDo(print())
        .andDo(document("notice-attachment-download",
            requestHeaders(
                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON),
                headerWithName(HttpHeaders.AUTHORIZATION).description("Access Token (JWT)")
            ),
            pathParameters(
                parameterWithName("noticeId").description("???????????? ID"),
                parameterWithName("attachmentId").description("???????????? ID")
            )
        ))
        .andReturn();

    MockHttpServletResponse response = mvcResult.getResponse();

    assertThat(response.getContentAsString()).isNotBlank();
    assertThat(response.getContentType()).isEqualTo("application/octet-stream;charset=UTF-8");
    assertThat(response.getHeader(HttpHeaders.CONTENT_DISPOSITION)).contains("File_1.txt");
  }

  @Test
  @DisplayName("???????????? ???????????? ??????(B05)")
  void removeAttachment() throws Exception {

    // GIVEN
    User user = userRepository.findByLoginIdAndEnabledIsTrue(GIVEN_LOGIN_ID).orElse(null);
    assert user != null;

    NoticeRequestVO noticeRequestVO = new NoticeRequestVO();
    noticeRequestVO.setTitle("New Title");
    noticeRequestVO.setContent("New Content");
    noticeRequestVO.setMultipartFiles(
        new MockMultipartFile[]{
            new MockMultipartFile("multipartFiles", "File_1.txt", MediaType.TEXT_PLAIN_VALUE, "File 1".getBytes())
        }
    );
    String noticeId = noticeService.addNotice(user.getId(), noticeRequestVO);
    NoticeResponseVO notice = noticeService.findNotice(noticeId, false);
    String attachmentId = notice.getFiles().get(0).getId();

    // WHEN
    ResultActions result = mvc.perform(post("/notice/{noticeId}/attachment/{attachmentId}", noticeId, attachmentId)
        .header(HttpHeaders.AUTHORIZATION, accessToken)
        .contentType(MediaType.APPLICATION_JSON));

    // THEN
    result.andExpect(status().isOk())
        .andDo(print())
        .andDo(document("notice-attachment-remove",
            requestHeaders(
                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON),
                headerWithName(HttpHeaders.AUTHORIZATION).description("Access Token (JWT)")
            ),
            pathParameters(
                parameterWithName("noticeId").description("???????????? ID"),
                parameterWithName("attachmentId").description("???????????? ID")
            ),
            responseFields(
                fieldWithPath("timestamp").description("????????????"),
                fieldWithPath("code").description("????????????"))
        ))
        .andReturn();

  }
}