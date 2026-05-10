package com.edussafy.clone;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapper;
import org.springframework.boot.actuate.health.HealthEndpoint;

class DependencySmokeTest {

    @Test
    void selected_libraries_are_available_on_test_classpath() {
        assertThat(StringUtils.isBlank(" ")).isTrue();
        assertThat(JavaTimeModule.class).isNotNull();
        assertThat(Jwts.class).isNotNull();
        assertThat(Mapper.class).isNotNull();
        assertThat(JsonType.class).isNotNull();
        assertThat(HealthEndpoint.class).isNotNull();
    }
}
