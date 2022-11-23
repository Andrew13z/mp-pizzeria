package com.example.clientapp.config;

import com.example.clientapp.property.KafkaConsumerProperties;
import com.example.clientapp.property.KafkaProducerProperties;
import com.example.palmettoentities.dto.OrderDto;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

  @Autowired
  private KafkaProperties kafkaProperties;

  @Autowired
  private KafkaProducerProperties kafkaProducerProperties;

  @Autowired
  private KafkaConsumerProperties kafkaConsumerProperties;

  @Bean
  public NewTopic orderTopic() {
    return TopicBuilder.name(kafkaProducerProperties.getTopicName())
      .partitions(3)
      .replicas(1)
      .build();
  }

  @Bean
  public NewTopic notificationTopic() {
    return TopicBuilder.name(kafkaConsumerProperties.getTopicName())
      .partitions(3)
      .replicas(1)
      .build();
  }

  @Bean
  KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, OrderDto>>
  kafkaListenerContainerFactory() {
    var factory = new ConcurrentKafkaListenerContainerFactory<String, OrderDto>();
    factory.setConsumerFactory(consumerFactory());
    factory.setConcurrency(3);
    return factory;
  }

  @Bean
  public ConsumerFactory<String, OrderDto> consumerFactory() {
    var jsonDeserializer = new JsonDeserializer<OrderDto>();
    jsonDeserializer.addTrustedPackages(OrderDto.class.getPackageName());
    return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(), jsonDeserializer);
  }

  @Bean
  public Map<String, Object> consumerConfigs() {
    var properties = new HashMap<String, Object>();
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers().get(0));
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumer().getGroupId());
    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    return properties;
  }
}
