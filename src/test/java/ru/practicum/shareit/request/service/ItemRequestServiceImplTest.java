package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.practicum.shareit.request.domain.ItemRequest;
import ru.practicum.shareit.request.domain.ItemRequestDto;
import ru.practicum.shareit.request.domain.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.domain.User;

import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
  @Mock
  private ItemRequestRepository itemRequestRepository;
  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private ItemRequestServiceImpl itemRequestService;

  ItemRequestDto itemRequestDto;
  User user;

  @BeforeEach
  void setUp() {
    itemRequestDto = ItemRequestDto
      .builder()
      .id(1L)
      .requester(1L)
      .description("Описание запроса")
      .created(LocalDateTime.parse("2023-08-08T10:10:10"))
      .build();

    user = User.builder()
      .id(1L)
      .name("Jon Doe")
      .email("mail@mail.ru")
      .build();
  }

  @Test
  void create() {
    ItemRequest newItem = ItemRequestMapper.toItemRequest(itemRequestDto);

    when(userRepository.findById(anyLong()))
      .thenReturn(Optional.ofNullable(user));
    when(itemRequestRepository.save(newItem))
      .thenReturn(newItem);

    ItemRequestDto request = itemRequestService.create(itemRequestDto);

    assertThat(request.getId(), notNullValue());
    assertThat(request.getRequester(), is(newItem.getRequester()));
    assertThat(request.getDescription(), is(newItem.getDescription()));
    assertThat(request.getCreated(), is(newItem.getCreated()));

    verify(userRepository).findById(anyLong());
    verify(itemRequestRepository).save(newItem);
  }

  @Test
  void findAllByRequester() {
    ItemRequest newItem = ItemRequestMapper.toItemRequest(itemRequestDto);

    when(userRepository.findById(anyLong()))
      .thenReturn(Optional.ofNullable(user));
    when(itemRequestRepository.findAllByRequester(anyLong()))
      .thenReturn(List.of(newItem));

    List<ItemRequestDto> requests = itemRequestService.findAllByRequester(1L);

    assertThat(requests, notNullValue());
    assertThat(requests.get(0).getId(), notNullValue());
    assertThat(requests.get(0).getRequester(), is(newItem.getRequester()));
    assertThat(requests.get(0).getDescription(), is(newItem.getDescription()));
    assertThat(requests.get(0).getCreated(), is(newItem.getCreated()));

    verify(userRepository).findById(anyLong());
    verify(itemRequestRepository).findAllByRequester(anyLong());
  }

  @Test
  void findALlPageableWithWrongPaginationSize() {
    ItemRequest newItem = ItemRequestMapper.toItemRequest(itemRequestDto);

    when(userRepository.findById(anyLong()))
      .thenReturn(Optional.ofNullable(user));
    when(itemRequestRepository.findAllByRequesterIsNot(any(), anyLong()))
      .thenReturn(List.of(newItem));

    List<ItemRequestDto> requests = itemRequestService.findALlPageable(1L, 0, 20);

    assertThat(requests, notNullValue());
    assertThat(requests.get(0).getId(), notNullValue());
    assertThat(requests.get(0).getRequester(), is(newItem.getRequester()));
    assertThat(requests.get(0).getDescription(), is(newItem.getDescription()));
    assertThat(requests.get(0).getCreated(), is(newItem.getCreated()));

    verify(userRepository).findById(anyLong());
    verify(itemRequestRepository).findAllByRequesterIsNot(any(), anyLong());
  }

  @Test
  void finById() {
    ItemRequest newItem = ItemRequestMapper.toItemRequest(itemRequestDto);

    when(userRepository.findById(anyLong()))
      .thenReturn(Optional.ofNullable(user));
    when(itemRequestRepository.findById(anyLong()))
      .thenReturn(Optional.of(newItem));

    ItemRequestDto request = itemRequestService.finById(1L, 1L);

    assertThat(request, notNullValue());
    assertThat(request.getId(), notNullValue());
    assertThat(request.getRequester(), is(newItem.getRequester()));
    assertThat(request.getDescription(), is(newItem.getDescription()));
    assertThat(request.getCreated(), is(newItem.getCreated()));

    verify(userRepository).findById(anyLong());
    verify(itemRequestRepository).findById(anyLong());
  }
}
