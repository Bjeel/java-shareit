package ru.practicum.shareit.request.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoTest {
  @Autowired
  private JacksonTester<ItemRequestDto> json;

  @Test
  void testItemRequestDto() throws Exception {
    LocalDateTime localDateTime = LocalDateTime.parse("2023-08-04T13:26:59");

    ItemRequestDto itemRequestDto = new ItemRequestDto(
      1L,
      "Нужен стол с двумя ножками",
      1L,
      localDateTime,
      new ArrayList<>()
    );

    JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

    assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Нужен стол с двумя ножками");
    assertThat(result).extractingJsonPathNumberValue("$.requester").isEqualTo(1);
    assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2023-08-04T13:26:59");
    assertThat(result).extractingJsonPathArrayValue("$.items").hasSize(0);
  }
}
