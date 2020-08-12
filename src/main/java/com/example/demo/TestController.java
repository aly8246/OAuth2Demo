package com.example.demo;

import com.example.demo.oauth2.SsoLoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestController {
    private final SsoLoginUser ssoLoginUser;

    @PreAuthorize("isAuthenticated()")//用户必须登陆
    @GetMapping("oauth2")
    public SsoLoginUser get() {
        SsoLoginUser.LoginUserDetails loginUser = ssoLoginUser.getLoginUser();
        System.out.println(loginUser);
        return ssoLoginUser;
    }
}
