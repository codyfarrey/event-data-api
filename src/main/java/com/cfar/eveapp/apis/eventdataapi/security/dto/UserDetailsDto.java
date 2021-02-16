package com.cfar.eveapp.apis.eventdataapi.security.dto;

import com.cfar.eveapp.apis.eventdataapi.web.dto.Role;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsDto {
    private long id;
    private String email;
    private Collection<Role> roles;

    public UserDetailsDto() {
        this.roles = new ArrayList<>();
    }

    public UserDetailsDto(long id, String email, Collection<Role> roles) {
        this.id = id;
        this.email = email;
        this.roles = roles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}
