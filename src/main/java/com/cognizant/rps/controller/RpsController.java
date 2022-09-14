package com.cognizant.rps.controller;

import com.cognizant.rps.models.Message;
import com.cognizant.rps.models.User;
import com.cognizant.rps.repo.UserRepo;
import com.cognizant.rps.service.UserService;
import com.cognizant.rps.util.Utility;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class RpsController {

    @Autowired
    private Utility jwtUtility;

    @Autowired
    private UserRepo ur;

    @Autowired
    private UserService us;

    @Autowired
    public RpsController(UserService us, UserRepo ur) {
        this.us = us;
        this.ur = ur;
    }

    @GetMapping("/comp")
    public Mono<Integer> singlePlayer(){
        Random choice = new Random();
        int result = choice.nextInt(3);
        return Mono.just((result + 1));
    }

    @PostMapping("/user/login")
    public Mono<String> handleLoginUser(@RequestBody LinkedHashMap<String, String> body){
        User logger = new User(0,body.get("username"),body.get("passphrase"), 0);
        String verify = us.loggedIn(logger);
        if(!verify.equals("Wrong Password")){
            return Mono.just(verify);//, HttpStatus.ACCEPTED);
        }else{
            return Mono.just("Username or password incorrect");//, HttpStatus.NOT_ACCEPTABLE);
        }

    }

    @GetMapping("/user/authenticate")
    public Mono<Boolean> handleAuthenticate(HttpServletRequest ticket) throws Exception{
        String check = ticket.getHeader("Authorization");
        String token = "";
        String username = "";
        User user = null;
        Boolean result = false;
        if(null != check && check.startsWith("Bearer ")) {
            token = check.substring(7);
            try {

                username = jwtUtility.getUsernameFromToken(token);
            }catch(Exception e){
                return Mono.just(result);//, HttpStatus.UNAUTHORIZED);
            }
        }

        if(null != username) {
            user = ur.findByUsername(username);

        }
        if(null != user && user.getLogin() == 1) {
            System.out.println("this should not be null: " + result);
            result = jwtUtility.validateToken(token, user);
            System.out.println("this should not be null: " + result);
        }
        return Mono.just(result);
//        if(result){
//            return Mono.just(result);//, HttpStatus.ACCEPTED);
//        }else {
//            return Mono.just(result);//, HttpStatus.UNAUTHORIZED);
//        }
    }

    @GetMapping("/user/logout")
    public void handleLogout(HttpServletRequest ticket) {
        String check = ticket.getHeader("Authorization");
        String token = "";
        String username = "";
        User user = null;
        if(null != check && check.startsWith("Bearer ")) {
            token = check.substring(7);
            try {

                username = jwtUtility.getUsernameFromToken(token);
            }catch(Exception e){
                //return new ResponseEntity <>( HttpStatus.NOT_FOUND);
            }
        }

        if(null != username) {
            user = ur.findByUsername(username);

        }
        if(null != user) {
            user.setLogin(0);
            ur.save(user);
            //new ResponseEntity<>( HttpStatus.NO_CONTENT);
        }else {
            //return new ResponseEntity <>( HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/user/register")
    public Mono<String> handleRegisterUser(@RequestBody LinkedHashMap<String, String> body){
        User logger = new User(0, body.get("username"),body.get("passphrase"), 0);
        String verify = us.register(logger);
        if(!verify.equals("EXCEPTION ON LINE 61")){
            return Mono.just(verify);//, HttpStatus.ACCEPTED);
        } else {
            return Mono.just("Username invalid.");//, HttpStatus.BAD_REQUEST);
        }
    }

}
