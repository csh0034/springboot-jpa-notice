package com.ask.sample.common;

import com.ask.sample.advice.exception.EntityNotFoundException;
import com.ask.sample.domain.User;
import com.ask.sample.repository.UserRepository;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
@Import(RestDocsConfig.class)
public abstract class ControllerSupportTest {

  @Autowired
  protected MockMvc mvc;

  @Autowired
  protected EntityManager em;

  @Autowired
  protected UserRepository userRepository;

  protected static final String GIVEN_USER_ID = "user-01";
  protected static final String GIVEN_EMAIL = "user01@gmail.com";
  protected static final String GIVEN_PASSWORD = "1234";
  protected static final String GIVEN_NOTICE_ID = "notice-01";

  protected User getSampleUser() {
    return userRepository.findByIdAndEnabledIsTrue(GIVEN_USER_ID)
        .orElseThrow(() -> new EntityNotFoundException("user not found"));
  }
}
