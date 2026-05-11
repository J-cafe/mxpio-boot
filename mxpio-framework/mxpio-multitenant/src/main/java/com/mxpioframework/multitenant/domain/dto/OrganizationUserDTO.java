package com.mxpioframework.multitenant.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrganizationUserDTO implements Serializable {

    private String organizationId;

    private String organizationName;

    private String username;

    private String nickname;

    private String password;

    private String dataSourceInfoId;
}
