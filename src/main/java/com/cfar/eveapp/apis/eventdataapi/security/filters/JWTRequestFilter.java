package com.cfar.eveapp.apis.eventdataapi.security.filters;

import com.cfar.eveapp.apis.eventdataapi.security.dto.UserDetailsDto;
import com.cfar.eveapp.apis.eventdataapi.security.jwt.JWTTokenProvider;
import com.cfar.eveapp.apis.eventdataapi.service.UserCacheService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class JWTRequestFilter extends BasicAuthenticationFilter {

    private JWTTokenProvider jwtTokenProvider;
    private UserCacheService userCacheService;

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    public JWTRequestFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);

        jwtTokenProvider = new JWTTokenProvider();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            log.info("Could not find Authorization token in headers");

            filterChain.doFilter(request, response);
            return;
        }

        try {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch(SignatureException e) {
            log.error("Error while reading JWT", e);
            filterChain.doFilter(request, response);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) throws SignatureException {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);


        if (authorizationHeader != null) {
            String jwt = authorizationHeader.substring(BEARER_PREFIX.length());

            String userDetailsJsonString = jwtTokenProvider.extractUserDetails(jwt);

            logger.info("extracted output from JWT: " + userDetailsJsonString);

            // map request into
            try {
                UserDetailsDto userDetails = new ObjectMapper().readValue(userDetailsJsonString, UserDetailsDto.class);

                if (userDetails != null && jwtTokenProvider.validateToken(jwt, userDetails)) {
                    ServletContext servletContext = request.getServletContext();
                    WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
                    userCacheService = webApplicationContext.getBean(UserCacheService.class);

                    userCacheService.setUserDetails(userDetails);

                    return new UsernamePasswordAuthenticationToken(userDetails.getEmail(), null, new ArrayList<>());
                }
            } catch (JsonProcessingException e) {
                log.error("Error mapping JWT body to Object", e);
            }
            return null;
        }
        return null;
    }
}
