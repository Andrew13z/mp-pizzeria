package com.example.courierapp.kafka;

import com.example.courierapp.service.OrderService;
import com.example.palmettoentities.dto.OrderDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {

  private static final Logger log = LoggerFactory.getLogger(OrderListener.class);

  private final OrderService orderService;

  @Autowired
  public OrderListener(OrderService orderService) {
    this.orderService = orderService;
  }

  @KafkaListener(topics = "${courier-app.consumer.topic-name}")
  public void consumeOrderMessage(ConsumerRecord<String, OrderDto> consumerRecord) {
    var order = consumerRecord.value();
    log.info("Received order message (id='{}') in partition {} with status {}.",
      order.getId(),
      consumerRecord.partition(),
      order.getStatus());

    orderService.updateOrderStatusIfNeeded(order);
  }
}
