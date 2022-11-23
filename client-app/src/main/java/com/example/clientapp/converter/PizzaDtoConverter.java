package com.example.clientapp.converter;

import com.example.clientapp.entity.Pizza;
import com.example.palmettoentities.dto.PizzaDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PizzaDtoConverter implements Converter<PizzaDto, Pizza> {

  @Override
  public Pizza convert(PizzaDto source) {
    if (source == null) {
      throw new IllegalArgumentException();
    }

    var entity = new Pizza();

    entity.setId(source.getId());
    entity.setName(source.getName());
    entity.setIngredients(source.getIngredients());
    entity.setSpicy(source.isSpicy());
    entity.setVegetarian(source.isVegetarian());

    return entity;
  }
}
