package com.example.clientapp.converter;

import com.example.clientapp.entity.Client;
import com.example.clientapp.entity.Order;
import com.example.palmettoentities.dto.ClientDto;
import com.example.palmettoentities.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoConverter implements Converter<OrderDto, Order> {

  private final Converter<ClientDto, Client> clientDtoConverter;

  @Autowired
  public OrderDtoConverter(Converter<ClientDto, Client> clientDtoConverter) {
    this.clientDtoConverter = clientDtoConverter;
  }

  @Override
  public Order convert(OrderDto source) {
    if (source == null) {
      throw new IllegalArgumentException();
    }

    var entity = new Order();

    entity.setId(source.getId());
    entity.setStatus(source.getStatus());
    entity.setClient(clientDtoConverter.convert(source.getClient()));

    return entity;
  }
}
