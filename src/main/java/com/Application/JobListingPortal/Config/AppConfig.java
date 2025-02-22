package com.Application.JobListingPortal.Config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.Application.JobListingPortal.JWTAuth.JwtFilter;

@Configuration
public class AppConfig {
    private final JwtFilter jwtFilter;

    public AppConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public ModelMapper getModelMapper(){
        return new ModelMapper();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Disable CSRF protection
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/jobListingpPortal/*","jobListingpPortal/list").permitAll()  // Public APIs
                //.requestMatchers("/api/jobs/post", "/api/jobs/update/**", "/api/jobs/delete/**").hasAuthority("EMPLOYER") // Employer-only APIs
                .anyRequest().authenticated()  // All other requests require authentication
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Stateless JWT authentication
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);  // Add our JWT filter

        return http.build();
    }

}
