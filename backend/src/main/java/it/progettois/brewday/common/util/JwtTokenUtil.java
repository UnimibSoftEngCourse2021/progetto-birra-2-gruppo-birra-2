package it.progettois.brewday.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import static it.progettois.brewday.common.constant.SecurityConstants.SECRET;
import static it.progettois.brewday.common.constant.SecurityConstants.TOKEN_PREFIX;


@Component
public class JwtTokenUtil {

    public String getUsernameFromToken(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();
    }
}
