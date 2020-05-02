package com.cefalo.backend.controller;

import com.cefalo.backend.exception.UserIdExistsException;
import com.cefalo.backend.model.User;
import com.cefalo.backend.model.auth.AuthenticationRequest;
import com.cefalo.backend.model.auth.AuthenticationResponse;
import com.cefalo.backend.service.IUserService;
import com.cefalo.backend.service.MyUserDetailsService;
import com.cefalo.backend.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class AuthControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @BeforeEach
    public void setUp(){
        try {
            userService.registerNewUserAccountAfterCheckingUserId(new User("fuad", "fuadmmnf", "fuadqwer1234"));
        } catch (UserIdExistsException e) {
//            e.printStackTrace();
        }
    }


    @Test
    public void authenticateUser_whenCredsProvided_thenReturnJWT() throws URISyntaxException
    {
        final String expectedUserId = "fuadmmnf";

        final String baseUrl = "http://localhost:"+randomServerPort+"/user/authenticate";
        URI uri = new URI(baseUrl);
        AuthenticationRequest authenticationRequest = new AuthenticationRequest( "fuadmmnf", "fuadqwer1234");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<AuthenticationRequest> request = new HttpEntity<>(authenticationRequest, headers);
        ResponseEntity<AuthenticationResponse> result = this.restTemplate.postForEntity(uri, request, AuthenticationResponse.class);

        assertEquals(expectedUserId, jwtUtil.extractUserId(result.getBody().getJwt()));
    }

    @Test
    public void authFail_whenBadCredsProvided_thenReturnMessage() throws URISyntaxException
    {
        final String expectedUserId = "fuadmmnf";

        final String baseUrl = "http://localhost:"+randomServerPort+"/user/authenticate";
        URI uri = new URI(baseUrl);
        AuthenticationRequest authenticationRequest = new AuthenticationRequest( "fuadnf", "fuadqwer34");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<AuthenticationRequest> request = new HttpEntity<>(authenticationRequest, headers);
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
    }
}