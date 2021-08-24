package com.example.skeleton.component.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.skeleton.constant.RestfulApiVersion.ANT_WHITE_LIST;
import static com.example.skeleton.constant.RestfulApiVersion.REG_WHITE_LIST;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final jwtFilter jwtFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    public WebSecurity(jwtFilter jwtFilter, AuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtFilter = jwtFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        var policy = httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable();
        ANT_WHITE_LIST.forEach(httpMethodStringPair -> {
            try {
                policy.authorizeRequests().antMatchers(httpMethodStringPair.getKey(), httpMethodStringPair.getValue()).permitAll();
            } catch (Exception exception) {
                exception.printStackTrace();
                assert false;
            }
        });
        REG_WHITE_LIST.forEach(httpMethodStringPair -> {
            try {
                policy.authorizeRequests().regexMatchers(httpMethodStringPair.getKey(), httpMethodStringPair.getValue()).permitAll();
            } catch (Exception e) {
                e.printStackTrace();
                assert false;
            }
        });
        policy.anonymous();
        policy.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        policy.authorizeRequests().anyRequest().authenticated().and().addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }


}

