package com.org.agendamento.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CorsConfigTest {

    @Test
    void devePermitirOrigemDoFrontendConfigurado() {
        CorsConfiguration configuration = configurar("http://localhost:8081");

        assertThat(configuration.checkOrigin("http://localhost:8081"))
                .isEqualTo("http://localhost:8081");
        assertThat(configuration.checkHttpMethod(HttpMethod.POST)).isNotEmpty();
    }

    @Test
    void deveRejeitarOrigemNaoConfigurada() {
        CorsConfiguration configuration = configurar("http://localhost:8081");

        assertThat(configuration.checkOrigin("https://origem-nao-autorizada.example")).isNull();
    }

    private CorsConfiguration configurar(String allowedOrigins) {
        TestCorsRegistry registry = new TestCorsRegistry();
        new CorsConfig(allowedOrigins).addCorsMappings(registry);
        return registry.getConfigurations().get("/**");
    }

    private static class TestCorsRegistry extends CorsRegistry {

        private Map<String, CorsConfiguration> getConfigurations() {
            return getCorsConfigurations();
        }
    }
}
