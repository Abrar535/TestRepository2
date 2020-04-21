package com.cefalo.backend.controller;

import com.cefalo.backend.model.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;


@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;


    @Test
    public void addNewUser_WhenUniqueUserId_thenReturnUser() throws URISyntaxException
    {
        final String baseUrl = "http://localhost:"+randomServerPort+"/user/registration";
        URI uri = new URI(baseUrl);
        User user = new User( "fuad", "fuadmmnf", "fuadmmnf1234");

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<User> request = new HttpEntity<>(user, headers);

        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        //Verify request succeed
        assertEquals(201, result.getStatusCodeValue());
    }

    @Test
    public void whenDuplicateUserId_ReturnError() throws URISyntaxException
    {
        final String baseUrl = "http://localhost:"+randomServerPort+"/user/registration";
        URI uri = new URI(baseUrl);
        User user = new User( "fuad", "fuadmmnf", "fuadmmnf1234");

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<User> request = new HttpEntity<>(user, headers);
        this.restTemplate.postForEntity(uri, request, String.class);

        request = new HttpEntity<>(user, headers);
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        assertEquals(400, result.getStatusCodeValue());
    }
}