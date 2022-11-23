package com.example.clientapp.service;

import com.example.clientapp.entity.Client;
import com.example.clientapp.repository.ClientRepository;
import com.example.palmettoentities.dto.ClientDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

  private static final Logger log = LoggerFactory.getLogger(ClientService.class);

  private final ClientRepository clientRepository;
  private final Converter<ClientDto, Client> clientDtoConverter;
  private final Converter<Client, ClientDto> clientConverter;

  @Autowired
  public ClientService(ClientRepository clientRepository, Converter<ClientDto, Client> clientDtoConverter, Converter<Client, ClientDto> clientConverter) {
    this.clientRepository = clientRepository;
    this.clientDtoConverter = clientDtoConverter;
    this.clientConverter = clientConverter;
  }

  public ClientDto create(ClientDto clientDto) {
    var entity = clientRepository.save(clientDtoConverter.convert(clientDto));

    log.info("Saved client (id='{}') with name {}.",
      entity.getId(),
      entity.getFullName());

    return clientConverter.convert(entity);
  }
}
