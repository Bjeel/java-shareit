package ru.practicum.shareit.error_handler;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Status;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

  @Test
  void getError() {
    String message = "Error";
    ErrorResponse errorResponse = new ErrorResponse(message);

    assertThat(errorResponse, notNullValue());
    assertThat(errorResponse.getError(), is(message));
  }
}
