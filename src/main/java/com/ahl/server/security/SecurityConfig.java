package com.ahl.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
                .httpBasic()
                .and()
                .authorizeRequests().anyRequest().authenticated()
//                .antMatchers("/api/tournament").hasRole("ADMIN")
//                .antMatchers("/api/tournament/*").hasRole("ADMIN")
//                .antMatchers("/api/team").hasRole("ADMIN")
//                .antMatchers("/api/team/*").hasRole("ADMIN")
//                .antMatchers("/api/player-relation").hasRole("ADMIN")
//                .antMatchers("/api/player-relation/*").hasRole("ADMIN")
//                .antMatchers("/api/match").hasRole("ADMIN")
//                .antMatchers("/api/match/*").hasRole("ADMIN")
//                .antMatchers("/api/start-match/*").hasRole("ADMIN")
//                .antMatchers("/api/end-match/*").hasRole("ADMIN")
//                .antMatchers("/api/goal").hasRole("ADMIN")
//                .antMatchers("/api/goal/*").hasRole("ADMIN")
//                .antMatchers("/api/player").hasRole("ADMIN")
//                .antMatchers("/api/player/*").hasRole("ADMIN")
                .and()
                .csrf()
                .disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception
    {
        auth.inMemoryAuthentication()
                .withUser("ahl2020")
                .password("{noop}ahlserver@bala")
                .roles("ADMIN");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/api/tournaments","/api/teams","/api/matches", "/api/goals", "api/players");
    }
}