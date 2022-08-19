package com.cognizant.rps.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/play")
public class RpsController {

    @GetMapping("/comp")
    public ResponseEntity<Integer> singlePlayer(){
        Random choice = new Random();
        int result = choice.nextInt(3);
        return ResponseEntity.ok((result + 1));
    }
}
