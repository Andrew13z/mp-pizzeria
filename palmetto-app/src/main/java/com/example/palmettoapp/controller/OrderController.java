package com.example.palmettoapp.controller;

import com.example.palmettoapp.service.OrderService;
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
  public ResponseEntity<String> markPrepared(@PathVariable Long id) {
    orderService.markPrepared(id);
    return ResponseEntity.ok("Order " + id + " marked prepared.");
  }
}
