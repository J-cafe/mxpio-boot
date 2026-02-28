package com.mxpioframework.security.matcher;

import org.springframework.security.web.util.matcher.RequestMatcher;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public final class CookieRequestMatcher implements RequestMatcher {

    private final String expectedCookieName;

    public CookieRequestMatcher(String expectedCookieName) {
        this.expectedCookieName = expectedCookieName;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
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
