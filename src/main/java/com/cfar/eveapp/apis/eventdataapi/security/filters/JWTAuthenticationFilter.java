package com.cfar.eveapp.apis.eventdataapi.security.filters;

import com.cfar.eveapp.apis.eventdataapi.security.dto.UserDetailsDto;
import com.cfar.eveapp.apis.eventdataapi.security.jwt.JWTTokenProvider;
import com.cfar.eveapp.apis.eventdataapi.service.UserCacheService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    JWTTokenProvider jwtTokenProvider;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            log.info("Attempting auth");
            String jwt = request.getHeader("Authorization").substring("Bearer ".length());
            UserDetailsDto creds = new ObjectMapper().readValue(jwtTokenProvider.extractUserDetails(jwt), UserDetailsDto.class);
            log.debug("Got creds from jwt: " + creds.getEmail());
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmail(), null, new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("Attempting auth");
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            String jwt = authorizationHeader.substring("Bearer ".length());
            String userDetailsJsonString = jwtTokenProvider.extractUserDetails(jwt);

            UserDetailsDto userDetailsDto = new ObjectMapper().readValue(userDetailsJsonString, UserDetailsDto.class);

            // userDetailsCache.setUserCache(userDetailsDto);
        }

        super.successfulAuthentication(request, response, chain, authResult);
    }
}
