package ru.practicum.shareit.booking.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoTest {

  @Autowired
  JacksonTester<BookingDto> json;

  @Test
  public void testBookingDto() throws Exception {
    LocalDateTime startDateTime = LocalDateTime.parse("2023-08-04T13:26:59");
    LocalDateTime endDateTime = LocalDateTime.parse("2023-08-04T13:26:59").plusDays(1);

    BookingDto bookingDto = new BookingDto(
      1L,
      1L,
      1L,
      startDateTime,
      endDateTime
    );

    JsonContent<BookingDto> result = json.write(bookingDto);

    assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    assertThat(result).extractingJsonPathNumberValue("$.booker").isEqualTo(1);
    assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
    assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2023-08-04T13:26:59");
    assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2023-08-05T13:26:59");
  }
}
