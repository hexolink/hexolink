package com.auth.config.security;

import com.auth.repository.tokenrepository.CrudTokenRepository;
import com.auth.service.token.TokenService;
import com.auth.service.userservice.UserService;
import com.auth.util.JacksonObjectMapper;
import com.auth.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationProvider customAuthenticationProvider;

    private final SecurityUtil securityUtil;

    private final UserService userService;

    private final TokenService tokenService;

    private final JacksonObjectMapper jacksonObjectMapper;

    @Autowired
    public SecurityConfig(AuthenticationProvider customAuthenticationProvider,
                          SecurityUtil securityUtil,
                          UserService userService,
                          TokenService tokenService,
                          JacksonObjectMapper jacksonObjectMapper) {
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.securityUtil = securityUtil;
        this.userService = userService;
        this.tokenService = tokenService;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    @Bean
    public AuthenticationEntryPoint restAuthenticationEntryPoint() {
        return (httpServletRequest, httpServletResponse, e) -> {
            Map<String, Object> errorObject = new HashMap<>();
            int errorCode = 401;
            errorObject.put("message", "Access Denied");
            errorObject.put("error", HttpStatus.UNAUTHORIZED);
            errorObject.put("code", errorCode);
            errorObject.put("timestamp", new Timestamp(new Date().getTime()));
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.setStatus(errorCode);
            httpServletResponse.getWriter().write(jacksonObjectMapper.writeValueAsString(errorObject));
        };
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/auth").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(getFirebaseAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilter(new JwtAuthorizationFilter(authenticationManager(), userService, securityUtil));
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Arrays.asList((AuthenticationProvider) customAuthenticationProvider));
    }

    public FirebaseAuthenticationFilter getFirebaseAuthenticationFilter() {
        final FirebaseAuthenticationFilter filter = new FirebaseAuthenticationFilter(
                customAuthenticationProvider,
                securityUtil,
                tokenService,
                userService
        );
        filter.setFilterProcessesUrl("/auth");
//        filter.setContinueChainBeforeSuccessfulAuthentication(true);
        return filter;
    }
}

