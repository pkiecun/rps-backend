package com.cognizant.rps.controller;

import com.cognizant.rps.models.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class RpsController {
    //http://rps-frontend-mhcrew.s3-website.us-east-2.amazonaws.com
//    @Autowired
//    private SimpMessagingTemplate simpMessagingTemplate;

//    @MessageMapping("/message")
//    @SendTo("/chatroom/public")
//    public Message receiveMessage( @Payload Message message){
//        System.out.println("received");
//        return message;
//    }
//
//    @MessageMapping("/private-message")
//    public Message recMessage(@Payload Message message){
//        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
//        System.out.println(message.toString());
//        return message;
//    }
    @GetMapping("/comp")
    public Mono<Integer> singlePlayer(){
        Random choice = new Random();
        int result = choice.nextInt(3);
        return Mono.just((result + 1));
    }
}
