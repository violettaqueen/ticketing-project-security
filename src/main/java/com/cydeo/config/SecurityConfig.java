package com.cydeo.config;

import com.cydeo.service.SecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
public class SecurityConfig {

    private final SecurityService securityService;
    private final AuthSuccessHandler authSuccessHandler;

    public SecurityConfig(SecurityService securityService, AuthSuccessHandler authSuccessHandler) {
        this.securityService = securityService;
        this.authSuccessHandler = authSuccessHandler;
    }

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
    //when we run security, we need to authorize each, but I need to exclude some pages
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {//what, how and who we authorize

        return http
                .authorizeRequests()                                            //checking for request
                .antMatchers("/user/**").hasAuthority("Admin")      //anything under user controller Admin has to be able to access, Admin need to match with DB
                .antMatchers("/project/**").hasAuthority("Manager") //what can be accessed and by users with the certain role, all functionalities described in User Controller
                .antMatchers("/task/employee/**").hasAuthority("Employee")
                .antMatchers("/task/**").hasAuthority("Manager")
               // .antMatchers("/task/**").hasAnyRole("EMPLOYEE", "ADMIN")     //define more then one role
               // .antMatchers("/task/**").hasAuthority("ROLE_EMPLOYEE")
                .antMatchers(
                "/",                                               //what can be accessed and b
                        "/login",
                        "/fragments/**",
                        "/assets/**",
                        "/images/**"
                 ).permitAll()                                                //authorize these directories for all
                .anyRequest().authenticated()                                 //any request should be authenticated, when form is filled
                .and()
               // .httpBasic()                                                //pop box that comes with spring, but I want my own
                .formLogin()                                                  // to show login page instead the pne Spring is provided
                    .loginPage("/login")
                    //.defaultSuccessUrl("/welcome")                          //when we login, land to welcome page
                .successHandler(authSuccessHandler)                           //
                .failureUrl("/login?error = true")         //navigate back to login page
                    .permitAll()                                              //accessible to use
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) //where logout button
                .logoutSuccessUrl("/login")
                .and()
                .rememberMe()
                    .tokenValiditySeconds(1000)  //how long activate yourself
                    .key("cydeo") //keeping info based on
                    .userDetailsService(securityService) //remember who? remember me, to capture the logged in user
                .and()
                .build();
    }


}
