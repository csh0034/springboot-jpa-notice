package com.ask.sample.controller;

import com.ask.sample.common.ControllerSupportTest;
import com.ask.sample.constant.Constant;
import com.ask.sample.doc.RestDocs;
import com.ask.sample.domain.Notice;
import com.ask.sample.domain.User;
import com.ask.sample.repository.NoticeRepository;
import com.ask.sample.repository.UserRepository;
import com.ask.sample.service.NoticeService;
import com.ask.sample.util.SecurityUtils;
import com.ask.sample.vo.request.NoticeRequestVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.fileUpload;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
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

    @BeforeEach
    void initUser() {
        User user = User.createUser("loginId-init", "password-init", Constant.Role.ROLE_USER, "userNm-init");
        userRepository.save(user);

        SecurityContext ctx = SecurityContextHolder.getContext();
        ctx.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
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
                .header(HttpHeaders.AUTHORIZATION, "AUTHORIZATION")
                .params(params)
                .accept(MediaType.APPLICATION_JSON));

        // THEN
        em.flush();
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("notice-add",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.MULTIPART_FORM_DATA),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Token")
                        ),
                        requestParameters(
                                parameterWithName("title").description("제목"),
                                parameterWithName("content").description("내용")
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
        User currentUser = SecurityUtils.getCurrentUser();

        NoticeRequestVO noticeRequestVO = new NoticeRequestVO();
        noticeRequestVO.setTitle("New Title");
        noticeRequestVO.setContent("New Content");
        noticeRequestVO.setMultipartFiles(
                new MockMultipartFile[]{
                        new MockMultipartFile("multipartFiles", "File_1.txt", MediaType.TEXT_PLAIN_VALUE, "File 1".getBytes()),
                        new MockMultipartFile("multipartFiles", "File_2.txt", MediaType.TEXT_PLAIN_VALUE, "File 2".getBytes())
                }
        );
        String noticeId = noticeService.addNotice(currentUser.getId(), noticeRequestVO);

        // WHEN
        ResultActions result = mvc.perform(get("/notice/{noticeId}", noticeId)
                .header(HttpHeaders.AUTHORIZATION, "AUTHORIZATION")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // THEN
        em.flush();
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("notice-find",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Token")
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
        User currentUser = SecurityUtils.getCurrentUser();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "0");
        params.add("size", "15");

        for (int i = 1; i <= 30; i++) {
            Notice notice = Notice.create(currentUser, "New Title : " + i, "New Content : " + i, Collections.emptyList());
            noticeRepository.save(notice);
            Thread.sleep(1);
        }

        // WHEN
        ResultActions result = mvc.perform(get("/notice")
                .header(HttpHeaders.AUTHORIZATION, "AUTHORIZATION")
                .params(params)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // THEN
        em.flush();
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("notice-all-find",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Token")
                        ),
                        requestParameters(
                                parameterWithName("page").description("요청 페이지").optional(),
                                parameterWithName("size").description("페이지당 출력수 (default 20)").optional()
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
                                fieldWithPath("result[].createdDt").description("등록일"),
                                fieldWithPath("result[].createdDe").description("등록일 " + Constant.DATE_FORMAT.getValue()),
                                fieldWithPath("page.size").description("페이지당 출력수"),
                                fieldWithPath("page.totalElements").description("검색된 전체 요소 개수"),
                                fieldWithPath("page.totalPages").description("전체 페이지 수"),
                                fieldWithPath("page.number").description("현재 페이지의 번호 (0부터 시작)"))

                ));
        ;
    }
}