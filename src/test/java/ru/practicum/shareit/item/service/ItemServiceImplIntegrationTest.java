package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.domain.Booking;

import ru.practicum.shareit.booking.repository.BookingRepository;

import ru.practicum.shareit.comments.domain.CommentDto;

import ru.practicum.shareit.item.domain.Item;

import ru.practicum.shareit.item.domain.ItemFullDto;

import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.domain.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

@SpringBootTest
@Rollback(false)
class ItemServiceImplIntegrationTest {
  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ItemServiceImpl itemService;

  @Autowired
  private BookingRepository bookingRepository;

  @BeforeEach
  public void prepare() {
    User user = new User();

    user.setId(1L);
    user.setEmail("mail@mail.ru");
    user.setName("Jon Doe");

    User savedUser = userRepository.save(user);

    Item item = new Item();

    item.setId(1L);
    item.setOwner(savedUser.getId());
    item.setAvailable(true);
    item.setName("Стол");
    item.setDescription("Стол с двумя ножками");

    itemRepository.save(item);

    bookingRepository.save(
      Booking.builder()
        .id(1L)
        .status(Status.APPROVED)
        .start(LocalDateTime.parse("2023-04-10T20:20:12"))
        .end(LocalDateTime.parse("2023-05-10T20:20:12"))
        .booker(savedUser)
        .item(item)
        .build()
    );

    bookingRepository.save(
      Booking.builder()
        .id(2L)
        .status(Status.APPROVED)
        .start(LocalDateTime.parse("2024-04-10T20:20:12"))
        .end(LocalDateTime.parse("2024-05-10T20:20:12"))
        .booker(savedUser)
        .item(item)
        .build()
    );

    itemService.addComment(
      CommentDto.builder()
        .itemId(item.getId())
        .authorId(user.getId())
        .text("Прекрасный комментарий")
        .build()
    );
  }

  @Test
  void findOne() {
    ItemFullDto item = itemService.findOne(1L, 1L);

    System.out.println(item);

    assertThat(item, notNullValue());
    assertThat(item.getId(), is(1L));
    assertThat(item.getName(), is("Стол"));
    assertThat(item.getDescription(), is("Стол с двумя ножками"));
    assertThat(item.getAvailable(), is(true));
    assertThat(item.getOwner(), is(1L));
    assertThat(item.getLastBooking(), notNullValue());
    assertThat(item.getNextBooking(), notNullValue());
    assertThat(item.getComments(), notNullValue());
  }
}
