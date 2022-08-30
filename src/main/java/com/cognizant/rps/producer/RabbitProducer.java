package com.cognizant.rps.producer;
import com.cognizant.rps.config.RabbitConfig;
import com.cognizant.rps.models.Message;
import com.cognizant.rps.models.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
//@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class RabbitProducer {

    @Autowired
    RabbitTemplate template;

    @MessageMapping("/private-message")
    public Mono<Message> createPerson(@RequestBody Message message){
        System.out.println("received" + message.toString());
        template.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTINGKEY, message);
        return Mono.just(message);
    }

}
