package com.mxpioframework.oauth.server.client;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.oauth.server.entity.OauthClientDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Arrays;

/**
 * 基于数据库表 {@code MB_OAUTH_CLIENT_DETAILS}（{@link OauthClientDetails}）的
 * {@link RegisteredClientRepository} 实现，替代原 Spring Security OAuth2 的
 * {@code ClientDetailsService}/{@code Oauth2ClientDetailsServiceImpl}，适配 Spring Authorization Server 7.0。
 *
 * <p>接入端信息在数据库表中直接维护（参见模块 README），{@link #save(RegisteredClient)} 不予支持。
 */
@Slf4j
@Service
public class JpaRegisteredClientRepository implements RegisteredClientRepository {

	@Override
	@Transactional(readOnly = true)
	public RegisteredClient findByClientId(String clientId) {
		return loadRegisteredClient(clientId);
	}

	@Override
	@Transactional(readOnly = true)
	public RegisteredClient findById(String id) {
		// id 即 clientId（见 loadRegisteredClient 中 withId(clientId)）
		return loadRegisteredClient(id);
	}

	@Override
	public void save(RegisteredClient registeredClient) {
		// no-op：接入端在数据库表 MB_OAUTH_CLIENT_DETAILS 中直接维护，不支持通过接口保存。
		// Spring Authorization Server 的 ClientSecretAuthenticationProvider 在 secret 编码需要升级（如 {noop} 升级为
		// {bcrypt}）时会调用 save()，若此处抛出异常会导致 /oauth2/token 在客户端鉴权成功后失败，故静默忽略，
		// DB 中的原始 secret 保持不变。
		log.debug("RegisteredClientRepository.save({}) ignored; maintain client in MB_OAUTH_CLIENT_DETAILS directly.",
				registeredClient != null ? registeredClient.getClientId() : null);
	}

	private RegisteredClient loadRegisteredClient(String clientId) {
		if (StringUtils.isBlank(clientId)) {
			return null;
		}
		OauthClientDetails client = JpaUtil.getOne(OauthClientDetails.class, clientId);
		if (client == null) {
			return null;
		}
		if (StringUtils.isBlank(client.getGrantTypes())) {
			log.warn("OAuth2 接入端 [{}] 未配置授权类型（GRANT_TYPES_），无法注册", clientId);
			return null;
		}

		RegisteredClient.Builder builder = RegisteredClient.withId(client.getClientId())
				.clientId(client.getClientId());

		// 客户端凭证与认证方式
		if (StringUtils.isNotBlank(client.getClientSecret())) {
			builder.clientSecret(client.getClientSecret())
					.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
					.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST);
		} else {
			// 公共客户端（如 PKCE）
			builder.clientAuthenticationMethod(ClientAuthenticationMethod.NONE);
		}

		// 授权类型
		for (String grantType : split(client.getGrantTypes())) {
			AuthorizationGrantType resolved = resolveGrantType(grantType);
			if (resolved != null) {
				builder.authorizationGrantType(resolved);
			}
		}

		// 作用域
		for (String scope : split(client.getScope())) {
			if (StringUtils.isNotBlank(scope)) {
				builder.scope(scope.trim());
			}
		}

		// 回调地址
		for (String redirectUri : split(client.getRedirectUri())) {
			if (StringUtils.isNotBlank(redirectUri)) {
				builder.redirectUri(redirectUri.trim());
			}
		}

		// Token 有效期
		TokenSettings.Builder tokenSettings = TokenSettings.builder();
		if (client.getAccessTokenValidity() != null) {
			tokenSettings.accessTokenTimeToLive(Duration.ofSeconds(client.getAccessTokenValidity()));
		}
		if (client.getRefreshTokenValidity() != null) {
			tokenSettings.refreshTokenTimeToLive(Duration.ofSeconds(client.getRefreshTokenValidity()));
		}
		builder.tokenSettings(tokenSettings.build());

		// 跳过授权确认页（自动批准），与原 autoApprove 行为一致，避免需要额外的 consent 页面。
		// SAS 7.0 起 ClientSettings.builder() 默认 requireProofKey(true)，会对所有接入端强制 PKCE。
		// 机密客户端（已配置 client_secret）无需 PKCE，显式关闭；公共客户端（无 secret）保留 PKCE，符合 RFC 7636。
		ClientSettings.Builder clientSettingsBuilder = ClientSettings.builder().requireAuthorizationConsent(false);
		if (StringUtils.isNotBlank(client.getClientSecret())) {
			clientSettingsBuilder.requireProofKey(false);
		}
		builder.clientSettings(clientSettingsBuilder.build());

		return builder.build();
	}

	private AuthorizationGrantType resolveGrantType(String grantType) {
		if (grantType == null) {
			return null;
		}
		String value = grantType.trim();
		if (StringUtils.isBlank(value)) {
			return null;
		}
		switch (value) {
			case "authorization_code":
				return AuthorizationGrantType.AUTHORIZATION_CODE;
			case "refresh_token":
				return AuthorizationGrantType.REFRESH_TOKEN;
			case "client_credentials":
				return AuthorizationGrantType.CLIENT_CREDENTIALS;
			case "password":
				// Spring Authorization Server 7.0 已移除 password 授权类型，忽略
				log.warn("OAuth2 接入端配置了 password 授权类型，Spring Authorization Server 7.0 不再支持，已忽略");
				return null;
			default:
				log.warn("未知的 OAuth2 授权类型：{}", value);
				return null;
		}
	}

	private String[] split(String value) {
		if (StringUtils.isBlank(value)) {
			return new String[0];
		}
		return Arrays.stream(value.split(","))
				.map(String::trim)
				.filter(StringUtils::isNotBlank)
				.toArray(String[]::new);
	}

}
