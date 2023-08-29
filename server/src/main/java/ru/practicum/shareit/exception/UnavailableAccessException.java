package ru.practicum.shareit.exception;

public class UnavailableAccessException extends RuntimeException {
  public UnavailableAccessException(String message) {
    super(message);
  }
}
