package com.example.palmettoentities.dto;

import java.util.Objects;

public class PizzaDto {

  private Long id;

  private String name;

  private String ingredients;

  private boolean spicy;

  private boolean vegetarian;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIngredients() {
    return ingredients;
  }

  public void setIngredients(String ingredients) {
    this.ingredients = ingredients;
  }

  public boolean isSpicy() {
    return spicy;
  }

  public void setSpicy(boolean spicy) {
    this.spicy = spicy;
  }

  public boolean isVegetarian() {
    return vegetarian;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PizzaDto pizzaDto = (PizzaDto) o;

    return Objects.equals(name, pizzaDto.name);
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }

  public void setVegetarian(boolean vegetarian) {


    this.vegetarian = vegetarian;
  }

  @Override
  public String toString() {
    return "PizzaDto{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", ingredients='" + ingredients + '\'' +
      ", spicy=" + spicy +
      ", vegetarian=" + vegetarian +
      '}';
  }
}
