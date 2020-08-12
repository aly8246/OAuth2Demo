package com.example.demo.oauth2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Oauth2TokenDto {
    /**
     * 访问token
     */
    private String access_token;
    public static final String OAUTH2_TOKEN_DTO_ACCESS_TOKEN = "access_token";

    /**
     * token前置
     */
    private String token_type;
    public static final String OAUTH2_TOKEN_DTO_TOKEN_TYPE = "token_type";

    /**
     * 刷新token
     */
    private String refresh_token;
    public static final String OAUTH2_TOKEN_DTO_REFRESH_TOKEN = "refresh_token";

    /**
     * token过期时间
     */
    private Integer expires_in;
    public static final String OAUTH2_TOKEN_DTO_EXPIRES_IN = "expires_in";


    /**
     * 授权范围
     */
    private String scope;
    public static final String OAUTH2_TOKEN_DTO_SCOPE = "scope";


    /***
     * 用户id
     */
    private String userId;
    public static final String OAUTH2_TOKEN_DTO_USER_ID = "userId";

    /**
     * 用户名
     */
    private String username;
    public static final String OAUTH2_TOKEN_DTO_USERNAME = "username";


    /**
     * 用户手机号
     */
    private String phone;
    public static final String OAUTH2_TOKEN_DTO_PHONE = "phone";

    /**
     * 平台openId
     */
    private String openId;
    public static final String OAUTH2_TOKEN_DTO_OPENID = "openId";

    /**
     * 用户邮箱
     */
    private String email;
    public static final String OAUTH2_TOKEN_DTO_EMAIL = "email";

    /**
     * 部门id集合
     * seq ,
     */
    private String deptIds;
    public static final String OAUTH2_TOKEN_DTO_DEPTIDS = "deptIds";

    /**
     * 机构id集合
     * seq ,
     */
    private String orgIds;
    public static final String OAUTH2_TOKEN_DTO_ORGIDS = "orgIds";

    /**
     * 身份id集合
     * seq ,
     */
    private String identities;
    public static final String OAUTH2_TOKEN_DTO_IDENTITIES = "identities";

    /**
     * 地址
     * seq , ->
     */
    private String addresses;
    public static final String OAUTH2_TOKEN_DTO_ADDRESSES = "addresses";

    /**
     * oauth2 信息
     * seq ,
     */
    private String oauth2MapJsonStr;
    public static final String OAUTH2_TOKEN_DTO_OAUTH2_MAP_JSON_STR = "oauth2MapJsonStr";


    /**
     * 验证密钥
     */
    private String jti;
    public static final String OAUTH2_TOKEN_DTO_JTI = "jti";


    /**
     * 是否设置密码
     */
    private Boolean ownPassword;
    public static final String OAUTH2_TOKEN_DTO_OWN_PASSWORD = "ownPassword";


    /**
     * 转成token参数
     */
    public static Map<String, Object> convertToAdditionalInfoMap(Oauth2TokenDto oauth2TokenDto) {
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put(OAUTH2_TOKEN_DTO_USER_ID, oauth2TokenDto.getUserId());
        additionalInfo.put(OAUTH2_TOKEN_DTO_USERNAME, oauth2TokenDto.getUsername());
        additionalInfo.put(OAUTH2_TOKEN_DTO_PHONE, oauth2TokenDto.getPhone());
        additionalInfo.put(OAUTH2_TOKEN_DTO_EMAIL, oauth2TokenDto.getEmail());
        additionalInfo.put(OAUTH2_TOKEN_DTO_OPENID, oauth2TokenDto.getOpenId());

        additionalInfo.put(OAUTH2_TOKEN_DTO_DEPTIDS, oauth2TokenDto.getDeptIds());
        additionalInfo.put(OAUTH2_TOKEN_DTO_ORGIDS, oauth2TokenDto.getOrgIds());
        additionalInfo.put(OAUTH2_TOKEN_DTO_IDENTITIES, oauth2TokenDto.getIdentities());

        additionalInfo.put(OAUTH2_TOKEN_DTO_ADDRESSES, oauth2TokenDto.getAddresses());

        additionalInfo.put(OAUTH2_TOKEN_DTO_OWN_PASSWORD, oauth2TokenDto.getOwnPassword());

        return additionalInfo;
    }

    public static Oauth2TokenDto covertToOauth2TokenDto(Map<String, Object> additionalInfoMap) {
        Oauth2TokenDto oauth2TokenDto = new Oauth2TokenDto();

        oauth2TokenDto.setUserId(additionalInfoMap.getOrDefault(OAUTH2_TOKEN_DTO_USER_ID, "null").toString());
        oauth2TokenDto.setUsername(additionalInfoMap.getOrDefault(OAUTH2_TOKEN_DTO_USERNAME, "null").toString());
        oauth2TokenDto.setOpenId(additionalInfoMap.getOrDefault(OAUTH2_TOKEN_DTO_OPENID, "null").toString());
        oauth2TokenDto.setIdentities(additionalInfoMap.getOrDefault(OAUTH2_TOKEN_DTO_IDENTITIES, "null").toString());

        oauth2TokenDto.setPhone(additionalInfoMap.getOrDefault(OAUTH2_TOKEN_DTO_PHONE, "null") == null ? "" : additionalInfoMap.getOrDefault(OAUTH2_TOKEN_DTO_PHONE, "null").toString());
        oauth2TokenDto.setEmail(additionalInfoMap.getOrDefault(OAUTH2_TOKEN_DTO_EMAIL, "null") == null ? "" : additionalInfoMap.getOrDefault(OAUTH2_TOKEN_DTO_EMAIL, "null").toString());
        oauth2TokenDto.setDeptIds(additionalInfoMap.getOrDefault(OAUTH2_TOKEN_DTO_DEPTIDS, "null") == null ? "" : additionalInfoMap.getOrDefault(OAUTH2_TOKEN_DTO_DEPTIDS, "null").toString());
        oauth2TokenDto.setOrgIds(additionalInfoMap.getOrDefault(OAUTH2_TOKEN_DTO_ORGIDS, "null") == null ? "" : additionalInfoMap.getOrDefault(OAUTH2_TOKEN_DTO_ORGIDS, "null").toString());
        oauth2TokenDto.setAddresses(additionalInfoMap.getOrDefault(OAUTH2_TOKEN_DTO_ADDRESSES, "null") == null ? "" : additionalInfoMap.getOrDefault(OAUTH2_TOKEN_DTO_ADDRESSES, "null").toString());
        oauth2TokenDto.setOwnPassword(additionalInfoMap.getOrDefault(OAUTH2_TOKEN_DTO_OWN_PASSWORD, "false") != null && Boolean.parseBoolean(additionalInfoMap.getOrDefault(OAUTH2_TOKEN_DTO_OWN_PASSWORD, "false").toString()));
        return oauth2TokenDto;
    }
}
