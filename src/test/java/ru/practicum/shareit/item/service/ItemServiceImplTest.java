package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.item.domain.ItemDto;
import ru.practicum.shareit.item.domain.ItemFullDto;
import ru.practicum.shareit.item.domain.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.domain.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
  @Mock
  private ItemRepository itemRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private BookingRepository bookingRepository;

  @InjectMocks
  private ItemServiceImpl itemService;

  User user;
  Item item;
  ItemDto itemDto;

  @BeforeEach
  public void prepare() {
    user = User.builder()
      .id(1L)
      .email("mail@mail.ru")
      .name("John Doe")
      .build();

    itemDto = ItemDto.builder()
      .owner(1L)
      .id(1L)
      .description("Стол с кривой ножкой")
      .name("Стол")
      .build();

    item = ItemMapper.toItemFromDto(itemDto);
  }


  @Test
  void create() {
    when(userRepository.findById(anyLong()))
      .thenReturn(Optional.ofNullable(user));
    when(itemRepository.save(item))
      .thenReturn(item);

    ItemDto savedItem = itemService.create(itemDto);

    assertThat(savedItem.getId(), notNullValue());
    assertThat(savedItem.getId(), is(itemDto.getId()));
    assertThat(savedItem.getOwner(), is(itemDto.getOwner()));
    assertThat(savedItem.getName(), is(itemDto.getName()));
    assertThat(savedItem.getDescription(), is(itemDto.getDescription()));
    assertThat(savedItem.getAvailable(), is(itemDto.getAvailable()));

    verify(userRepository).findById(anyLong());
    verify(itemRepository).save(item);
  }

  @Test
  void findAll() {
    when(itemRepository.findAllByOwner(user.getId()))
      .thenReturn(List.of(item));
    when(bookingRepository.findAllByItem(any()))
      .thenReturn(List.of());

    List<ItemFullDto> savedItems = itemService.findAll(user.getId());

    assertThat(savedItems, notNullValue());
    assertThat(savedItems, hasSize(1));
    assertThat(savedItems.get(0).getId(), is(itemDto.getId()));
    assertThat(savedItems.get(0).getOwner(), is(itemDto.getOwner()));
    assertThat(savedItems.get(0).getName(), is(itemDto.getName()));
    assertThat(savedItems.get(0).getDescription(), is(itemDto.getDescription()));
    assertThat(savedItems.get(0).getAvailable(), is(itemDto.getAvailable()));

    verify(itemRepository).findAllByOwner(user.getId());
  }
  
  @Test
  void update() {
    itemDto.setAvailable(false);


    when(userRepository.findById(anyLong()))
      .thenReturn(Optional.ofNullable(user));

    when(itemRepository.findById(anyLong()))
      .thenReturn(Optional.ofNullable(item));

    item.setAvailable(false);

    ItemDto savedItems = itemService.update(itemDto);

    assertThat(savedItems, notNullValue());
    assertThat(savedItems.getId(), is(itemDto.getId()));
    assertThat(savedItems.getOwner(), is(itemDto.getOwner()));
    assertThat(savedItems.getName(), is(itemDto.getName()));
    assertThat(savedItems.getDescription(), is(itemDto.getDescription()));
    assertThat(savedItems.getAvailable(), is(itemDto.getAvailable()));

    verify(userRepository).findById(anyLong());
    verify(itemRepository).findById(anyLong());
  }
}
