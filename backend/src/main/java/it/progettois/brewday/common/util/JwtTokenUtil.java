package it.progettois.brewday.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static it.progettois.brewday.common.constant.SecurityConstants.*;


@Component
public class JwtTokenUtil {

    public String getUsername(HttpServletRequest request) {
        return JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(request.getHeader(HEADER_STRING).replace(TOKEN_PREFIX, ""))
                .getSubject();
    }

    public String getUsername(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();
    }
}
