package com.kafka.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.kafka.api", "com.kafka.core"})
public class KafkaApiServerApplication {

   public static void main(String[] args) {

      SpringApplication.run(KafkaApiServerApplication.class);
   }
}
