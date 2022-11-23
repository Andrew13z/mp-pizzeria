package com.example.courierapp.controller;

import com.example.courierapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

  private final OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PatchMapping("/{id}")
  public ResponseEntity<String> markDelivered(@PathVariable Long id) {
    orderService.markDeliveredDelivered(id);
    return ResponseEntity.ok("Order " + id + " marked delivered.");
  }
}
