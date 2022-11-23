package com.example.clientapp.controller;

import com.example.clientapp.service.OrderService;
import com.example.palmettoentities.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  @PostMapping
  public ResponseEntity<OrderDto> create(@RequestBody OrderDto orderDto) {
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(orderService.create(orderDto));
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(orderService.getById(id));
  }
}
