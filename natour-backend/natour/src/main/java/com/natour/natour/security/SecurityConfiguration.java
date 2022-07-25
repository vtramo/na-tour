package com.natour.natour.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.natour.natour.security.authproviders.DefaultAuthenticationProvider;
import com.natour.natour.security.authproviders.GoogleAuthenticationProvider;
import com.natour.natour.security.handler.LoginSuccessHandler;
import com.natour.natour.security.handler.LoginUnsuccessHandler;
import com.natour.natour.service.ApplicationUserDetailsService;

@Configuration
public class SecurityConfiguration {

    @Autowired
    private GoogleAuthenticationProvider googleAuthenticationProvider;

    @Autowired
    private DefaultAuthenticationProvider defaultAuthenticationProvider;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests()
            .antMatchers("/login").permitAll()
            .anyRequest().authenticated()
            .and().formLogin()       
            .successHandler(new LoginSuccessHandler())
            .failureHandler(new LoginUnsuccessHandler())
            .and().oauth2Login()
            .successHandler(new LoginSuccessHandler())
            .failureHandler(new LoginUnsuccessHandler())
            .and()
            .userDetailsService(userDetailsService())
            .authenticationProvider(defaultAuthenticationProvider)
            .authenticationProvider(googleAuthenticationProvider);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/images/*", "/h2-console/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new ApplicationUserDetailsService();
    }
}
