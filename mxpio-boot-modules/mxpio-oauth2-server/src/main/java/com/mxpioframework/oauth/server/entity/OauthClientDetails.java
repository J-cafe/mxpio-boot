package com.mxpioframework.oauth.server.entity;

import com.mxpioframework.security.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * OAuth2 接入端配置（对应表 MB_OAUTH_CLIENT_DETAILS）。
 * <p>
 * 仅作为 JPA 实体维护接入端信息，供 {@code JpaRegisteredClientRepository} 读取并映射为
 * Spring Authorization Server 的 {@code RegisteredClient}。
 */
@Entity
@Schema(description="oauth2 接入端配置")
@Table(name = "MB_OAUTH_CLIENT_DETAILS")
@EqualsAndHashCode(callSuper=false)
@Data
public class OauthClientDetails extends BaseEntity{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="CLIENT_ID_")
    private String clientId;

    @Column(name="CLIENT_SECRET_")
    private String clientSecret;

    @Column(name="RESOURCE_IDS_")
    private String resourceIds;

    @Column(name="SCOPE_")
    private String scope;

    @Column(name="GRANT_TYPES_")
    private String grantTypes;

    @Column(name="REDIRECT_URI_")
    private String redirectUri;

    @Column(name="AUTHORITIES_")
    private String authorities;

    @Column(name="ACCESS_TOKEN_VALIDITY_")
    private Integer accessTokenValidity;

    @Column(name="REFRESH_TOKEN_VALIDITY_")
    private Integer refreshTokenValidity;

    @Column(name="AUTO_APPROVE_SCOPES_")
    private String autoApproveScopes;

}
