package com.staxter.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource(name = "userDetailService")
    private UserDetailsService userDetailsService;

    /* Used for password encryption during user create also for password decryption during user login */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /* Handles user authentication through RegistrationUserDetailService */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /* Various Spring Security Configurations */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().disable();
        /* user authentatication not required to access /login, /logout and /userservice/register */
        http.authorizeRequests().antMatchers("/login", "/logout", "/userservice/register").permitAll()
            .antMatchers("/").authenticated()
            .and()
            .formLogin()
            /* login page is located in /login */
            .loginPage("/login")
            /* If login successful redirect to welcome page */
            .defaultSuccessUrl("/")
            /* If login not successful redirect to /login-error and display a message */
            .failureUrl("/login-error")
            /* the name of the username and password fields in the login template */
            .usernameParameter("username")
            .passwordParameter("password")
            .and()
            .logout()
            /* Upon logout redirect to /logoutSuccessful and display a message */
            .logoutSuccessUrl("/logoutSuccessful")
            /* Clear http session data */
            .invalidateHttpSession(true)
            .and()
            .exceptionHandling();
    }
}
