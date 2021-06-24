package com.ask.sample.controller;

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

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        accessToken = jwtUtils.generate(JwtUser.of(GIVEN_LOGIN_ID, "ROLE_USER"));
    }

    @Test
    @DisplayName("공지사항 등록(B01)")
    void addNotice() throws Exception {

        // GIVEN
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("title", "Test Title");
        params.add("content", "Test Content");

        MockMultipartFile mockFile1 = new MockMultipartFile("multipartFiles", "File_1.txt", MediaType.TEXT_PLAIN_VALUE, "File 1".getBytes());
        MockMultipartFile mockFile2 = new MockMultipartFile("multipartFiles", "File_2.txt", MediaType.TEXT_PLAIN_VALUE, "File 2".getBytes());
        MockMultipartFile mockFile3 = new MockMultipartFile("multipartFiles", "File_3.txt", MediaType.TEXT_PLAIN_VALUE, "File 3".getBytes());

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
                                parameterWithName("title").description("제목").attributes(new Attribute("validation", "NotBlank")),
                                parameterWithName("content").description("내용").attributes(new Attribute("validation", "NotBlank"))
                        ),
                        requestParts(
                                partWithName("multipartFiles").description("업로드파일[]").optional()
                        ),
                        responseFields(RestDocs.noticeFindResponseDescriptor)
                ));
    }

    @Test
    @DisplayName("공지사항 상세 조회(B02)")
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
                                parameterWithName("noticeId").description("공지사항 ID")
                        ),
                        responseFields(RestDocs.noticeFindResponseDescriptor)
                ));
    }

    @Test
    @DisplayName("공지사항 조회(B03)")
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
                .params(params)
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
                                parameterWithName("page").description("요청 페이지 (default 0)").optional().attributes(new Attribute("validation", "Number")),
                                parameterWithName("size").description("페이지당 출력수 (default 20)").optional().attributes(new Attribute("validation", "Number")),
                                parameterWithName("title").description("제목 (검색조건)").optional()
                        ),
                        responseFields(
                                fieldWithPath("timestamp").description("요청시간"),
                                fieldWithPath("code").description("응답코드"),
                                fieldWithPath("result[].id").description("공지사항 ID"),
                                fieldWithPath("result[].title").description("제목"),
                                fieldWithPath("result[].content").description("내용"),
                                fieldWithPath("result[].fileCnt").description("파일수"),
                                fieldWithPath("result[].readCnt").description("조회수"),
                                fieldWithPath("result[].createdBy").description("등록자"),
                                fieldWithPath("result[].createdDt").description("등록일 " + Constants.DATE_FORMAT),
                                fieldWithPath("page.size").description("페이지당 출력수"),
                                fieldWithPath("page.totalElements").description("검색된 전체 요소 개수"),
                                fieldWithPath("page.totalPages").description("전체 페이지 수"),
                                fieldWithPath("page.currentPage").description("현재 페이지의 번호 (0부터 시작)"))

                ));
    }

    @Test
    @DisplayName("공지사항 첨부파일 다운로드(B04)")
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
                                parameterWithName("noticeId").description("공지사항 ID"),
                                parameterWithName("attachmentId").description("첨부파일 ID")
                        )
                ))
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertThat(response.getContentAsString()).isNotBlank();
        assertThat(response.getContentType()).isEqualTo("application/octet-stream;charset=UTF-8");
        assertThat(response.getHeader(HttpHeaders.CONTENT_DISPOSITION)).contains("File_1.txt");
    }

    @Test
    @DisplayName("공지사항 첨부파일 삭제(B05)")
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
                                parameterWithName("noticeId").description("공지사항 ID"),
                                parameterWithName("attachmentId").description("첨부파일 ID")
                        ),
                        responseFields(
                                fieldWithPath("timestamp").description("요청시간"),
                                fieldWithPath("code").description("응답코드"))
                ))
                .andReturn();

    }
}