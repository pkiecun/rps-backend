package com.cognizant.rps.consumer;

import com.cognizant.rps.config.RabbitConfig;
import com.cognizant.rps.models.Message;
import com.cognizant.rps.models.User;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

//import java.lang.reflect.Member;

@Controller
//@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@Component
public class RabbitConsumer {

    @RabbitListener(queues = RabbitConfig.QUEUE)
    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Mono<Message> consumeMessageFromQueue(Message message){
        System.out.println("consumed" + message.toString());
        return Mono.just(message);
    }

}
