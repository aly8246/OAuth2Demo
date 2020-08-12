package com.example.demo.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;


public class CustomAuthExceptionHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        System.out.println(e.getMessage());
        e.printStackTrace();

        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        /**
         *   if (e instanceof InvalidTokenException) {
         *             result = Result.error(OAUTH2_TOKEN_INVALID, e.getMessage());
         *         } else
         */


        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        Map<String, String> map = new LinkedHashMap<>();
        if (e instanceof InsufficientAuthenticationException) {
            String message = e.getMessage();
            if (message.contains("Invalid token does not contain resource id")) {
                map.put("code", "600");
                map.put("msg", e.getMessage());
            }
            httpServletResponse.setStatus(HttpStatus.OK.value());
            map.put("code", "600");
            map.put("msg", e.getMessage());
        } else {
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            map.put("code", "600");
            map.put("msg", e.getMessage());
        }

        httpServletResponse.getWriter().write(Objects.requireNonNull(new ObjectMapper().writeValueAsString(map)));
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.writeValue(httpServletResponse.getOutputStream(), result);
    }
}
