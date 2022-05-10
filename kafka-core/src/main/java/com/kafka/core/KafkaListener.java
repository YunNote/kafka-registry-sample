package com.kafka.core;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaListener {

   @org.springframework.kafka.annotation.KafkaListener(
      topics = "test-topic",
      id = "test-topic-application",
      groupId = "abcde",
      containerFactory = "kafkaListenerContainerFactory"
   )
   public void concume(@Headers MessageHeaders headers, @Payload GenericRecord message) throws IOException {

      System.out.println(headers);
      System.out.println(message);
   }
}
