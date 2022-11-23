package com.example.clientapp.dto;

public class ErrorDto {

  private String reasonPhrase;
  private String message;

  public ErrorDto(String reasonPhrase, String message) {
    this.reasonPhrase = reasonPhrase;
    this.message = message;
  }

  public String getReasonPhrase() {
    return reasonPhrase;
  }

  public void setReasonPhrase(String reasonPhrase) {
    this.reasonPhrase = reasonPhrase;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
