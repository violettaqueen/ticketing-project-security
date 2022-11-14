package com.cydeo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Configuration
public class SecurityConfig {

//    @Bean
//    public UserDetailsService userDetailService(PasswordEncoder encoder) {
//
//        List<UserDetails> userList = new ArrayList<>();
//        userList.add(
//                new User("Mike", encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))));
//
//        userList.add(
//                new User("Ozzy", encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_MANAGER"))));
//
//        return new InMemoryUserDetailsManager(userList);  //save in memory
//    }

    // spring providing us a class security filter chain
    //when we run security, we need to authorize each authorize, but I need to exclude some pages
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeRequests()
                .antMatchers("/user/**").hasRole("ADMIN")  //anything under user controller Admin has to be able to access
                .antMatchers("/project/**").hasRole("MANAGER")
                .antMatchers("/task/employee/**").hasRole("EMPLOYEE")
                .antMatchers("/task/**").hasRole("MANAGER")
               // .antMatchers("/task/**").hasAnyRole("EMPLOYEE", "ADMIN")
               // .antMatchers("/task/**").hasAuthority("ROLE_EMPLOYEE")
                .antMatchers(
                "/",
                        "/login",
                        "/fragments/**",
                        "/assets/**",
                        "/images/**"
                 ).permitAll()
                .anyRequest().authenticated()
                .and()
               // .httpBasic()//pop box that comes with spring, but I want my own
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/welcome")
                .failureUrl("/login?error = true")
                    .permitAll()
                .and().build();
    }


}
