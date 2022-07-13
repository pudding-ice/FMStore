package com.myjava.core.cas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CasAuthenticationRedirectextends extends CasAuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(CasAuthenticationRedirectextends.class);

    private String serviceUrlBak = null;

    @Override
    protected String createServiceUrl(final HttpServletRequest request, final HttpServletResponse response) {
        if (serviceUrlBak == null) {
            serviceUrlBak = getServiceProperties().getService();
        }
        String ctx = request.getContextPath();
        String requestURI = request.getRequestURI();
        requestURI = requestURI.substring(requestURI.indexOf(ctx) + ctx.length(), requestURI.length());
        return serviceUrlBak + requestURI;
    }

}

