package com.kafka.api.test;

import lombok.RequiredArgsConstructor;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Parser;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/test")
@RestController
public class TestController {

   private final KafkaTemplate<String, GenericRecord> kafkaTemplate;

   @GetMapping("/send")
   public void send() {

      Schema.Parser parser = new Parser();
      Schema avroSchema = parser.parse("{\n"
         + "  \"type\": \"record\",\n"
         + "  \"name\": \"AUser\",\n"
         + "  \"namespace\": \"com.kafka.core.scheme\",\n"
         + "  \"fields\": [\n"
         + "    {\n"
         + "      \"name\": \"name\",\n"
         + "      \"type\": [\n"
         + "        \"null\",\n"
         + "        \"string\"\n"
         + "      ],\n"
         + "      \"default\": null\n"
         + "    },\n"
         + "    {\n"
         + "      \"name\": \"age\",\n"
         + "      \"type\": \"int\",\n"
         + "      \"default\": 0\n"
         + "    }\n"
         + "  ]\n"
         + "}");

      GenericRecord avroRecord = new GenericData.Record(avroSchema);
      avroRecord.put("name", "최윤진");
      avroRecord.put("age", 30);

      ProducerRecord<String, GenericRecord> record = new ProducerRecord<>("test-topic", avroRecord);
      kafkaTemplate.send(record);
   }

//   @GetMapping("/send2")
//   public void send2() {
//
//      Schema.Parser parser = new Parser();
//      Schema avroSchema = parser.parse("{\n"
//         + "  \"namespace\": \"com.kafka.core.scheme\",\n"
//         + "  \"type\": \"record\",\n"
//         + "  \"name\": \"AUser\",\n"
//         + "  \"fields\": [\n"
//         + "    {\n"
//         + "      \"name\": \"name5\",\n"
//         + "      \"type\": [\"null\", \"string\"],\n"
//         + "      \"default\" : null "
//         + "    },\n"
//         + "    {\n"
//         + "      \"name\": \"age\",\n"
//         + "      \"type\": \"int\",\n"
//         + "      \"default\" : null "
//         + "    },\n"
//         + "    {\n"
//         + "      \"name\": \"address3\",\n"
//         + "      \"type\": [\"null\", \"string\"],\n"
//         + "      \"default\" : null "
//         + "    },\n"
//         + "    {\n"
//         + "      \"name\": \"name4\",\n"
//         + "      \"type\": [\"null\", \"string\"],\n"
//         + "      \"default\" : null "
//         + "    }\n"
//         + "  ]\n"
//         + "}\n"
//         + "\n");
//
//      GenericRecord avroRecord = new GenericData.Record(avroSchema);
////      avroRecord.put("name4", "최윤진");
//      avroRecord.put("age", 30);
//      avroRecord.put("address3", "서울시");
//
//      ProducerRecord<String, GenericRecord> record = new ProducerRecord<>("test-topic", avroRecord);
//      kafkaTemplate.send(record);
//   }

   @GetMapping
   public String test() {

      return "Test";
   }

}
