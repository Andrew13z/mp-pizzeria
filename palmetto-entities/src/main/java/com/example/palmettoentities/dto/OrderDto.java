package com.example.palmettoentities.dto;


import com.example.palmettoentities.enums.OrderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderDto {

  private Long id;

  private OrderStatus status;

  private ClientDto client;

  private List<PizzaDto> pizzas = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public ClientDto getClient() {
    return client;
  }

  public void setClient(ClientDto client) {
    this.client = client;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    OrderDto orderDto = (OrderDto) o;

    if (!Objects.equals(id, orderDto.id)) return false;
    if (status != orderDto.status) return false;
    if (!Objects.equals(client, orderDto.client)) return false;
    return Objects.equals(pizzas, orderDto.pizzas);
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (client != null ? client.hashCode() : 0);
    result = 31 * result + (pizzas != null ? pizzas.hashCode() : 0);
    return result;
  }

  public List<PizzaDto> getPizzas() {


    return pizzas;
  }

  @Override
  public String toString() {
    return "OrderDto{" +
      "id=" + id +
      ", status=" + status +
      ", client=" + client +
      ", pizzas=" + pizzas +
      '}';
  }
}
