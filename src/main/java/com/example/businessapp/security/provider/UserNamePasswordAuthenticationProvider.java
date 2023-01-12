package com.example.businessapp.security.provider;

import com.example.businessapp.security.UsernamePasswordAuthentication;
import com.example.businessapp.security.proxy.AuthenticationServerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class UserNamePasswordAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AuthenticationServerProxy proxy;
    //curl -H "username:john" -H "password:12345" http://localhost:8090/login
    //curl -v -H "username:john" -H "code:8547" http://localhost:8090/login
    // curl -v -H " Authorization: eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6ImpvaG4ifQ.zfrXY-OOqYMVThzZwZRqp9byVaUzfRJPETOcdvMjQUceGJ6dvTixemHK7AwCpZAET-SDoBp3kWRENL8d7O0HVQ" http://localhost:8090/login

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());
        proxy.sendAuth(username,password);
        return new UsernamePasswordAuthenticationToken(username,password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthentication.class.isAssignableFrom(authentication);
    }
}
