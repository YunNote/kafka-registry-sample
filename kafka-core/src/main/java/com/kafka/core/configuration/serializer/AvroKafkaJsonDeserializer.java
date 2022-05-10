package com.kafka.core.configuration.serializer;

import org.springframework.kafka.support.serializer.JsonDeserializer;

public class AvroKafkaJsonDeserializer<T> extends JsonDeserializer<T> {

   public AvroKafkaJsonDeserializer(Class<? super T> targetType) {

      super(targetType);
      this.setRemoveTypeHeaders(false);
      this.addTrustedPackages("com.kafka.core.scheme");
      this.setUseTypeMapperForKey(true);
   }

}
