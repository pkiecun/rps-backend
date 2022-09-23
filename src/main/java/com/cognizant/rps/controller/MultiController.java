package com.cognizant.rps.controller;

import com.cognizant.rps.models.Match;
import com.cognizant.rps.models.Message;
import com.cognizant.rps.models.Status;
import com.cognizant.rps.repo.GameRepo;
import com.cognizant.rps.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class MultiController {
    @Autowired
    private GameService gs;

    @Autowired
    private GameRepo gr;

    @Autowired
    public MultiController(GameService gs, GameRepo gr){
        this.gs = gs;
        this.gr = gr;
    }

    @PostMapping("/multi/start")
    public Mono<Message> handleStart(@RequestBody Message body){
        return Mono.just(gs.setGoal(body));
                //new Message(body.get("senderName"), body.get("receiverName"), new Match(body.get("message")),Status.MESSAGE);
    }

    @PutMapping("/multi/find")
    public Mono<Message> handleFind(@RequestBody Message message){
        Message partner = gs.findMatch(message);
                return Mono.just(partner);
    }
//        return Mono.just(gs.findMatch(message));

//    @PostMapping("/multi/result")
//    public Mono<Message> handleResult(@RequestBody Message message){
//        return Mono.just(gs.findResult(message));
//    }

}
