package com.example.clientapp.service;

import com.example.clientapp.entity.Order;
import com.example.clientapp.entity.Pizza;
import com.example.clientapp.exception.EntityNotFoundException;
import com.example.clientapp.kafka.OrderProducer;
import com.example.clientapp.repository.ClientRepository;
import com.example.clientapp.repository.OrderRepository;
import com.example.clientapp.repository.PizzaRepository;
import com.example.palmettoentities.dto.OrderDto;
import com.example.palmettoentities.dto.PizzaDto;
import com.example.palmettoentities.enums.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static java.util.stream.Collectors.toList;

@Service
public class OrderService {

  private static final Logger log = LoggerFactory.getLogger(OrderService.class);

  private final OrderRepository orderRepository;
  private final ClientRepository clientRepository;
  private final PizzaRepository pizzaRepository;
  private final OrderProducer orderProducer;

  private final Converter<OrderDto, Order> orderDtoConverter;
  private final Converter<Order, OrderDto> orderConverter;

  @Autowired
  public OrderService(OrderRepository orderRepository,
                      ClientRepository clientRepository,
                      PizzaRepository pizzaRepository,
                      OrderProducer orderProducer,
                      Converter<OrderDto, Order> orderDtoConverter,
                      Converter<Order, OrderDto> orderConverter) {
    this.orderRepository = orderRepository;
    this.clientRepository = clientRepository;
    this.pizzaRepository = pizzaRepository;
    this.orderProducer = orderProducer;
    this.orderDtoConverter = orderDtoConverter;
    this.orderConverter = orderConverter;
  }

  public OrderDto create(OrderDto orderDto) {
    var savedOrder = saveOrder(orderDto);
    sendOrderMessage(savedOrder);
    return savedOrder;
  }

  private void sendOrderMessage(OrderDto orderDto) {
    orderProducer.sendOrderMessage(orderDto);
  }

  private OrderDto saveOrder(OrderDto orderDto) {
    var orderEntity = orderDtoConverter.convert(orderDto);

    var clientId = orderDto.getClient().getId();
    var clientEntity = clientRepository.findById(clientId)
      .orElseThrow(() -> new EntityNotFoundException("Client not found by id: " + clientId));
    orderEntity.setClient(clientEntity);

    var pizzaEntities = orderDto.getPizzas().stream()
      .map(PizzaDto::getId)
      .map(this::findPizzaById)
      .collect(toList());
    orderEntity.getPizzas().addAll(pizzaEntities);

    orderEntity.setStatus(OrderStatus.CREATED);

    var savedOrderEntity = orderRepository.save(orderEntity);

    log.info("Saved order (id='{}') from {} for {} pizza(s).",
      savedOrderEntity.getId(),
      savedOrderEntity.getClient().getFullName(),
      savedOrderEntity.getPizzas().size());

    return orderConverter.convert(savedOrderEntity);
  }

  public OrderDto getById(Long id) {
    return orderRepository.findById(id)
      .map(orderConverter::convert)
      .orElseThrow(() -> new EntityNotFoundException("Order not found by id: " + id));
  }

  private Pizza findPizzaById(Long pizzaId) {
    return pizzaRepository.findById(pizzaId)
      .orElseThrow(() -> new EntityNotFoundException("Pizza not found by id: " + pizzaId));
  }

  @Transactional
  public void updateOrderStatus(OrderDto order) {
    log.info("Updating order (id='{}') status to {}.",
      order.getId(),
      order.getStatus());

    orderRepository.findById(order.getId())
      .ifPresentOrElse(
        orderEntity -> orderEntity.setStatus(order.getStatus()),
        () -> new EntityNotFoundException("Order not found by id: " + order.getId())
      );
  }
}
