package com.example.skeleton.component.security;

import com.example.skeleton.component.jwt.JwtTokenProvider;
import com.example.skeleton.constant.RestfulApiVersion;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.skeleton.constant.RestfulApiVersion.REG_WHITE_LIST;


@Component
public class jwtFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    public jwtFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (RestfulApiVersion.ANT_WHITE_LIST.stream().map(p -> new AntPathRequestMatcher(p.getValue(), p.getKey().toString())).anyMatch(matcher -> matcher.matches(request))) {
            filterChain.doFilter(request, response);
            return;
        }
        if (REG_WHITE_LIST.stream().map(p -> new RegexRequestMatcher(p.getValue(), p.getKey().toString())).anyMatch(matcher -> matcher.matches(request))) {
            filterChain.doFilter(request, response);
            return;
        }
        var token = JwtTokenProvider.tryGetJwtToken(request);
        if (token.isPresent()) {
            var userDetail = userDetailsService.loadUserByUsername(JwtTokenProvider.AccountName(token.get()));
            var auth = new UsernamePasswordAuthenticationToken(userDetail.getUsername(), userDetail.getPassword(), userDetail.getAuthorities());
            auth.setDetails(auth);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
