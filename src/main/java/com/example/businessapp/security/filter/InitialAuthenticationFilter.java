package com.example.businessapp.security.filter;

import com.example.businessapp.security.OtpAuthentication;
import com.example.businessapp.security.UsernamePasswordAuthentication;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class InitialAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private AuthenticationManager manager;

    @Value("${jwt.singing.key}")
    private String singingKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username = request.getHeader("username");
        String password = request.getHeader("password");
        String code = request.getHeader("code");

        if (code == null){
            Authentication a = new UsernamePasswordAuthentication(username, password);
            manager.authenticate(a);
        }else {
            Authentication a = new OtpAuthentication(username, code);
            a = manager.authenticate(a);
            SecretKey key = Keys.hmacShaKeyFor(
                    singingKey.getBytes(
                            StandardCharsets.UTF_8
                    )
            );
            String jwt = Jwts.builder()
                    .setClaims(Map.of("username",username))
                    .signWith(key)
                    .compact();
            response.setHeader("Authorization",jwt);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath()
                .equals("/login"); //only works in login page.
    }
}
