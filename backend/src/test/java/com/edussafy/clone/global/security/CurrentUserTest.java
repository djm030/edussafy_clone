package com.edussafy.clone.global.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.edussafy.clone.domain.user.domain.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.core.MethodParameter;

class CurrentUserTest {

    @Test
    void resolver_returns_current_user_id_from_security_context() throws Exception {
        CurrentUserArgumentResolver resolver = new CurrentUserArgumentResolver();
        CurrentUserPrincipal principal = new CurrentUserPrincipal(7L, "user@example.com", UserRole.STUDENT);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())
        );

        MethodParameter parameter = new MethodParameter(SampleController.class.getDeclaredMethod("sample", Long.class), 0);
        NativeWebRequest webRequest = new ServletWebRequest(new MockHttpServletRequest());

        Object result = resolver.resolveArgument(parameter, null, webRequest, null);

        assertThat(result).isEqualTo(7L);
        SecurityContextHolder.clearContext();
    }

    static class SampleController {
        void sample(@CurrentUser Long userId) {
        }
    }
}
