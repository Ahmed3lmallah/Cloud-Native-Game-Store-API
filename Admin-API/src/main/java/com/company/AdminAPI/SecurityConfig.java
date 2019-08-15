package com.company.AdminAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder authBuilder) throws Exception {

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        authBuilder.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(
                        "select username, password, enabled from users where username = ?")
                .authoritiesByUsernameQuery(
                        "select username, authority from authorities where username = ?")
                .passwordEncoder(encoder);

    }

    public void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.httpBasic();

        httpSecurity.authorizeRequests()
                .mvcMatchers(HttpMethod.PUT,"/customers/{id}").hasAuthority("ROLE_TEAMLEAD")
                .mvcMatchers(HttpMethod.PUT,"/products/{id}").hasAuthority("ROLE_TEAMLEAD")
                .mvcMatchers(HttpMethod.PUT,"/inventory/{id}").hasAuthority("ROLE_TEAMLEAD")
                .mvcMatchers(HttpMethod.PUT,"/invoices/{id}").hasAuthority("ROLE_TEAMLEAD")
                .mvcMatchers(HttpMethod.PUT,"/levelup/{id}").hasAuthority("ROLE_TEAMLEAD")
                .mvcMatchers(HttpMethod.POST,"/customers").hasAuthority("ROLE_TEAMLEAD")
                .mvcMatchers(HttpMethod.POST,"/products").hasAuthority("ROLE_MANAGER")
                .mvcMatchers(HttpMethod.POST,"/inventory").hasAuthority("ROLE_MANAGER")
                .mvcMatchers(HttpMethod.POST,"/invoices").hasAuthority("ROLE_MANAGER")
                .mvcMatchers(HttpMethod.POST,"/levelup").hasAuthority("ROLE_MANAGER")
                .mvcMatchers(HttpMethod.DELETE,"/customers/{id}").hasAuthority("ROLE_ADMIN")
                .mvcMatchers(HttpMethod.DELETE,"/products/{id}").hasAuthority("ROLE_ADMIN")
                .mvcMatchers(HttpMethod.DELETE,"/inventory/{id}").hasAuthority("ROLE_ADMIN")
                .mvcMatchers(HttpMethod.DELETE,"/invoices/{id}").hasAuthority("ROLE_ADMIN")
                .mvcMatchers(HttpMethod.DELETE,"/levelup/{id}").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated();

        httpSecurity
                .logout()
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .deleteCookies("JSESSIONID")
                .deleteCookies("XSRF-TOKEN")
                .invalidateHttpSession(true);

        httpSecurity
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

    }
}