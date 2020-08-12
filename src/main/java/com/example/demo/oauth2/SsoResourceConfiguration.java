package com.example.demo.oauth2;

import com.example.demo.util.ApplicationContextUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SsoResourceConfiguration extends ResourceServerConfigurerAdapter {

    @Value("${security.oauth2.resource.id}")
    private String resourceId;


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(resourceId).stateless(true);
        resources.authenticationEntryPoint(ApplicationContextUtil.getBean(AuthenticationEntryPoint.class));
        resources.accessDeniedHandler(ApplicationContextUtil.getBean(AccessDeniedHandler.class));
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
                .httpBasic()
        ;
    }

    private String[] getStringArray(List<String> stringList) {
        if (stringList == null) {
            return new String[0];
        }
        String[] strings = new String[stringList.size()];
        for (int i = 0; i < stringList.size(); i++) {
            strings[i] = stringList.get(i);
        }
        return strings;
    }

    /**
     * 权限验证处理器
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    /**
     * 异常处理器
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthExceptionHandler();
    }

}
