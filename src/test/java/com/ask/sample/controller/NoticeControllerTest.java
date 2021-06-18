package com.ask.sample.controller;

import com.ask.sample.common.ControllerSupportTest;
import com.ask.sample.constant.Constant;
import com.ask.sample.doc.RestDocs;
import com.ask.sample.domain.User;
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

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.fileUpload;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = "setting.upload-dir=~/DEV/upload-test")
class NoticeControllerTest extends ControllerSupportTest {

    @Autowired
    private NoticeService noticeService;

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
}