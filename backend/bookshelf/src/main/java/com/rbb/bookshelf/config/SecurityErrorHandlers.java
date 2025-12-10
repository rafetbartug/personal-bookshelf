package com.rbb.bookshelf.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Configuration
public class SecurityErrorHandlers {

    @Bean
    AuthenticationEntryPoint restAuthEntryPoint() {
        return (req, res, ex) -> writeJson(res, 401, "UNAUTHORIZED", req.getRequestURI());
    }

    @Bean
    AccessDeniedHandler restAccessDeniedHandler() {
        return (req, res, ex) -> writeJson(res, 403, "FORBIDDEN", req.getRequestURI());
    }

    private static void writeJson(HttpServletResponse res, int status, String error, String path) throws IOException {
        res.setStatus(status);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.getWriter().write("{\"status\":" + status + ",\"error\":\"" + error + "\",\"path\":\"" + path + "\"}");
    }
}
