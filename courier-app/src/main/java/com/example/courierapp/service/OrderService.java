package com.example.courierapp.service;

import com.example.courierapp.kafka.OrderProducer;
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

  public void updateOrderStatusIfNeeded(OrderDto orderDto) {
    if (OrderStatus.PREPARED == orderDto.getStatus()) {
      orderDto.setStatus(OrderStatus.DELIVERING);

      orderProducer.sendOrderMessage(orderDto);

      log.info("Marked order (id='{}') as delivering.", orderDto.getId());
    }
  }

  public void markDeliveredDelivered(Long id) {
    var order = new OrderDto();
    order.setId(id);
    order.setStatus(OrderStatus.DELIVERED);

    orderProducer.sendOrderMessage(order);

    log.info("Marked order (id='{}') prepared.", id);
  }
}
