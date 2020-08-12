package com.example.demo.oauth2;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserIdentityEnum {
    RESIDENT(1),// 普通居民[全部用户拥有]
    PATIENT(2),// 患病者
    DOCTOR(3),// 医生
    FAMILY_DOCTOR(4),// 家庭医生
    DOCTOR_MANAGER(5)// 医管
    ;
    private Integer identity;

    public static UserIdentityEnum getByValue(String code) {
        for (UserIdentityEnum sexEnum : UserIdentityEnum.values()) {
            if (sexEnum.identity.toString().equals(code)) {
                return sexEnum;
            }
        }
        return null;
    }
}
