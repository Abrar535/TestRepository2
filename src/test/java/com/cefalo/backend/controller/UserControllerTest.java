package com.cefalo.backend.controller;

import com.cefalo.backend.exception.UserIdExistsException;
import com.cefalo.backend.model.User;
import com.cefalo.backend.service.IUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;


    private User createUser(String username, String userId, String password) {
        User tempUser = new User();
        tempUser.setName(username);
        tempUser.setUserId(userId);
        tempUser.setPassword(password);
        tempUser.setUpdatedAt(new Date());
        tempUser.setUpdatedAt(new Date());

        return tempUser;
    }

//    @Test
//    public void registerNewUser_whenUserIdUnique_thenReturnUser()
//            throws Exception {
//        String testUserId = "fuadmmnf";
//        User testUser = createUser("fuad", testUserId, "fuadqwer1234");
//
//    }

    @Test
    public void addNewUser_WhenUniqueUserId_thenReturnUser() throws URISyntaxException
    {
        final String baseUrl = "http://localhost:"+randomServerPort+"/user/registration";
        URI uri = new URI(baseUrl);
        User user = createUser( "fuad", "fuadmmnf", "fuadmmnf1234");

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
        User user = createUser( "fuad", "fuadmmnf", "fuadmmnf1234");

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<User> request = new HttpEntity<>(user, headers);
        this.restTemplate.postForEntity(uri, request, String.class);

        request = new HttpEntity<>(user, headers);
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        assertEquals(400, result.getStatusCodeValue());
    }
}