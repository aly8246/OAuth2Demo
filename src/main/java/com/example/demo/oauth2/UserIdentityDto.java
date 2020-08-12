package com.example.demo.oauth2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserIdentityDto {
    /**
     * id
     */
    private Long id;

    /**
     * 用户身份类型
     */
    private Integer identityType;

    /**
     * 身份code
     */
    private String identityCode;

    /**
     * 机构id
     */
    private Long orgId;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 地区id
     */
    private Long areaId;

    /**
     * 身份特权级别
     */
    private Integer ipl;

}