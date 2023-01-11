package com.example.businessapp.security.proxy;

import com.example.businessapp.ds.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthenticationServerProxy {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${auth.server.base.url}")
    private String baseUrl;

    public void sendAuth(String username,String password){
        String url = baseUrl + "/user/auth";
        var body = new User();
        body.setUsername(username);
        body.setPassword(password);
        var request = new HttpEntity<>(body);
        restTemplate.postForEntity(url,request, Void.class);
    }
}
