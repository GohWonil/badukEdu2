package org.choongang.configs;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DbConfig {

  private final EntityManager em;

  @Bean
  public JPAQueryFactory jpaQueryFactory() {

    return new JPAQueryFactory(em);
  }
}
