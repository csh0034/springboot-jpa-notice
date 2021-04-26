package com.ask.sample.controller;

import com.ask.sample.constant.Constant;
import com.ask.sample.domain.User;
import com.ask.sample.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "testUser")
class NoticeControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private UserRepository userRepository;
    @Autowired private EntityManager em;

    @BeforeEach
    void initUser() {
        User user = User.createUser("loginId-init", "password-init", Constant.Role.ROLE_USER, "userNm-init");
        userRepository.save(user);

        SecurityContext ctx = SecurityContextHolder.getContext();
        ctx.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
    }

    @Test
    void addNotice() throws Exception {
        // GIVEN
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("title", "Test Title");
        params.add("content", "Test Content");

        MockMultipartFile mockFile1 = new MockMultipartFile("multipartFiles", "File_1.txt", MediaType.TEXT_PLAIN_VALUE, "File 1".getBytes());
        MockMultipartFile mockFile2 = new MockMultipartFile("multipartFiles", "File_2.txt", MediaType.TEXT_PLAIN_VALUE, "File 2".getBytes());
        MockMultipartFile mockFile3 = new MockMultipartFile("multipartFiles", "File_3.txt", MediaType.TEXT_PLAIN_VALUE, "File 3".getBytes());

        // WHEN
        ResultActions perform = mvc.perform(multipart("/notice/add")
                .file(mockFile1)
                .file(mockFile2)
                .file(mockFile3)
                .params(params));

        // THEN
        em.flush();
        perform.andExpect(status().isOk());
    }
}