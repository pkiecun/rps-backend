package com.cognizant.rps.controller;
import com.cognizant.rps.RpsApplication;
import com.cognizant.rps.models.User;
import com.cognizant.rps.repo.UserRepo;
import com.cognizant.rps.service.UserService;
import com.cognizant.rps.util.Utility;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;


//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = RpsApplication.class)
//@AutoConfigureMockMvc
//@AutoConfigureTestDatabase
//@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = RpsController.class, excludeAutoConfiguration = {ReactiveSecurityAutoConfiguration.class})
@Import(UserService.class)
public class RpsControllerIntegrationTest {

    @MockBean
    private Utility jwt;

    @Autowired
    private WebTestClient mockMvc;

    @MockBean
    private UserRepo ur;

//    @Autowired
//    private UserService us = new UserService(ur);

    //private ObjectMapper om = new ObjectMapper();

    @Test
    void singlePlayerTest()throws Exception{

        mockMvc.get().uri("http://localhost:8000/comp").header(HttpHeaders.ACCEPT, "application/json")
                .exchange().expectStatus().isOk();
    }


    @Test
//    @Transactional
    void handleLoginFailedTest() throws Exception {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("password");
        User user = new User(1001, "username", encodedPassword, 1);
        User somebody = new User(0, "username", "password", 0);

        when((ur).findByUsername(anyString())).thenReturn(user);

        LinkedHashMap<String, String> loginBody = new LinkedHashMap<>();
        loginBody.put("username", "justin");
        loginBody.put("passphrase", "passwor");
        //MvcResult m=
        mockMvc.post().uri("/user/login")
                .header(HttpHeaders.ACCEPT, "application/json")
                .body(BodyInserters.fromValue(loginBody))
                .exchange().expectStatus().isOk().expectBody()
                .jsonPath("$").isEqualTo("Username or password incorrect");

    }

    @Test
    //@Transactional
    void handleAuthenticationTest() throws Exception {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("password");
        User user = new User(1001, "justin", encodedPassword, 1);
        //User somebody = new User(0, "username", "password", 0);

        when((ur).findByUsername(anyString())).thenReturn(user);
        when(jwt.generateToken(any())).thenReturn("Bearer Hello I am a token");
        when(jwt.getUsernameFromToken(anyString())).thenReturn("justin");

        String check = jwt.generateToken(user);
        System.out.println("check = " + check);

        mockMvc.get().uri("/user/authenticate").header("Authorization", check)
                .exchange().expectStatus().isOk().expectBody()
                .jsonPath("$").isEqualTo(false);
    }

   // @Test
//    //@Transactional
//    void handleAuthenticationFailTimeTest() throws Exception {
//        String check = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhIiwiZXhwIjoxNjYwMzM0NzcyLCJpYXQiOjE2NjAzMzExNzJ9.qSYYjwgxvw3v8BOm0bx1_8-Te1EKKTEeq-1BHBpFcWrJ0DvHx8ZYh3F2SBliYkybDmkmtgBjHiRWYW-FCXPF9w";
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/user/authenticate").header("Authorization", check))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").value(false));
//    }

//    @Test
//    @Transactional
//    void handleAuthenticationFailNameTest() throws Exception {
//        User guy = new User(1, "shmurples", "password", 1);
//
//        String check = "Bearer " + jwt.generateToken(guy);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/user/authenticate").header("Authorization", check))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").value(false));
//    }
//
//    @Test
//    @Transactional
//    void handleAuthenticationFailLogTest() throws Exception {
//        LinkedHashMap<String, String> loginBody = new LinkedHashMap<>();
//        loginBody.put("username", "justin");
//        loginBody.put("passphrase", "password");
//        MvcResult m =
//                mockMvc.perform(post("/user/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(om.writeValueAsString(loginBody))
//                )
//                        .andDo(print())
//                        .andReturn();
//        String check = m.getResponse().getContentAsString();
//        mockMvc.perform(MockMvcRequestBuilders.get("/user/logout").header("Authorization", check))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/user/authenticate").header("Authorization", check))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").value(false));
//    }
//
    @Test
    //@Transactional
    void handleFailLogoutTest() throws Exception {
        String check = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhIiwiZXhwIjoxNjYwMzM0NzcyLCJpYXQiOjE2NjAzMzExNzJ9.qSYYjwgxqw3v8BOm0bx1_8-Te1EKKTEeq-1BHBpFcWrJ0DvHx8ZYh3F2SBliYkybDmkmtgBjHiRWYW-FCXPF9w";

        mockMvc.get().uri("/user/logout").
                header("Authorization",check).exchange().expectStatus().
                        isOk();

    }

    @Test
    //@Transactional
    void handleRegisterTest() throws Exception {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("password");
        User user = new User(1001, "username", encodedPassword, 1);
        User somebody = new User(0, "username", "password", 0);
        List<User> response = new ArrayList<>();

        when((ur).findAll()).thenReturn(response);
        when((ur).save(any())).thenReturn(user);

        LinkedHashMap<String, String> loginBody = new LinkedHashMap<>();
        loginBody.put("username", "justin");
        loginBody.put("passphrase", "passwor");
        //MvcResult m=
        mockMvc.post().uri("/user/register")
                .header(HttpHeaders.ACCEPT, "application/json")
                .body(BodyInserters.fromValue(loginBody))
                .exchange().expectStatus().isOk().expectBody()
                .jsonPath("$").isNotEmpty();

    }

    @Test
    //@Transactional
    void handleRegisterFailedTest() throws Exception {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("password");
        User user = new User(1001, "justin", encodedPassword, 1);
        User somebody = new User(0, "username", "password", 0);
        List<User> response = new ArrayList<>();
        response.add(user);

        when((ur).findAll()).thenReturn(response);
        //when((ur).save(any())).thenReturn(user);

        LinkedHashMap<String, String> loginBody = new LinkedHashMap<>();
        loginBody.put("username", "justin");
        loginBody.put("passphrase", "password");
        //MvcResult m=
        mockMvc.post().uri("/user/register")
                .header(HttpHeaders.ACCEPT, "application/json")
                .body(BodyInserters.fromValue(loginBody))
                .exchange().expectStatus().isOk().expectBody()
                .jsonPath("$").isEqualTo("Username invalid.");

    }

}
