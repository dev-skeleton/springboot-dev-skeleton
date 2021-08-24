package com.example.skeleton.component.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.skeleton.exception.ServerSideException;
import com.example.skeleton.model.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Optional;


@Component
public class JwtTokenProvider {

    static private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    // TODO fixme

    static public String TokenHeader = "Authorization";
    static public String TokenPrefix = "Bearer ";
    static public String UserSecret = "just-some-admin-jwt-secret";
    static public String Issuer = "MEC-platform";

    static public String issue(@NotNull Object entity) {
        if (entity instanceof User) {
            return JWT.create()
                    .withIssuer(Issuer)
                    .withSubject(((User) entity).getName())
                    .withIssuedAt(new Date())
                    .withExpiresAt(DateUtils.addDays(new Date(), 1))
                    .sign(Algorithm.HMAC256(UserSecret));
        }

        throw ServerSideException.InternalError(String.format("cannot issue jwt for entity %s", entity.getClass().getSimpleName()));
    }

    @Validated
    static public String issue(@NotNull Object entity, @NotBlank String secret) {
        if (entity instanceof User) {
            return JWT.create()
                    .withIssuer(Issuer)
                    .withSubject(((User) entity).getName())
                    .withIssuedAt(new Date())
                    .withExpiresAt(DateUtils.addDays(new Date(), 1))
                    .sign(Algorithm.HMAC256(secret));
        }

        throw ServerSideException.InternalError(String.format("cannot issue jwt for entity %s", entity.getClass().getSimpleName()));
    }

    static public DecodedJWT resolve(HttpServletRequest request) {
        return JWT.decode(getToken(request));
    }

    static public Optional<DecodedJWT> tryGetJwtToken(HttpServletRequest request) {
        try {
            var decode = JWT.require(Algorithm.HMAC256(UserSecret)).withIssuer(Issuer).build().verify(getToken(request));
            return Optional.of(decode);
        } catch (Exception exception) {
            logger.info("validate request [{} ==> {} {}] failed, {}", request.getRemoteAddr(), request.getMethod(), request.getRequestURI(), exception.getMessage());
            return Optional.empty();
        }
    }

    static public String AccountName(@NotNull DecodedJWT jwt) {
        return jwt.getSubject();
    }

    static private String getToken(HttpServletRequest request) {
        if (StringUtils.startsWith(request.getHeader(TokenHeader), TokenPrefix)) {
            return StringUtils.removeStart(request.getHeader(TokenHeader), TokenPrefix);
        } else {
            return "";
        }
    }

}
