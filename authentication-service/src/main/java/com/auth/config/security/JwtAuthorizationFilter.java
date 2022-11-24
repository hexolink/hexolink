package com.auth.config.security;

import com.auth.entity.User;
import com.auth.service.userservice.UserService;
import com.auth.util.SecurityUtil;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

import static com.auth.entity.Role.ROLE_ANONYMOUS;
import static com.auth.entity.Role.ROLE_USER;
import static com.auth.util.Constants.ROLE_CLIENT_INFO;
import static com.auth.util.Constants.TOKEN_BEARER_PREFIX;
import static org.apache.tomcat.websocket.Constants.AUTHORIZATION_HEADER_NAME;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final SecurityUtil securityUtil;

    private final UserService userService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  UserService userService,
                                  SecurityUtil securityUtil) {
        super(authenticationManager);
        this.userService = userService;
        this.securityUtil = securityUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) {
        try {
            String header = request.getHeader(AUTHORIZATION_HEADER_NAME);
            if (header == null || !header.startsWith(TOKEN_BEARER_PREFIX)) {
                chain.doFilter(request, response);
                return;
            }

            UsernamePasswordAuthenticationToken authorization = authorizeRequest(request);
            SecurityContextHolder.getContext().setAuthentication(authorization);
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UsernamePasswordAuthenticationToken authorizeRequest(HttpServletRequest request) {
        try {
            String token = securityUtil.getTokenFromRequest(request);
            Map<String, Object> clientInfo = securityUtil.getClientInfo(token);
            if (ROLE_USER.name().equals(clientInfo.get(ROLE_CLIENT_INFO))) {
                User user = userService.getById(Integer.parseInt((String) clientInfo.get("id")));
                return new UsernamePasswordAuthenticationToken(
                        user, null, Collections.singleton(ROLE_USER)
                );
            } else {
                throw new AuthorizationServiceException("fsdfsd11");
            }
        } catch (Exception e) {
            throw new AuthorizationServiceException("fsdfsd");
        }
    }
}

