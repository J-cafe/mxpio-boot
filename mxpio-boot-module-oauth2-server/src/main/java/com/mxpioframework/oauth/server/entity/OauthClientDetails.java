package com.mxpioframework.oauth.server.entity;

import com.mxpioframework.security.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.*;

@Entity
@Schema(description="oauth2 接入端配置")
@Table(name = "MB_OAUTH_CLIENT_DETAILS")
@EqualsAndHashCode(callSuper=false)
public class OauthClientDetails extends BaseEntity implements ClientDetails{

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

    @Column(name="AUTHORIZED_GRANT_TYPES_")
    private String authorizedGrantTypes;

    @Column(name="WEB_SERVER_REDIRECT_URI_")
    private String webServerRedirectUri;

    @Column(name="AUTHORITIES_")
    private String authorities;

    @Column(name="ACCESS_TOKEN_VALIDITY_")
    private Integer accessTokenValidity;

    @Column(name="REFRESH_TOKEN_VALIDITY_")
    private Integer refreshTokenValidity;

    @Column(name="ADDITIONAL_INFORMATION_")
    private String additionalInformation;

    @Column(name="AUTOAPPROVE_")
    private String autoapprove;

    public String getClientId(){
        return clientId;
    }

    public Set<String> getResourceIds(){
        Set<String> set = new HashSet<>();
        if(StringUtils.isNotBlank(this.resourceIds)){
            set.addAll(Arrays.asList(this.resourceIds.split(",")));
        }
        return set;
    }

    public String getClientSecret(){
        return this.clientSecret;
    }

    public Set<String> getScope(){
        Set<String> set = new HashSet<>();
        if(StringUtils.isNotBlank(this.scope)){
            set.addAll(Arrays.asList(this.scope.split(",")));
        }
        return set;
    }

    public Set<String> getAuthorizedGrantTypes(){
        Set<String> set = new HashSet<>();
        if(StringUtils.isNotBlank(this.authorizedGrantTypes)){
            set.addAll(Arrays.asList(this.authorizedGrantTypes.split(",")));
        }
        return set;
    }

    public Collection<GrantedAuthority> getAuthorities(){
        Collection<GrantedAuthority> list = new ArrayList<>();
        if(StringUtils.isNotBlank(this.authorities)){
            for(String authority : this.authorities.split(",")){
                list.add(new OAuth2UserAuthority(authority,new HashMap<>()));
            }
        }
        return list;
    }

    public Integer getAccessTokenValiditySeconds(){
        return this.accessTokenValidity;
    }

    public Integer getRefreshTokenValiditySeconds(){
        return this.refreshTokenValidity;
    }

    public boolean isAutoApprove(String scope) {
        List<String> autoApproveScopes = Arrays.asList(this.autoapprove.split(","));
        if(CollectionUtils.isEmpty(autoApproveScopes)){
            return false;
        }
        else {
            for(String auto : autoApproveScopes) {
                if (auto.equals("true") || scope.matches(auto)) {
                    return true;
                }
            }
            return false;
        }
    }

    public Map<String, Object> getAdditionalInformation(){
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isNotBlank(this.additionalInformation)){
            for(String pair : this.additionalInformation.split(",")){
                map.put(pair.split(":")[0], pair.split(":")[1]);
            }
        }
        return Collections.unmodifiableMap(map);
    }

    public boolean isSecretRequired() {
        return this.clientSecret != null;
    }

    public boolean isScoped() {
        return StringUtils.isNotBlank(this.scope);
    }

    public Set<String> getRegisteredRedirectUri(){
        Set<String> set = new HashSet<>();
        if(StringUtils.isNotBlank(this.webServerRedirectUri)){
            set.addAll(Arrays.asList(this.webServerRedirectUri.split(",")));
        }
        return set;
    }
}




