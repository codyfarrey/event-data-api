package com.cfar.eveapp.apis.eventdataapi.config;

import com.cfar.eveapp.apis.eventdataapi.security.filters.JWTAuthenticationFilter;
import com.cfar.eveapp.apis.eventdataapi.security.filters.JWTRequestFilter;
import com.cfar.eveapp.apis.eventdataapi.web.dto.ErrorResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;

@EnableWebSecurity
@Slf4j
public class EventDataSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().httpBasic().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/events/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTRequestFilter(authenticationManager()))
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPointEventImpl());
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPointEventImpl() {
        return (request, response, authException) -> {
            log.debug("authenticationEntryPoint called. Rejecting access.");

            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            ErrorResponseDto responseDto = new ErrorResponseDto();
            responseDto.setErrorId(1001);
            responseDto.setErrorMessage(authException.getMessage());
            responseDto.setRequestId("idh9821yhd1");

            try {
                String jsonString =  new ObjectMapper().writeValueAsString(responseDto);

                response.getWriter().println(jsonString);
                response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            } catch (JsonProcessingException e) {
                log.warn("Exception while processing json to string.");
            }
        };
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return null;
            }
        });
    }
}