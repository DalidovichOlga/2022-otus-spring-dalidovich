package ru.otus.spring.booklib.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.otus.spring.booklib.domain.UserGrant;
import ru.otus.spring.booklib.dto.UserDto;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String SECRET = "Супер секретный ключ по-русски";
    public static final int TOKEN_TIME_LIVE = 900000;

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);

        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/api/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {

        try {
            UserDto creds = new ObjectMapper()
                    .readValue(req.getInputStream(), UserDto.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {
        String token = JWT.create()
                .withSubject(((UserGrant) auth.getPrincipal()).getUsername())
                .withClaim("roles", auth.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.joining(","))
                )
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_TIME_LIVE))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));

        String body = "Bearer " + token;

        res.getWriter().write(body);
        res.getWriter().flush();
    }
}