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
import org.springframework.http.MediaType;
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

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService userService;


    private User createUser(String username, String userId, String password) {
        User tempUser = new User();
        tempUser.setName(username);
        tempUser.setUserId(userId);
        tempUser.setPassword(password);
        tempUser.setUpdatedAt(new Date());
        tempUser.setCreatedAt(new Date());

        return tempUser;
    }

    private String generateRequestBody(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String result = ow.writeValueAsString(obj);
        System.out.println("conversion: ");
        System.out.println(result);

        return result;
    }

    @BeforeEach
    public void setUp() {
        User user = createUser("fuad", "fuadmmnf", "fuadqwer1234");
        try {
            given(userService.registerNewUserAccountAfterCheckingUserId(user)).willReturn(Optional.of(user));
        } catch (UserIdExistsException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void registerNewUser_whenUserIdUnique_thenReturnUser()
            throws Exception {
        String testUserId = "fuadmmnf";
        User testUser = createUser("fuad", testUserId, "fuadqwer1234");

        mockMvc.perform(post("/user/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(generateRequestBody(testUser)))
                .andExpect(status().isCreated())
                .andExpect((ResultMatcher) jsonPath("$[0].userId", is(testUserId)));
    }
}