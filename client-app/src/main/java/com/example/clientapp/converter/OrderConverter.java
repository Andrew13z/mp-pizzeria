package com.example.clientapp.converter;

import com.example.clientapp.entity.Client;
import com.example.clientapp.entity.Order;
import com.example.clientapp.entity.Pizza;
import com.example.palmettoentities.dto.ClientDto;
import com.example.palmettoentities.dto.OrderDto;
import com.example.palmettoentities.dto.PizzaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toList;

@Component
public class OrderConverter implements Converter<Order, OrderDto> {

  private final Converter<Client, ClientDto> clientConverter;
  private final Converter<Pizza, PizzaDto> pizzaConverter;

  @Autowired
  public OrderConverter(Converter<Client, ClientDto> clientConverter, Converter<Pizza, PizzaDto> pizzaConverter) {
    this.clientConverter = clientConverter;
    this.pizzaConverter = pizzaConverter;
  }

  @Override
  public OrderDto convert(Order source) {
    if (source == null) {
      throw new IllegalArgumentException();
    }

    var dto = new OrderDto();

    dto.setId(source.getId());
    dto.setStatus(source.getStatus());
    dto.setClient(clientConverter.convert(source.getClient()));

    var pizzaDtos = source.getPizzas().stream()
      .map(pizzaConverter::convert)
      .collect(toList());
    dto.getPizzas().addAll(pizzaDtos);

    return dto;
  }
}
