package com.cefalo.backend.repository;

import com.cefalo.backend.model.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;


    @Test
    public void findById_whenUserIdPasswordCorrent_thenReturnUser() {
        //given
        User newUser = new User("fuad", "fuadmmnf", "fuadqwer1234");
        testEntityManager.persist(newUser);
        testEntityManager.flush();

        //when
        User found = userRepository.findByUserId(newUser.getUserId());

        //then
        assertEquals(newUser, found);
    }

    
    @Test
    public void findById_whenUserIdIncorrect_thenReturnNull() {
        //given
        User newUser = new User("fuad", "fuadmmnf", "fuadqwer1234");
        testEntityManager.persist(newUser);
        testEntityManager.flush();

        //when
        User found = userRepository.findByUserId("fuad");

        //then
        assertNull(found);

    }
}