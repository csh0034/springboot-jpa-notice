package com.ask.sample.config;

import com.ask.sample.config.security.JwtUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Configuration
@RequiredArgsConstructor
@EnableJpaAuditing
public class JpaConfig {

  private final TransactionManager transactionManager;

  @Bean
  public JPAQueryFactory jpaQueryFactory(EntityManager em) {
    return new JPAQueryFactory(em);
  }

  @Bean
  public AuditorAware<String> auditorAware() {
    return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
        .filter(authentication -> authentication instanceof JwtUser)
        .map(authentication -> ((JwtUser) authentication.getPrincipal()).getUserId());
  }

  @Bean
  public TransactionInterceptor transactionAdvice() {
    NameMatchTransactionAttributeSource txAttributeSource = new NameMatchTransactionAttributeSource();
    RuleBasedTransactionAttribute txAttribute = new RuleBasedTransactionAttribute();

    txAttribute.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
    txAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

    Map<String, TransactionAttribute> txMethods = new HashMap<>();
    txMethods.put("*", txAttribute);

    txAttributeSource.setNameMap(txMethods);

    return new TransactionInterceptor(transactionManager, txAttributeSource);
  }

  @Bean
  public Advisor transactionAdviceAdvisor() {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression("execution(* com.ask.sample.service.*.*(..))");
    return new DefaultPointcutAdvisor(pointcut, transactionAdvice());
  }
}