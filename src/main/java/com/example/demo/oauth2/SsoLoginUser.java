package com.example.demo.oauth2;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
@AutoConfigureAfter({TokenStore.class})
@Slf4j
public class SsoLoginUser {
    public static final String ROLE_PREFIX = "ROLE_";

    @Resource
    JwtTokenStore jwtTokenStore;

    /**
     * 获取登陆用户的全部信息
     *
     * @return
     */
    @SneakyThrows
    public LoginUserDetails getLoginUser() {
        if (jwtTokenStore == null) {
            throw new AccessDeniedException("不是合法的oauth2资源服务或者认证的客户端");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2Authentication) {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
            OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
            String tokenValue = oAuth2AuthenticationDetails.getTokenValue();
            Collection<GrantedAuthority> authorities = oAuth2Authentication.getAuthorities();

            CompletableFuture<LoginUserDetails> loginUserDetailsCompletableFuture = CompletableFuture
                    .supplyAsync(LoginUserDetails::new)
                    .thenApplyAsync(loginUserDetails -> {
                        loginUserDetails.oAuth2AuthenticationDetails = oAuth2AuthenticationDetails;
                        loginUserDetails.oAuth2Authentication = oAuth2Authentication;
                        loginUserDetails.token = tokenValue;
                        loginUserDetails.authorities = authorities;
                        return loginUserDetails;
                    })
                    .thenApplyAsync(loginUserDetails -> {
                        OAuth2AccessToken accessToken = jwtTokenStore.readAccessToken(tokenValue);
                        Map<String, Object> additionalInformation = accessToken.getAdditionalInformation();

                        Oauth2TokenDto oauth2TokenDto = Oauth2TokenDto.covertToOauth2TokenDto(additionalInformation);

                        loginUserDetails.scope = accessToken.getScope();
                        loginUserDetails.userId = Long.valueOf(oauth2TokenDto.getUserId());
                        loginUserDetails.openId = oauth2TokenDto.getOpenId();
                        loginUserDetails.username = oauth2TokenDto.getUsername();
                        loginUserDetails.phone = oauth2TokenDto.getPhone();
                        loginUserDetails.email = oauth2TokenDto.getEmail();
                        loginUserDetails.ownPassword = oauth2TokenDto.getOwnPassword();

                        //机构信息
                        if (!StringUtils.isEmpty(oauth2TokenDto.getOrgIds()))
                            loginUserDetails.orgIdList = Stream.of(oauth2TokenDto.getOrgIds().split(",")).collect(Collectors.toSet());
                        //部门信息
                        if (!StringUtils.isEmpty(oauth2TokenDto.getDeptIds()))
                            loginUserDetails.deptIdList = Stream.of(oauth2TokenDto.getDeptIds().split(",")).collect(Collectors.toSet());
                        //身份信息
                        if (!StringUtils.isEmpty(oauth2TokenDto.getIdentities())) {
                            ObjectMapper objectMapper = new ObjectMapper();
                            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, UserIdentityDto.class);
                            try {
                                loginUserDetails.userIdentityDtoList = objectMapper.readValue(oauth2TokenDto.getIdentities(), javaType);
                            } catch (Exception ignored) {
                            }
                        }
                        //获取地区信息
                        if (!StringUtils.isEmpty(oauth2TokenDto.getAddresses())) {
                            Map<Integer, List<String>> addressMap = new HashMap<>();
                            Stream.of(oauth2TokenDto.getAddresses().split(","))
                                    .collect(Collectors.toList())
                                    .forEach(s -> {
                                        String k = s.split("->")[0];
                                        String vStr = s.split("->")[1];
                                        List<String> v = Stream.of(vStr.split("---")).collect(Collectors.toList());
                                        addressMap.put(Integer.valueOf(k), v);
                                    });
                            loginUserDetails.userAddressMap = addressMap;
                        }

                        return loginUserDetails;
                    })
                    .thenApplyAsync(loginUserDetails -> {
                        loginUserDetails.roleList = this.getRoleList(loginUserDetails);
                        return loginUserDetails;
                    });

            LoginUserDetails loginUserDetails = loginUserDetailsCompletableFuture.get();

//            //基础资料校验
//            //手机号不存在
//            if (StringUtils.isEmpty(loginUserDetails.getPhone())) {
//                throw new RuntimeException("用户信息缺失");
//            }
//            //openId异常
//            if (StringUtils.isEmpty(loginUserDetails.getOpenId())) {
//                throw new ApplicationUserAttributeException(USER_OPEN_ID_NOT_EXIST);
//            }
//            //username异常
//            if (StringUtils.isEmpty(loginUserDetails.getUsername())) {
//                throw new ApplicationUserAttributeException(USER_USERNAME_NOT_EXIST);
//            }

            return loginUserDetails;
        }
        return null;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginUser {
        /**
         * 本次登陆的授权范围
         */
        public Set<String> scope;

        /**
         * 用户名
         */
        public String username;

        /**
         * 用户id
         */
        public Long userId;

        /**
         * 机构id
         */
        public String openId;

        /**
         * 手机号
         */
        public String phone;

        /**
         * 邮箱地址
         */
        public String email;

        /**
         * 是否输入密码
         */
        public Boolean ownPassword;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginUserDetails extends LoginUser {
        private OAuth2AuthenticationDetails oAuth2AuthenticationDetails;
        private OAuth2Authentication oAuth2Authentication;

        /**
         * jwt token
         */
        private String token;

        /**
         * 用户权限范围
         */
        private Collection<GrantedAuthority> authorities;

        /**
         * token
         */
        private OAuth2AccessToken accessToken;


        /**
         * 用户机构ID列表
         */
        private Set<String> orgIdList;

        /**
         * 用户部门ID列表
         */
        private Set<String> deptIdList;

        /**
         * 用户角色ID列表
         */
        private Set<String> roleList;

        /**
         * 用户身份ID列表
         */
        private List<UserIdentityDto> userIdentityDtoList;

        /**
         * 用户地址map
         */
        private Map<Integer, List<String>> userAddressMap;


        /**
         * 用户是否有该身份
         *
         * @param identityCode 身份code
         * @return bool
         */
        public boolean hasIdentity(Integer identityCode) {
            return this.userIdentityDtoList.stream().anyMatch(e -> e.getIdentityType().equals(identityCode));
        }

        /**
         * 用户是否有该身份
         *
         * @param userIdentityEnum 身份枚举
         * @return bool
         */
        public boolean hasIdentity(UserIdentityEnum userIdentityEnum) {
            return this.userIdentityDtoList.stream().anyMatch(e -> e.getIdentityType().equals(userIdentityEnum.getIdentity()));
        }

    }


    private Set<String> getRoleList(LoginUserDetails loginUserDetails) {
        return Stream.of(loginUserDetails
                .getAuthorities()
                .stream()
                .filter(e -> e.getAuthority().startsWith(ROLE_PREFIX))
                .map(Object::toString)
                .collect(Collectors.joining(","))
                .replace(ROLE_PREFIX, "")
                .split(",")
        ).collect(Collectors.toSet());
    }

}
