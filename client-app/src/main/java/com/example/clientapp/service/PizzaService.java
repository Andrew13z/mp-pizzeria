package com.example.clientapp.service;

import com.example.clientapp.entity.Pizza;
import com.example.clientapp.repository.PizzaRepository;
import com.example.palmettoentities.dto.PizzaDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PizzaService {

  private static final Logger log = LoggerFactory.getLogger(PizzaService.class);

  private final PizzaRepository pizzaRepository;
  private final Converter<PizzaDto, Pizza> pizzaDtoConverter;
  private final Converter<Pizza, PizzaDto> pizzaConverter;

  public PizzaService(PizzaRepository pizzaRepository,
                      Converter<PizzaDto, Pizza> pizzaDtoConverter,
                      Converter<Pizza, PizzaDto> pizzaConverter) {
    this.pizzaRepository = pizzaRepository;
    this.pizzaDtoConverter = pizzaDtoConverter;
    this.pizzaConverter = pizzaConverter;
  }

  public PizzaDto create(PizzaDto pizzaDto) {
    var entity = pizzaRepository.save(pizzaDtoConverter.convert(pizzaDto));

    log.info("Saved pizza (id='{}') with name {}.",
      entity.getId(),
      entity.getName());

    return pizzaConverter.convert(entity);
  }

  public List<PizzaDto> getAll() {
    return pizzaRepository.findAll().stream()
      .map(pizzaConverter::convert)
      .collect(toList());
  }
}
