package ru.practicum.shareit.comments.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoTest {
  @Autowired
  JacksonTester<CommentDto> json;

  @Test
  public void testCommentDto() throws Exception {
    CommentDto commentDto = new CommentDto(
      1L,
      1L,
      "Хороший стол с одной ножкой",
      1L
    );

    JsonContent<CommentDto> result = json.write(commentDto);

    assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
    assertThat(result).extractingJsonPathNumberValue("$.authorId").isEqualTo(1);
    assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Хороший стол с одной ножкой");
  }
}
