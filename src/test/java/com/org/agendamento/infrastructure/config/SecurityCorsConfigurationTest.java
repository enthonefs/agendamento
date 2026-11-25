package com.org.agendamento.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class SecurityCorsConfigurationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void deveExporFonteCorsExplicitaParaOSpringSecurity() {
        assertThat(applicationContext.getBeansOfType(CorsConfigurationSource.class).values())
                .anyMatch(UrlBasedCorsConfigurationSource.class::isInstance);
    }
}
