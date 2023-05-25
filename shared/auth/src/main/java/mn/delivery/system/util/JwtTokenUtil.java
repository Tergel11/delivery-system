package mn.delivery.system.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.model.auth.User;
import mn.delivery.system.repository.auth.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author digz6666
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Value("${token.secret}")
    private String secret;

    @Value("${token.expiration}")
    private Long expiration;

    private final UserRepository userRepository;

    private SecretKey secretKey;

    @PostConstruct
    public void initSecretKey() {
        secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String getUsernameFromToken(String token) {
        String username = null;
        Claims claims = getClaimsFromToken(token);
        //log.debug("Got claims from token: " + claims);
        if (claims != null) {
            username = claims.getSubject();
        }
        return username;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created = null;
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            created = new Date((Long) claims.get("iat"));
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expirationDate = null;
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            expirationDate = claims.getExpiration();
        }
        return expirationDate;
    }

    public String refreshToken(String token) {
        String refreshedToken = null;
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            claims.put("iat", new Date());
            refreshedToken = generateToken(claims);
        }
        return refreshedToken;
    }

    public Boolean validateToken(String token) {
        String username = getUsernameFromToken(token);
//        Date created = getCreatedDateFromToken(token);

        User user = userRepository.findByEmailAndDeletedFalse(username);
        if (user != null) {
            return Objects.equals(username, user.getEmail()) && !(isTokenExpired(token));
        } else {
            return !(isTokenExpired(token));
        }
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        if (StringUtils.hasText(token)) {
            claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }
        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate != null && expirationDate.before(new Date());
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);
        claims.put("aud", "WEB");
        claims.put("iss", "Ast Starter");
        claims.put("iat", new Date());
        return generateToken(claims);
    }

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }
}
