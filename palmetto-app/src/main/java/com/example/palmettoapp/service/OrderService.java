package com.example.palmettoapp.service;

import com.example.palmettoapp.kafka.OrderProducer;
import com.example.palmettoentities.dto.OrderDto;
import com.example.palmettoentities.enums.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private static final Logger log = LoggerFactory.getLogger(OrderService.class);

  private final OrderProducer orderProducer;

  @Autowired
  public OrderService(OrderProducer orderProducer) {
    this.orderProducer = orderProducer;
  }


  public void processOrder(OrderDto order) {
    order.setStatus(OrderStatus.PREPARING);
    orderProducer.sendOrderMessage(order);
  }

  public void markPrepared(Long id) {
    var order = new OrderDto();
    order.setId(id);
    order.setStatus(OrderStatus.PREPARED);

    orderProducer.sendOrderMessage(order);

    log.info("Marked order (id = {}) prepared.", id);
  }
}
