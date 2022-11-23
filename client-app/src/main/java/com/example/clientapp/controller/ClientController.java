package com.example.clientapp.controller;

import com.example.clientapp.service.ClientService;
import com.example.palmettoentities.dto.ClientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clients")
public class ClientController {

  private final ClientService clientService;

  @Autowired
  public ClientController(ClientService clientService) {
    this.clientService = clientService;
  }

  @PostMapping
  public ResponseEntity<ClientDto> create(@RequestBody ClientDto clientDto) {
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(clientService.create(clientDto));
  }
}
