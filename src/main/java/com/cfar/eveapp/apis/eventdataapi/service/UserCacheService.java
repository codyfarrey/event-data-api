package com.cfar.eveapp.apis.eventdataapi.service;

import com.cfar.eveapp.apis.eventdataapi.security.dto.UserDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserCacheService {

    @Autowired
    private UserDetailsDto userDetails;

    public UserDetailsDto getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetailsDto userDetails) {
        this.userDetails = userDetails;
    }
}
