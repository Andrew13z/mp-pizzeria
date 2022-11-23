package com.example.clientapp.converter;

import com.example.clientapp.entity.Client;
import com.example.palmettoentities.dto.ClientDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ClientConverter implements Converter<Client, ClientDto> {

  @Override
  public ClientDto convert(Client source) {
    if (source == null) {
      throw new IllegalArgumentException();
    }

    var dto = new ClientDto();

    dto.setId(source.getId());
    dto.setName(source.getFullName());
    dto.setPhoneNumber(source.getPhoneNumber());
    dto.setAddress(source.getAddress());

    return dto;
  }
}
