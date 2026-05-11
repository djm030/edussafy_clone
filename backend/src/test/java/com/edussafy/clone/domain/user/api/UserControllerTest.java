package com.edussafy.clone.domain.user.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.edussafy.clone.domain.user.application.UserCommandService;
import com.edussafy.clone.domain.user.application.UserQueryService;
import com.edussafy.clone.domain.auth.application.JwtTokenProvider;
import com.edussafy.clone.domain.user.domain.enums.UserRole;
import com.edussafy.clone.domain.user.dto.response.CampusSummaryResponse;
import com.edussafy.clone.domain.user.dto.response.UserMeResponse;
import com.edussafy.clone.global.security.CurrentUserArgumentResolver;
import com.edussafy.clone.global.security.CurrentUserPrincipal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(UserControllerTest.TestCurrentUserConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserQueryService userQueryService;

    @MockBean
    private UserCommandService userCommandService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void getCampusSummary_returns_summary_for_current_user() throws Exception {
        UserMeResponse user = new UserMeResponse(1L, "user@example.com", "홍길동", "1234567", 12, "서울", 1, null, null);
        CampusSummaryResponse summary = new CampusSummaryResponse(user, 120, 3400, "Lv.3 성장하는 개발자", 3, 96.5, 12, 0L);
        given(userQueryService.getCampusSummary(1L)).willReturn(summary);

        mockMvc.perform(get("/api/v1/users/me/campus-summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.user.email").value("user@example.com"))
                .andExpect(jsonPath("$.data.scholarshipPoint").value(120))
                .andExpect(jsonPath("$.data.levelNo").value(3));
    }

    @Test
    void verifyPassword_returns_boolean_result() throws Exception {
        given(userCommandService.verifyPassword(eq(1L), any())).willReturn(true);

        mockMvc.perform(post("/api/v1/users/me/password/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"password1234!\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.verified").value(true));

        verify(userCommandService).verifyPassword(eq(1L), any());
    }

    @Test
    void updateProfileImage_connects_uploaded_file_to_current_user() throws Exception {
        mockMvc.perform(patch("/api/v1/users/me/profile-image")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fileId\":10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(userCommandService).updateProfileImage(1L, 10L);
    }

    @Test
    void updateProfileImage_clears_profile_image_when_file_id_is_null() throws Exception {
        mockMvc.perform(patch("/api/v1/users/me/profile-image")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fileId\":null}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(userCommandService).updateProfileImage(eq(1L), isNull());
    }

    @TestConfiguration
    static class TestCurrentUserConfig implements WebMvcConfigurer {
        @Bean
        CurrentUserArgumentResolver currentUserArgumentResolver() {
            return new CurrentUserArgumentResolver() {
                @Override
                protected CurrentUserPrincipal getPrincipal() {
                    return new CurrentUserPrincipal(1L, "user@example.com", UserRole.STUDENT);
                }
            };
        }

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(currentUserArgumentResolver());
        }
    }
}
