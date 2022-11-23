package com.example.clientapp.kafka;

import com.example.clientapp.property.KafkaProducerProperties;
import com.example.palmettoentities.dto.OrderDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderProducer {

  private static final Logger log = LoggerFactory.getLogger(OrderProducer.class);

  private final KafkaTemplate<String, OrderDto> template;
  private final KafkaProducerProperties kafkaProducerProperties;

  @Autowired
  public OrderProducer(KafkaTemplate<String, OrderDto> template, KafkaProducerProperties kafkaProducerProperties) {
    this.template = template;
    this.kafkaProducerProperties = kafkaProducerProperties;
  }

  public void sendOrderMessage(OrderDto orderDto) {
    var topicName = kafkaProducerProperties.getTopicName();
    var id = orderDto.getId().toString();

    template.send(topicName, id, orderDto);

    log.info("Sent order message (id='{}') to topic '{}'.", orderDto.getId(), topicName);
  }
}
