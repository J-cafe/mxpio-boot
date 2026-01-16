package com.mxpioframework.security.matcher;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public final class CookieRequestMatcher implements RequestMatcher {

    private final String expectedCookieName;

    public CookieRequestMatcher(String expectedCookieName) {
        this.expectedCookieName = expectedCookieName;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(ArrayUtils.isEmpty(cookies)){
            return false;
        }
        for (Cookie cookie : cookies) {
            if (expectedCookieName.equals(cookie.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "CookieRequestMatcher [expectedCookieName=" + this.expectedCookieName +"]";
    }
}
