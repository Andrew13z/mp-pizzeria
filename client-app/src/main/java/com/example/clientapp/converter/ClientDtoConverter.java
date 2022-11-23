package com.example.clientapp.converter;

import com.example.clientapp.entity.Client;
import com.example.palmettoentities.dto.ClientDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ClientDtoConverter implements Converter<ClientDto, Client> {

  @Override
  public Client convert(ClientDto source) {
    if (source == null) {
      throw new IllegalArgumentException();
    }

    var entity = new Client();

    entity.setId(source.getId());
    entity.setFullName(source.getName());
    entity.setPhoneNumber(source.getPhoneNumber());
    entity.setAddress(source.getAddress());

    return entity;
  }
}
