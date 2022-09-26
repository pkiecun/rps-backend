package com.cognizant.rps.controller;

import com.cognizant.rps.models.Message;
import com.cognizant.rps.models.User;
import com.cognizant.rps.repo.UserRepo;
import com.cognizant.rps.service.UserService;
import com.cognizant.rps.util.MailingUtil;
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
@CrossOrigin(origins = "http://rps-frontend-mhcrew.s3-website.us-east-2.amazonaws.com/", allowCredentials = "true")
public class RpsController {

    @Autowired
    private Utility jwtUtility;

    @Autowired
    private MailingUtil m;

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
        long startTime = System.currentTimeMillis();
        User logger = new User(0,body.get("username"),body.get("passphrase"), 0);
        String verify = us.loggedIn(logger);
        long endTime = System.currentTimeMillis();
        if(endTime - startTime > 7000) {
            triggerMail("Error in handleLoginUser. Delay length: "+(endTime - startTime)+" milliseconds.");
        }
        if (!verify.equals("Wrong Password")) {
            return Mono.just(verify);//, HttpStatus.ACCEPTED);
        } else {
            return Mono.just("Username or password incorrect");//, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/user/authenticate")
    public Mono<Boolean> handleAuthenticate(@RequestHeader LinkedHashMap<String, String> ticket) throws Exception{
        long startTime = System.currentTimeMillis();
        long endTime;
        String check = ticket.get("token");
        String token = "";
        String username = "";
        User user = null;
        Boolean result = false;
        if(null != check && check.startsWith("Bearer ")) {
            token = check.substring(7);
            try {
                username = jwtUtility.getUsernameFromToken(token);
                endTime = System.currentTimeMillis();
                if(endTime - startTime > 7000) {
                    triggerMail("Error in handleAuthenticate: getUsernameFromToken(). Delay length: "+(endTime - startTime)+" milliseconds.");
                }
            }catch(Exception e){
                return Mono.just(result);//, HttpStatus.UNAUTHORIZED);
            }
        }

        if(null != username) {
            user = ur.findByUsername(username);
            endTime = System.currentTimeMillis();
            if(endTime - startTime > 7000) {
                triggerMail("Error in handleAuthenticate: findByUsername(). Delay length: "+(endTime - startTime)+" milliseconds.");
            }
        }
        if(null != user && user.getLogin() == 1) {
            result = jwtUtility.validateToken(token, user);
            endTime = System.currentTimeMillis();
            if(endTime - startTime > 7000) {
                triggerMail("Error in handleAuthenticate: validateToken(). Delay length: "+(endTime - startTime)+" milliseconds.");
            }
        }
        return Mono.just(result);
    }

    @GetMapping("/user/logout")
    public void handleLogout(@RequestHeader LinkedHashMap<String, String> ticket) {
        long startTime = System.currentTimeMillis();
        long endTime;
        String check = ticket.get("token");
        String token = "";
        String username = "";
        User user = null;
        if(null != check && check.startsWith("Bearer ")) {
            token = check.substring(7);
            try {
                username = jwtUtility.getUsernameFromToken(token);
                endTime = System.currentTimeMillis();
                if(endTime - startTime > 7000) {
                    triggerMail("Error in handleLogout: getUsernameFromToken(). Delay length: "+(endTime - startTime)+" milliseconds.");
                }
            }catch(Exception e){
                //return new ResponseEntity <>( HttpStatus.NOT_FOUND);
            }
        }

        if(null != username) {
            user = ur.findByUsername(username);
            endTime = System.currentTimeMillis();
            if(endTime - startTime > 7000) {
                triggerMail("Error in handleLogout: findByUsername(). Delay length: "+(endTime - startTime)+" milliseconds.");
            }
        }
        if(null != user) {
            user.setLogin(0);
            ur.save(user);
            endTime = System.currentTimeMillis();
            if(endTime - startTime > 7000) {
                triggerMail("Error in handleLogout: save(). Delay length: "+(endTime - startTime)+" milliseconds.");
            }
        }else {
            //return new ResponseEntity <>( HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/user/register")
    public Mono<String> handleRegisterUser(@RequestBody LinkedHashMap<String, String> body){
        long startTime = System.currentTimeMillis();
        User logger = new User(0, body.get("username"),body.get("passphrase"), 0);
        String verify = us.register(logger);

        long endTime = System.currentTimeMillis();
        if(endTime - startTime > 7000) {
            triggerMail("Error in handleRegisterUser. Delay length: "+(endTime - startTime)+" milliseconds.");
        }
        if (!verify.equals("EXCEPTION ON LINE 61")) {
            return Mono.just(verify);//, HttpStatus.ACCEPTED);
        } else {
            return Mono.just("Username invalid.");//, HttpStatus.BAD_REQUEST);
        }

    }

    public void triggerMail(String body){
        m.sendMail("testUser15379@gmail.com",
                "Delay in RpsController response!",
                body);
    }
}
