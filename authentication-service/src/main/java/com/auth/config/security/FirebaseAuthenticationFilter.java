package com.auth.config.security;

import com.auth.entity.JwtModel;
import com.auth.entity.User;
import com.auth.service.token.TokenService;
import com.auth.service.userservice.UserService;
import com.auth.to.Device;
import com.auth.to.GoogleUserTo;
import com.auth.util.SecurityUtil;
import com.auth.util.converters.UserConverter;
import com.auth.util.exception.AuthenticationException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.nimbusds.jose.JOSEException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Collections;
import java.util.Map;

import static com.auth.entity.Role.ROLE_USER;
import static com.auth.util.Constants.*;
import static org.apache.tomcat.websocket.Constants.AUTHORIZATION_HEADER_NAME;

@Slf4j
public class FirebaseAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationProvider customAuthenticationProvider;

    private final SecurityUtil securityUtil;


    private final TokenService tokenService;

    private final UserService userService;

    public FirebaseAuthenticationFilter(AuthenticationProvider customAuthenticationProvider,
                                        SecurityUtil securityUtil,
                                        TokenService tokenService,
                                        UserService userService) {
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.securityUtil = securityUtil;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String idToken = securityUtil.getTokenFromRequest(request);
        FirebaseToken decodedToken;
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = null;
        try {
            decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            if (decodedToken != null) {
                Map<String, String> firebase = (Map<String, String>) decodedToken.getClaims().get("firebase");
                String signInProvider = firebase.get(SIGN_IN_PROVIDER_CONSTANT);
                if (GOOGLE_COM_GOOGLE_FIREBASE_CONSTANT.equals(signInProvider)) {
                    Device device = securityUtil.getDevice(request);
                    GoogleUserTo signedInUser = new GoogleUserTo();
                    signedInUser.setName(decodedToken.getName());
                    signedInUser.setEmail(decodedToken.getEmail());
                    signedInUser.setPictureUrl(decodedToken.getPicture());
                    signedInUser.setSignInProvider(signInProvider);
                    signedInUser.setDevice(device);
                    usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            signedInUser, decodedToken, Collections.singleton(ROLE_USER));
                }
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            }
        } catch (Exception e) {
            throw new AuthenticationException();
        }
        return customAuthenticationProvider.authenticate(usernamePasswordAuthenticationToken);
    }

    //    проверяю единожды при аутентификации(ищу существующего клиента или создаю нового)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) {
        try {
            String token = null;
            SecretKey secureKey = securityUtil.getSecureKey();
            if (authentication.getAuthorities().contains(ROLE_USER)) {
                token = createUserAndGetToken(authentication, request, secureKey);
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
            response.setHeader(AUTHORIZATION_HEADER_NAME, TOKEN_BEARER_PREFIX + token);
        } catch (Exception e) {
            throw new AuthenticationException();
        }
    }

    private String createUserAndGetToken(Authentication authentication, HttpServletRequest request, SecretKey secureKey) throws ParseException, JOSEException {
        String deviceId = ((GoogleUserTo) authentication.getPrincipal()).getDevice().getDeviceFactoryIdentifier();
        User user = null;
        try {
            user = userService.create(
                    UserConverter.createNewFromTo(
                            (GoogleUserTo) authentication.getPrincipal(),
                            Collections.singleton(ROLE_USER)
                    )
            );
        } catch (Exception e) {
            log.info("error user creating with deviceId {} ", deviceId);
        }
        tokenService.deleteByDeviceId(deviceId);
        String token = securityUtil.generateAccessToken(user.getId().toString(), ROLE_USER, deviceId, secureKey);
        tokenService.save(new JwtModel(token, user.getId(), deviceId, secureKey.getEncoded()));
        return token;
    }
}
