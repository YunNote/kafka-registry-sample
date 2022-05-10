package com.kafka.core.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.core.configuration.serializer.AvroJsonMixin;
import com.kafka.core.configuration.serializer.AvroKafkaJsonDeserializer;
import com.kafka.core.scheme.AUser;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaConfiguration {

   private final KafkaProperties kafkaProperties;

   @Bean
   public KafkaTemplate<String, Object> kafkaTemplate() {

      return new KafkaTemplate<>(producerFactory());
   }

   @Bean
   public ProducerFactory<String, Object> producerFactory() {

      Map<String, Object> configProps = new HashMap<>();
      configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
      configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
      configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroKafkaJsonDeserializer.class);

      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.addMixIn(SpecificRecordBase.class, AvroJsonMixin.class);

      DefaultKafkaProducerFactory<String, Object> factory = new DefaultKafkaProducerFactory<>(configProps);
      factory.setValueSerializer(new JsonSerializer<>(objectMapper));

      return factory;
   }


   @Bean
   public ConcurrentKafkaListenerContainerFactory<String, AUser> kafkaListenerContainerFactory(
      RetryTemplate retryTemplate
   ) {

      Map<String, Object> props = new HashMap<>();
      props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
      props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaProperties.getConsumer().getEnableAutoCommit());
      props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperties.getConsumer().getAutoOffsetReset());
//      props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

      ConsumerFactory<String, AUser> consumerFactory = new DefaultKafkaConsumerFactory<>(
         props,
         new StringDeserializer(),
         new AvroKafkaJsonDeserializer<>(AUser.class)
      );

      ConcurrentKafkaListenerContainerFactory<String, AUser> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
      containerFactory.setConsumerFactory(consumerFactory);
      containerFactory.setRetryTemplate(retryTemplate);
      containerFactory.setRecoveryCallback(context -> {
         log.info("consumer retry -" + context.toString());
         return null;
      });

      return containerFactory;
   }

   @Bean
   public RetryTemplate retryTemplate() {

      RetryTemplate retryTemplate = new RetryTemplate();
      // 재시도시 1초 후에 재 시도하도록 backoff delay 시간을 설정한다.
      FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
      fixedBackOffPolicy.setBackOffPeriod(1000L);
      retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
      // 최대 재시도 횟수 설정
      SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
      retryPolicy.setMaxAttempts(2);
      retryTemplate.setRetryPolicy(retryPolicy);
      return retryTemplate;
   }

}
