package com.mxpioframework.oauth.server.token;

import com.mxpioframework.cache.provider.CacheProvider;
import com.mxpioframework.common.util.SpringUtil;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.service.OnlineUserService;
import com.mxpioframework.security.util.TokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * 自定义 OAuth2 令牌生成器：将 Spring Authorization Server 颁发的访问令牌桥接为 mxpio 系统自身的
 * JWT 令牌（{@link TokenUtil#createAccessToken}），并写入在线用户缓存，使第三方获取的 access_token
 * 可直接作为 mxpio 的 Access-Token 使用（与系统原有登录逻辑完全一致，不改动 mxpio-security）。
 *
 * <p>对应原 {@code Auth0JwtAccessTokenConverter} 的行为，适配 Spring Authorization Server 7.0 的
 * {@link OAuth2TokenGenerator} 机制。仅处理授权码（authorization_code）与刷新令牌（refresh_token）授权
 * 类型，即资源主体为 mxpio {@link User} 的场景；其它类型返回 {@code null}。
 *
 * <p>注意：Spring Authorization Server 在颁发访问令牌后，会在同一请求线程内再次调用本生成器生成刷新令牌，
 * 因此使用 {@link ThreadLocal} 在两者间传递已签发的刷新令牌。
 */
@Component
public class MxpioOAuth2TokenGenerator implements OAuth2TokenGenerator<OAuth2Token> {

	private static final ThreadLocal<String> REFRESH_TOKEN_HOLDER = new ThreadLocal<>();

	@Value("${mxpio.token.time:1800000}")
	private long tokenTime;

	@Value("${mxpio.refresh.token.time:7200000}")
	private long refreshTokenTime;

	@Override
	public OAuth2Token generate(OAuth2TokenContext context) {
		if (context.getTokenType() == null) {
			return null;
		}
		if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
			return generateAccessToken(context);
		}
		if (OAuth2TokenType.REFRESH_TOKEN.equals(context.getTokenType())) {
			return generateRefreshToken();
		}
		// 其它令牌类型（如 OIDC id_token，本模块未启用 OIDC）不处理
		return null;
	}

	private OAuth2AccessToken generateAccessToken(OAuth2TokenContext context) {
		// 清理上一次（如复用刷新令牌场景）残留的值
		REFRESH_TOKEN_HOLDER.remove();

		User user = resolveUser(context);
		if (user == null) {
			// 非资源所有者（User）主体的授权类型（如 client_credentials）不在本生成器处理范围
			return null;
		}

		// 签发 mxpio 访问令牌与刷新令牌
		String accessTokenValue = TokenUtil.createAccessToken(user);
		String refreshTokenValue = TokenUtil.createRefreshToken(accessTokenValue);

		// 写入在线用户缓存，令牌即可被 JwtTokenFilter 识别（与系统原有登录逻辑一致）
		CacheProvider cacheProvider = SpringUtil.getBean(CacheProvider.class);
		if (cacheProvider != null) {
			OnlineUserService onlineUserService = SpringUtil.getBean(OnlineUserService.class);
			onlineUserService.save(user, accessTokenValue, refreshTokenValue, cacheProvider);
		}

		// 暂存刷新令牌，供同一请求中随后生成刷新令牌时复用
		REFRESH_TOKEN_HOLDER.set(refreshTokenValue);

		Instant issuedAt = Instant.now();
		Instant expiresAt = issuedAt.plusMillis(tokenTime);
		return new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, accessTokenValue, issuedAt, expiresAt);
	}

	private OAuth2RefreshToken generateRefreshToken() {
		String refreshTokenValue = REFRESH_TOKEN_HOLDER.get();
		REFRESH_TOKEN_HOLDER.remove();
		if (refreshTokenValue == null) {
			return null;
		}
		Instant issuedAt = Instant.now();
		Instant expiresAt = issuedAt.plusMillis(refreshTokenTime);
		return new OAuth2RefreshToken(refreshTokenValue, issuedAt, expiresAt);
	}

	private User resolveUser(OAuth2TokenContext context) {
		Authentication authentication = context.getPrincipal();
		if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
			return null;
		}
		return (User) authentication.getPrincipal();
	}

}
