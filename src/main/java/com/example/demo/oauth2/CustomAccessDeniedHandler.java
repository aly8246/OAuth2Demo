package com.example.demo.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;


public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        String header = httpServletRequest.getHeader("x-feign-request");

        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        Map<String, String> map = new LinkedHashMap<>();
        if (header != null) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            map.put("code", "600");
            map.put("msg", e.getMessage());
        } else {
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            map.put("code", "600");
            map.put("msg", e.getMessage());
        }

        String json = new ObjectMapper().writeValueAsString(map);
        assert json != null;
        httpServletResponse.getWriter().write(json);
    }
}
