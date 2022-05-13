package com.kafka.core;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
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
   public void consumer(@Headers MessageHeaders headers, @Payload GenericRecord payload) throws IOException {

      System.out.println(headers);
      System.out.println(payload);
      Schema schema = payload.getSchema();

//      User user = convertToClass(payload, User.class);
//      System.out.println(user);
   }

//   public <T> T convertToClass(GenericRecord payload, Class<T> convert) throws JsonProcessingException {
//
//      Map<String, Object> values = new ConcurrentHashMap<>();
//      payload.getSchema().getFields().forEach(field -> {
//         values.put(field.name(), payload.get(field.name()));
//      });
//
//      ObjectMapper om = new ObjectMapper();
//      return om.readValue(payload.toString(), convert);
//   }
}
