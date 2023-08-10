package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.shareit.booking.domain.BookingItemDto;
import ru.practicum.shareit.comments.domain.CommentDto;
import ru.practicum.shareit.comments.domain.CommentNewDto;
import ru.practicum.shareit.consts.Headers;
import ru.practicum.shareit.item.domain.ItemDto;
import ru.practicum.shareit.item.domain.ItemFullDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.domain.ItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private ItemService itemService;

  @Autowired
  ObjectMapper mapper;

  ItemDto itemDto = ItemDto
    .builder()
    .id(1L)
    .name("Стол")
    .description("Стол с одной ногой")
    .available(true)
    .owner(1L)
    .requestId(1L)
    .build();

  CommentDto commentDto = CommentDto
    .builder()
    .itemId(1L)
    .authorId(1L)
    .id(1L)
    .text("Прекрасный стол, стоит устойчиво")
    .build();

  CommentNewDto commentNewDto = CommentNewDto
    .builder()
    .id(1L)
    .authorName("Имя автора")
    .text("Прекрасный стол, стоит устойчиво")
    .created(LocalDateTime.parse("2023-08-05T14:25:22"))
    .build();

  BookingItemDto lastBooking = BookingItemDto
    .builder()
    .bookerId(1L)
    .itemId(1L)
    .id(1L)
    .build();

  BookingItemDto nextBooking = BookingItemDto
    .builder()
    .bookerId(1L)
    .itemId(2L)
    .id(2L)
    .build();

  ItemRequestDto itemRequestDto = ItemRequestDto
    .builder()
    .id(1L)
    .description("Описание запроса")
    .created(LocalDateTime.parse("2022-08-04T13:26:59"))
    .requester(1L)
    .items(List.of(itemDto))
    .build();

  ItemFullDto itemFullDto = ItemFullDto
    .builder()
    .id(1L)
    .name("Стол")
    .description("Стол с одной ногой")
    .available(true)
    .owner(1L)
    .lastBooking(lastBooking)
    .nextBooking(nextBooking)
    .request(List.of(itemRequestDto))
    .comments(List.of(commentNewDto))
    .build();

  @BeforeEach
  void setUp(WebApplicationContext wac) {
    mvc = MockMvcBuilders
      .webAppContextSetup(wac)
      .build();

    itemDto = ItemDto
      .builder()
      .id(1L)
      .name("Стол")
      .description("Стол с одной ногой")
      .available(true)
      .owner(1L)
      .requestId(1L)
      .build();

    commentDto = CommentDto
      .builder()
      .itemId(1L)
      .authorId(1L)
      .id(1L)
      .text("Прекрасный стол, стоит устойчиво")
      .build();

    commentNewDto = CommentNewDto
      .builder()
      .id(1L)
      .authorName("Имя автора")
      .text("Прекрасный стол, стоит устойчиво")
      .created(LocalDateTime.parse("2023-08-05T14:25:22"))
      .build();

    itemFullDto = ItemFullDto
      .builder()
      .id(1L)
      .name("Стол")
      .description("Стол с одной ногой")
      .available(true)
      .owner(1L)
      .lastBooking(lastBooking)
      .nextBooking(nextBooking)
      .request(List.of(itemRequestDto))
      .comments(List.of(commentNewDto))
      .build();
  }

  @Test
  void create() throws Exception {
    when(itemService.create(any()))
      .thenReturn(itemDto);

    mvc.perform(post("/items")
        .content(mapper.writeValueAsString(itemDto))
        .characterEncoding(StandardCharsets.UTF_8)
        .contentType(MediaType.APPLICATION_JSON)
        .header(Headers.USER_ID, 1L)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().is(201))
      .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
      .andExpect(jsonPath("$.owner", is(itemDto.getOwner()), Long.class))
      .andExpect(jsonPath("$.name", is(itemDto.getName())))
      .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
      .andExpect(jsonPath("$.available", is(true)));
  }

  @Test
  void addComment() throws Exception {
    when(itemService.addComment(any()))
      .thenReturn(commentNewDto);

    mvc.perform(post("/items/1/comment")
        .content(mapper.writeValueAsString(commentDto))
        .characterEncoding(StandardCharsets.UTF_8)
        .contentType(MediaType.APPLICATION_JSON)
        .header(Headers.USER_ID, 1L)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(commentNewDto.getId()), Long.class))
      .andExpect(jsonPath("$.created", is(commentNewDto.getCreated().toString())))
      .andExpect(jsonPath("$.authorName", is(commentNewDto.getAuthorName())))
      .andExpect(jsonPath("$.text", is(commentNewDto.getText())));
  }

  @Test
  void findOne() throws Exception {
    when(itemService.findOne(anyLong(), anyLong()))
      .thenReturn(itemFullDto);

    mvc.perform(get("/items/1")
        .header(Headers.USER_ID, 1L)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
      .andExpect(jsonPath("$.owner", is(itemDto.getOwner()), Long.class))
      .andExpect(jsonPath("$.name", is(itemDto.getName())))
      .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
      .andExpect(jsonPath("$.available", is(true)))
      .andExpect(jsonPath("$.lastBooking.id", is(lastBooking.getId()), Long.class))
      .andExpect(jsonPath("$.lastBooking.bookerId", is(lastBooking.getBookerId()), Long.class))
      .andExpect(jsonPath("$.lastBooking.itemId", is(lastBooking.getItemId()), Long.class))
      .andExpect(jsonPath("$.nextBooking.id", is(nextBooking.getId()), Long.class))
      .andExpect(jsonPath("$.nextBooking.bookerId", is(nextBooking.getBookerId()), Long.class))
      .andExpect(jsonPath("$.nextBooking.itemId", is(nextBooking.getItemId()), Long.class))
      .andExpect(jsonPath("$.comments", hasSize(1)))
      .andExpect(jsonPath("$.comments[0].text", is(commentNewDto.getText())))
      .andExpect(jsonPath("$.comments[0].id", is(commentNewDto.getId()), Long.class))
      .andExpect(jsonPath("$.comments[0].authorName", is(commentNewDto.getAuthorName())))
      .andExpect(jsonPath("$.comments[0].created", is(commentNewDto.getCreated().toString())))
      .andExpect(jsonPath("$.request", hasSize(1)))
      .andExpect(jsonPath("$.request[0].id", is(itemRequestDto.getId()), Long.class))
      .andExpect(jsonPath("$.request[0].description", is(itemRequestDto.getDescription())))
      .andExpect(jsonPath("$.request[0].requester", is(itemRequestDto.getRequester()), Long.class))
      .andExpect(jsonPath("$.request[0].created", is(itemRequestDto.getCreated().toString())))
      .andExpect(jsonPath("$.request[0].items", hasSize(1)))
      .andExpect(jsonPath("$.request[0].items[0].id", is(itemDto.getId()), Long.class))
      .andExpect(jsonPath("$.request[0].items[0].owner", is(itemDto.getOwner()), Long.class))
      .andExpect(jsonPath("$.request[0].items[0].name", is(itemDto.getName())))
      .andExpect(jsonPath("$.request[0].items[0].description", is(itemDto.getDescription())))
      .andExpect(jsonPath("$.request[0].items[0].available", is(true)));
  }

  @Test
  void findAll() throws Exception {
    when(itemService.findAll(anyLong()))
      .thenReturn(List.of(itemFullDto));

    mvc.perform(get("/items")
        .header(Headers.USER_ID, 1L)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(1)))
      .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
      .andExpect(jsonPath("$[0].owner", is(itemDto.getOwner()), Long.class))
      .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
      .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
      .andExpect(jsonPath("$[0].available", is(true)))
      .andExpect(jsonPath("$[0].lastBooking.id", is(lastBooking.getId()), Long.class))
      .andExpect(jsonPath("$[0].lastBooking.bookerId", is(lastBooking.getBookerId()), Long.class))
      .andExpect(jsonPath("$[0].lastBooking.itemId", is(lastBooking.getItemId()), Long.class))
      .andExpect(jsonPath("$[0].nextBooking.id", is(nextBooking.getId()), Long.class))
      .andExpect(jsonPath("$[0].nextBooking.bookerId", is(nextBooking.getBookerId()), Long.class))
      .andExpect(jsonPath("$[0].nextBooking.itemId", is(nextBooking.getItemId()), Long.class))
      .andExpect(jsonPath("$[0].comments", hasSize(1)))
      .andExpect(jsonPath("$[0].comments[0].text", is(commentNewDto.getText())))
      .andExpect(jsonPath("$[0].comments[0].id", is(commentNewDto.getId()), Long.class))
      .andExpect(jsonPath("$[0].comments[0].authorName", is(commentNewDto.getAuthorName())))
      .andExpect(jsonPath("$[0].comments[0].created", is(commentNewDto.getCreated().toString())))
      .andExpect(jsonPath("$[0].request", hasSize(1)))
      .andExpect(jsonPath("$[0].request[0].id", is(itemRequestDto.getId()), Long.class))
      .andExpect(jsonPath("$[0].request[0].description", is(itemRequestDto.getDescription())))
      .andExpect(jsonPath("$[0].request[0].requester", is(itemRequestDto.getRequester()), Long.class))
      .andExpect(jsonPath("$[0].request[0].created", is(itemRequestDto.getCreated().toString())))
      .andExpect(jsonPath("$[0].request[0].items", hasSize(1)))
      .andExpect(jsonPath("$[0].request[0].items[0].id", is(itemDto.getId()), Long.class))
      .andExpect(jsonPath("$[0].request[0].items[0].owner", is(itemDto.getOwner()), Long.class))
      .andExpect(jsonPath("$[0].request[0].items[0].name", is(itemDto.getName())))
      .andExpect(jsonPath("$[0].request[0].items[0].description", is(itemDto.getDescription())))
      .andExpect(jsonPath("$[0].request[0].items[0].available", is(true)));
  }

  @Test
  void findAllBySearch() throws Exception {
    when(itemService.search(anyString()))
      .thenReturn(List.of(itemDto));

    mvc.perform(get("/items/search?text=someText")
        .header(Headers.USER_ID, 1L)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(1)))
      .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
      .andExpect(jsonPath("$[0].owner", is(itemDto.getOwner()), Long.class))
      .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
      .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
      .andExpect(jsonPath("$[0].available", is(true)));
  }

  @Test
  void update() throws Exception {
    itemDto.setOwner(2L);
    when(itemService.update(any()))
      .thenReturn(itemDto);

    mvc.perform(patch("/items/1")
        .content(mapper.writeValueAsString(ItemDto.builder().owner(2L).build()))
        .contentType(MediaType.APPLICATION_JSON)
        .header(Headers.USER_ID, 1L)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
      .andExpect(jsonPath("$.owner", is(itemDto.getOwner()), Long.class))
      .andExpect(jsonPath("$.name", is(itemDto.getName())))
      .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
      .andExpect(jsonPath("$.available", is(true)));
  }

  @Test
  void deleteItem() throws Exception {
    mvc.perform(delete("/items/1"))
      .andExpect(status().is(204));
  }
}
