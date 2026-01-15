package com.mxpioframework.oauth.server.convert;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mxpioframework.cache.provider.CacheProvider;
import com.mxpioframework.common.util.SpringUtil;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.service.OnlineUserService;
import com.mxpioframework.security.util.TokenUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.common.*;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Auth0JwtAccessTokenConverter extends JwtAccessTokenConverter {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken result = new DefaultOAuth2AccessToken(accessToken);
        result.setValue(encode(result, authentication));
        Map<String, Object> info = new LinkedHashMap<>(accessToken.getAdditionalInformation());
        String tokenId = result.getValue();
        if (!info.containsKey(TOKEN_ID)) {
            info.put(TOKEN_ID, tokenId);
        }

        result.setAdditionalInformation(info);
        return result;
    }

    @Override
    protected String encode(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        UserDetails jwtUserDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) jwtUserDetails;
        String jwtAccessToken = TokenUtil.createAccessToken(user);
        String jwtRefreshToken = TokenUtil.createRefreshToken(jwtAccessToken);
        ((DefaultOAuth2AccessToken) accessToken).setValue(jwtAccessToken);
        DefaultOAuth2RefreshToken oAuth2RefreshToken = new  DefaultOAuth2RefreshToken(jwtRefreshToken);
        ((DefaultOAuth2AccessToken) accessToken).setRefreshToken(oAuth2RefreshToken);
        CacheProvider cacheProvider = SpringUtil.getBean(CacheProvider.class);
        //签发token
        if(cacheProvider != null) {
            OnlineUserService onlineUserService = SpringUtil.getBean(OnlineUserService.class);
            onlineUserService.save(user, jwtAccessToken, jwtRefreshToken, cacheProvider);
        }
        return jwtAccessToken;
    }

    @Override
    protected Map<String, Object> decode(String token){
        try{
            DecodedJWT decodedJWT = TokenUtil.verifyToken(token);
            Map<String,Object> map = new HashMap<>();
            for(Map.Entry<String, Claim> claim : decodedJWT.getClaims().entrySet()){
                map.put(claim.getKey(),claim.getValue().asString());
            }
            return map;
        }
        catch (Exception e){
            throw new IllegalArgumentException("Invalid JWT Token: " + e.getMessage(), e);
        }

    }


}
