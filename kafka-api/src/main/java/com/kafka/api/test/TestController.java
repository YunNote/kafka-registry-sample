package com.kafka.api.test;

import com.kafka.core.scheme.AUser;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/test")
@RestController
public class TestController {

   private final KafkaTemplate<String, Object> kafkaTemplate;

   @GetMapping("/send")
   public void send() {

      AUser auser = AUser.newBuilder()
         .setName("최윤진")
         .setAge(30)
         .build();

      kafkaTemplate.send("test-topic", auser);
   }

   @GetMapping
   public String test() {

      return "Test";
   }

}
