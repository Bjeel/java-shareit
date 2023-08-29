package ru.practicum.shareit.item.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoTest {
  @Autowired
  private JacksonTester<ItemDto> json;

  @Test
  void testItemDto() throws Exception {
    ItemDto itemDto = new ItemDto(
      1L,
      "Стол",
      "Нужен стол с двумя ножками",
      true,
      1L,
      1L
    );

    JsonContent<ItemDto> result = json.write(itemDto);

    assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Стол");
    assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Нужен стол с двумя ножками");
    assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
    assertThat(result).extractingJsonPathNumberValue("$.owner").isEqualTo(1);
    assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
  }
}
