package com.cfar.eveapp.apis.eventdataapi.config;

import com.cfar.eveapp.apis.eventdataapi.security.dto.UserDetailsDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

@Configuration
@Slf4j
public class EventDataAppConfig {
    @Autowired
    ConfigurableEnvironment configurableEnvironment;

    @Bean
    public UserDetailsDto userDetails() {
        return new UserDetailsDto();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
