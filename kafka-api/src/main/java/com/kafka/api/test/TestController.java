package com.kafka.api.test;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Parser;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
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

      Properties configs = new Properties();
      configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
      configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
      configs.setProperty("value.serializer", KafkaAvroSerializer.class.getName());
      configs.setProperty("schema.registry.url", "http://localhost:8081");

      Schema.Parser parser = new Parser();
      Schema avroSchema = parser.parse("{\n"
         + "  \"namespace\": \"com.kafka.core.scheme\",\n"
         + "  \"type\": \"record\",\n"
         + "  \"name\": \"AUser\",\n"
         + "  \"fields\": [\n"
         + "    {\n"
         + "      \"name\": \"name\",\n"
         + "      \"type\": \"string\",\n"
         + "      \"avro.java.string\": \"String\"\n"
         + "    },\n"
         + "    {\n"
         + "      \"name\": \"age\",\n"
         + "      \"type\": \"int\"\n"
         + "    }\n"
         + "  ]\n"
         + "}\n"
         + "\n");

      GenericRecord avroRecord = new GenericData.Record(avroSchema);
      avroRecord.put("name", "최윤진");
      avroRecord.put("age", 30);

      KafkaProducer<String, GenericRecord> producer = new KafkaProducer<>(configs);
      ProducerRecord<String, GenericRecord> record = new ProducerRecord<>("test-topic", avroRecord);
      producer.send(record);
   }

   @GetMapping("/send2")
   public void send2() {

      Properties configs = new Properties();
      configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
      configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
      configs.setProperty("value.serializer", KafkaAvroSerializer.class.getName());
      configs.setProperty("schema.registry.url", "http://localhost:8081");

      Schema.Parser parser = new Parser();
      Schema avroSchema = parser.parse("{\n"
         + "  \"namespace\": \"com.kafka.core.scheme\",\n"
         + "  \"type\": \"record\",\n"
         + "  \"name\": \"AUser\",\n"
         + "  \"fields\": [\n"
         + "    {\n"
         + "      \"name\": \"name\",\n"
         + "      \"type\": \"string\",\n"
         + "      \"avro.java.string\": \"String\"\n"
         + "    },\n"
         + "    {\n"
         + "      \"name\": \"age\",\n"
         + "      \"type\": \"int\"\n"
         + "    },\n"
         + "    {\n"
         + "      \"name\": \"address\",\n"
         + "      \"type\": \"string\",\n"
         + "      \"avro.java.string\": \"String\"\n"
         + "    }\n"
         + "  ]\n"
         + "}\n"
         + "\n");

      GenericRecord avroRecord = new GenericData.Record(avroSchema);
      avroRecord.put("name", "최윤진");
      avroRecord.put("age", 30);
      avroRecord.put("address", "서울시");

      KafkaProducer<String, GenericRecord> producer = new KafkaProducer<>(configs);
      ProducerRecord<String, GenericRecord> record = new ProducerRecord<>("test-topic", avroRecord);
      producer.send(record);
   }

   @GetMapping
   public String test() {

      return "Test";
   }

}
