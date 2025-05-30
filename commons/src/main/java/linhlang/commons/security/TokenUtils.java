package linhlang.commons.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenUtils {

    private final SecurityProperties properties;

    public String generateToken(TokenUser tokenUser, long lifeTime) {
        Instant currentTime = Instant.now();
        SecretKey secretKey = Keys.hmacShaKeyFor(properties.getSecretKey().getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .signWith(secretKey)
                .subject(tokenUser.getUsername())
                .claim("accountId", tokenUser.getAccountId())
                .claim("roles", StringUtils.join(tokenUser.getRoles(), ","))
                .issuedAt(Date.from(currentTime))
                .expiration(Date.from(currentTime.plusSeconds(lifeTime)))
                .compact();
    }

    public TokenUser parseToken(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(properties.getSecretKey().getBytes(StandardCharsets.UTF_8));
        try {
            Jws<Claims> claimsJws = Jwts.parser().verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);

            return new TokenUser(
                    claimsJws.getPayload().get("accountId", String.class),
                    claimsJws.getPayload().getSubject(),
                    Arrays.asList(claimsJws.getPayload().get("roles", String.class).split(","))
            );
        } catch (Exception e) {
            log.warn("Token invalid", e);
            return null;
        }
    }
}
