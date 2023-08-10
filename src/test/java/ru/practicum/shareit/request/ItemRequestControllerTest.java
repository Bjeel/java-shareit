package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.shareit.consts.Headers;
import ru.practicum.shareit.item.domain.ItemDto;
import ru.practicum.shareit.request.domain.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
  @Autowired
  private MockMvc mvc;

  @MockBean
  private ItemRequestService itemRequestService;

  @Autowired
  ObjectMapper mapper;

  ItemDto itemDto = ItemDto
    .builder()
    .id(1L)
    .name("John Doe")
    .description("Описание предмета")
    .available(true)
    .owner(1L)
    .requestId(1L)
    .build();

  ItemRequestDto itemRequestDto = ItemRequestDto
    .builder()
    .id(1L)
    .description("Описание запроса")
    .created(LocalDateTime.parse("2022-08-04T13:26:59"))
    .requester(1L)
    .items(List.of(itemDto))
    .build();

  @BeforeEach
  void setUp(WebApplicationContext wac) {
    mvc = MockMvcBuilders
      .webAppContextSetup(wac)
      .build();

    itemRequestDto = ItemRequestDto
      .builder()
      .id(1L)
      .description("Описание запроса")
      .created(LocalDateTime.parse("2022-08-04T13:26:59"))
      .requester(1L)
      .items(List.of(itemDto))
      .build();
  }

  @Test
  void crete() throws Exception {
    when(itemRequestService.create(any()))
      .thenReturn(itemRequestDto);

    mvc.perform(post("/requests")
        .content(mapper.writeValueAsString(itemRequestDto))
        .header(Headers.USER_ID, 1L)
        .characterEncoding(StandardCharsets.UTF_8)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().is(HttpStatus.CREATED.value()))
      .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
      .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
      .andExpect(jsonPath("$.requester", is(itemRequestDto.getRequester()), Long.class))
      .andExpect(jsonPath("$.created", is(itemRequestDto.getCreated().toString())));
  }

  @Test
  void findAllByRequester() throws Exception {
    when(itemRequestService.findAllByRequester(1L))
      .thenReturn(List.of(itemRequestDto));

    mvc.perform(get("/requests")
        .header(Headers.USER_ID, 1L))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(1)))
      .andExpect(jsonPath("$[0].id", is(itemRequestDto.getId()), Long.class))
      .andExpect(jsonPath("$[0].description", is(itemRequestDto.getDescription())))
      .andExpect(jsonPath("$[0].requester", is(itemRequestDto.getRequester()), Long.class))
      .andExpect(jsonPath("$[0].created", is(itemRequestDto.getCreated().toString())))
      .andExpect(jsonPath("$[0].items", hasSize(1)))
      .andExpect(jsonPath("$[0].items[0].id", is(itemDto.getId()), Long.class))
      .andExpect(jsonPath("$[0].items[0].name", is(itemDto.getName())))
      .andExpect(jsonPath("$[0].items[0].description", is(itemDto.getDescription())))
      .andExpect(jsonPath("$[0].items[0].available", is(itemDto.getAvailable())))
      .andExpect(jsonPath("$[0].items[0].owner", is(itemDto.getOwner()), Long.class))
      .andExpect(jsonPath("$[0].items[0].requestId", is(itemDto.getRequestId()), Long.class));
  }

  @Test
  void findAll() throws Exception {
    when(itemRequestService.findALlPageable(1L, 0, 10))
      .thenReturn(List.of(itemRequestDto));

    mvc.perform(get("/requests/all")
        .header(Headers.USER_ID, 1L))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(1)))
      .andExpect(jsonPath("$[0].id", is(itemRequestDto.getId()), Long.class))
      .andExpect(jsonPath("$[0].description", is(itemRequestDto.getDescription())))
      .andExpect(jsonPath("$[0].requester", is(itemRequestDto.getRequester()), Long.class))
      .andExpect(jsonPath("$[0].created", is(itemRequestDto.getCreated().toString())))
      .andExpect(jsonPath("$[0].items", hasSize(1)))
      .andExpect(jsonPath("$[0].items[0].id", is(itemDto.getId()), Long.class))
      .andExpect(jsonPath("$[0].items[0].name", is(itemDto.getName())))
      .andExpect(jsonPath("$[0].items[0].description", is(itemDto.getDescription())))
      .andExpect(jsonPath("$[0].items[0].available", is(itemDto.getAvailable())))
      .andExpect(jsonPath("$[0].items[0].owner", is(itemDto.getOwner()), Long.class))
      .andExpect(jsonPath("$[0].items[0].requestId", is(itemDto.getRequestId()), Long.class));
  }

  @Test
  void findById() throws Exception {
    when(itemRequestService.finById(1L, 1L))
      .thenReturn(itemRequestDto);

    mvc.perform(get("/requests/1")
        .content(mapper.writeValueAsString(itemRequestDto))
        .header(Headers.USER_ID, 1L)
        .characterEncoding(StandardCharsets.UTF_8)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
      .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
      .andExpect(jsonPath("$.requester", is(itemRequestDto.getRequester()), Long.class))
      .andExpect(jsonPath("$.created", is(itemRequestDto.getCreated().toString())))
      .andExpect(jsonPath("$.items", hasSize(1)))
      .andExpect(jsonPath("$.items[0].id", is(itemDto.getId()), Long.class))
      .andExpect(jsonPath("$.items[0].name", is(itemDto.getName())))
      .andExpect(jsonPath("$.items[0].description", is(itemDto.getDescription())))
      .andExpect(jsonPath("$.items[0].available", is(itemDto.getAvailable())))
      .andExpect(jsonPath("$.items[0].owner", is(itemDto.getOwner()), Long.class))
      .andExpect(jsonPath("$.items[0].requestId", is(itemDto.getRequestId()), Long.class));
  }
}
