package com.example.palmettoapp.kafka;

import com.example.palmettoapp.service.OrderService;
import com.example.palmettoentities.dto.OrderDto;
import com.example.palmettoentities.dto.PizzaDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toList;

@Component
public class OrderListener {

  private static final Logger log = LoggerFactory.getLogger(OrderListener.class);

  private final OrderService orderService;

  @Autowired
  public OrderListener(OrderService orderService) {
    this.orderService = orderService;
  }

  @KafkaListener(topics = "${palmetto-app.consumer.topic-name}")
  public void consumeOrderMessage(ConsumerRecord<String, OrderDto> consumerRecord) {
    var order = consumerRecord.value();
    log.info("Received order message (id='{}') in partition {} for {} pizza(s): {}",
      order.getId(),
      consumerRecord.partition(),
      order.getPizzas().size(),
      order.getPizzas().stream()
        .map(PizzaDto::getName)
        .collect(toList()));

    orderService.processOrder(order);
  }
}
