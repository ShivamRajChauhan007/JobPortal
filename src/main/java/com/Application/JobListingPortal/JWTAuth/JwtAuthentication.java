package com.Application.JobListingPortal.JWTAuth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;


public class JwtAuthentication implements Authentication {
    private String email;
    private String role;
    private boolean authenticated = true;  // Default to authenticated

    public JwtAuthentication(String email, String role) {
        this.email = email;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;  // You can return actual roles/authorities if needed
    }

    @Override
    public Object getCredentials() {
        return null;  // JWT-based authentication doesn't require credentials
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return email;  // Typically, the principal is the user identifier
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        this.authenticated = authenticated;
    }

    @Override
    public String getName() {
        return email;
    }
}
