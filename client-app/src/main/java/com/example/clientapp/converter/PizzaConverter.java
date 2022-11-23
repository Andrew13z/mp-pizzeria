package com.example.clientapp.converter;

import com.example.clientapp.entity.Pizza;
import com.example.palmettoentities.dto.PizzaDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PizzaConverter implements Converter<Pizza, PizzaDto> {

  @Override
  public PizzaDto convert(Pizza source) {
    if (source == null) {
      throw new IllegalArgumentException();
    }

    var dto = new PizzaDto();

    dto.setId(source.getId());
    dto.setName(source.getName());
    dto.setIngredients(source.getIngredients());
    dto.setSpicy(source.isSpicy());
    dto.setVegetarian(source.isVegetarian());

    return dto;
  }
}
