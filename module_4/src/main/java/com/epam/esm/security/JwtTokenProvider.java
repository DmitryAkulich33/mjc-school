package com.epam.esm.security;

import com.epam.esm.domain.Role;
import com.epam.esm.exceptions.JwtAuthenticationException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNoneBlank;

@Component
public class JwtTokenProvider {
    private final JwtUserDetailsService jwtUserDetailsService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_BEARER = "Bearer ";

    @Value("${jwt.token.secret}")
    private String secret;              // кодовое слово, которое потом потребуется для расшифровки
    @Value("${jwt.token.expired}")
    private long validityMilliseconds;

    @Autowired
    public JwtTokenProvider(JwtUserDetailsService jwtUserDetailsService) {
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes()); // переводим в спец формат
    }

    public String createToken(String login, List<Role> roles) {

        Claims claims = Jwts.claims().setSubject(login); // создаем заявку токена
        claims.put("roles", getRoleNames(roles));

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityMilliseconds);

        return Jwts.builder()                     // создаем токен
                .setClaims(claims)                // добавил логин пользователя, чтобы потом его оттуда забрать в фильтре, когда пользователь будет делать запрос
                .setIssuedAt(now)                 // время создания токена
                .setExpiration(validity)          // истечение срока
                .signWith(SignatureAlgorithm.HS256, secret) // принимает на вход алгоритм подписи и кодовое слово, которое потом потребуется для расшифровки
                .compact();
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);                   // создаем объект для аутентификации
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUsername(String token) { // информация о логине пользователя
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (isNoneBlank(bearerToken) && bearerToken.startsWith(AUTHORIZATION_BEARER)) {
            return bearerToken.substring(7);
        }

        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("message.invalid_token");
        }
    }

    private List<String> getRoleNames(List<Role> userRoles) {
        return userRoles.stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }
}
