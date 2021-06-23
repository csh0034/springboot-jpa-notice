package com.ask.sample.common;

import com.ask.sample.config.security.JwtUser;
import com.ask.sample.util.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
@Import(RestDocsConfig.class)
@ExtendWith(RestDocumentationExtension.class)
public abstract class ControllerSupportTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected EntityManager em;

    protected static final String GIVEN_LOGIN_ID = "user-01";
    protected static final String GIVEN_PASSWORD = "1234";
    protected static final String GIVEN_TOKEN = JwtUtils.generate(JwtUser.of(GIVEN_LOGIN_ID, "ROLE_USER"));
}
