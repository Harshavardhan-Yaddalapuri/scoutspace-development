package com.noobprogrammer.scoutspace.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // Allow access to these paths without authentication
                .antMatchers("/api/v1/auth/signup", "/api/v1/auth/signin").permitAll()
                // Restrict access to other paths
                .anyRequest().authenticated().and().formLogin().loginPage("/api/v1/auth/signin").permitAll().and().logout().permitAll();
    }
}
