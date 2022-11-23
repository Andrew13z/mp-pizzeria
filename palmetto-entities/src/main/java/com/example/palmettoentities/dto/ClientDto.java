package com.example.palmettoentities.dto;

import java.util.Objects;

public class ClientDto {

  private Long id;

  private String name;

  private String phoneNumber;

  private String address;

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

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getAddress() {
    return address;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ClientDto clientDto = (ClientDto) o;

    return Objects.equals(phoneNumber, clientDto.phoneNumber);
  }

  @Override
  public int hashCode() {
    return phoneNumber != null ? phoneNumber.hashCode() : 0;
  }

  public void setAddress(String address) {


    this.address = address;
  }

  @Override
  public String toString() {
    return "ClientDto{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", phoneNumber='" + phoneNumber + '\'' +
      ", address='" + address + '\'' +
      '}';
  }
}
