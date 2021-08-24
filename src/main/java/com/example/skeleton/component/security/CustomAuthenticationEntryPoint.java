package com.example.skeleton.component.security;

import com.example.skeleton.util.Api;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    //commence()方法。 这个方法主要是,用户未认证访问资源时,所做的处理
    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) throws IOException, ServletException {
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpStatus.FORBIDDEN.value());
        res.getWriter().write(Api.SimpleStatusMsg(HttpStatus.FORBIDDEN.value(), authException.getLocalizedMessage()));
    }
}
