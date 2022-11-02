package com.restful.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restful.models.Credentials;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final long EXPIRATION_TIME = 900_000;
    public static final String SECRET = "QVBJIFJFU1RmdWwgVXNpbmcgU3ByaW5nIEJvb3Q=";

    private final AuthenticationManager authentication;

    public JwtAuthenticationFilter(AuthenticationManager authentication) {
        this.authentication = authentication;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try{
            Credentials creds = new ObjectMapper().readValue(request.getInputStream(), Credentials.class);

            UsernamePasswordAuthenticationToken authResult = new UsernamePasswordAuthenticationToken(
                    creds.getUsername(), creds.getPassword()
            );

            return authentication.authenticate(authResult);
        }catch(IOException | AuthenticationException e) {
            // AuthenticationException: Fail to authenticate
            // IOException: "Bad credentials"
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        String username = ((UserDetails) authResult.getPrincipal()).getUsername();

        String token = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));

        String body = username + " " + token;

        response.setHeader(HttpHeaders.AUTHORIZATION, body);
    }
}
