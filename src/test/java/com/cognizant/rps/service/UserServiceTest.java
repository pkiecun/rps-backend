package com.cognizant.rps.service;

import com.cognizant.rps.models.User;
import com.cognizant.rps.repo.UserRepo;
import com.cognizant.rps.service.UserService;
import com.cognizant.rps.util.Utility;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.anyString;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
class UserServiceTest {
    @Mock
    static UserRepo ur;

    @Mock
    static Utility jwt;

    @Autowired
    static UserService us;

    @Test
    void findByCredentials(){
        us = new UserService(ur, jwt);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("password");
        User user = new User(1001, "username", encodedPassword, 1);
        User somebody = new User(0, "username", "password", 0);

        when((ur).findByUsername(anyString())).thenReturn(user);

        String testUser = us.loggedIn(somebody);

        String userToken = "Bearer " + jwt.generateToken(user);
        assertEquals("pass", testUser, userToken);

    }



}

