package ru.practicum.shareit.item.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemFullDtoTest {
  @Autowired
  private JacksonTester<ItemFullDto> json;

  @Test
  void testItemFullDto() throws Exception {
    ItemFullDto itemFullDto = new ItemFullDto(
      1L,
      "Стол",
      "Нужен стол с двумя ножками",
      true,
      1L,
      null,
      null,
      new ArrayList<>(),
      new ArrayList<>()
    );

    JsonContent<ItemFullDto> result = json.write(itemFullDto);

    assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Стол");
    assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Нужен стол с двумя ножками");
    assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
    assertThat(result).extractingJsonPathNumberValue("$.owner").isEqualTo(1);
    assertThat(result).extractingJsonPathValue("$.lastBooking").isNull();
    assertThat(result).extractingJsonPathValue("$.nextBooking").isNull();
    assertThat(result).extractingJsonPathArrayValue("$.comments").hasSize(0);
    assertThat(result).extractingJsonPathArrayValue("$.request").hasSize(0);

  }
}
