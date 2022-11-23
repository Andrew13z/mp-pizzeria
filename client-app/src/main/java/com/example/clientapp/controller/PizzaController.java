package com.example.clientapp.controller;

import com.example.clientapp.service.PizzaService;
import com.example.palmettoentities.dto.PizzaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pizzas")
public class PizzaController {

  private final PizzaService pizzaService;

  @Autowired
  public PizzaController(PizzaService pizzaService) {
    this.pizzaService = pizzaService;
  }

  @PostMapping
  public ResponseEntity<PizzaDto> create(@RequestBody PizzaDto pizzaDto) {
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(pizzaService.create(pizzaDto));
  }

  @GetMapping
  public ResponseEntity<List<PizzaDto>> getAll() {
    return ResponseEntity.ok(pizzaService.getAll());
  }
}
