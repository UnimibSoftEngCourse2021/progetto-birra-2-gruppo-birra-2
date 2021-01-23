package it.progettois.brewday.common.constant;

public class SecurityConstants {

    private SecurityConstants() {
    }

    public static final String SECRET = "SoftEngProject"; //Secret key to generate JWTs
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/brewer";
}
