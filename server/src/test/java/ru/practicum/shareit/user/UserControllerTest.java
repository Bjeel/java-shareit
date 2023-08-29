package ru.practicum.shareit.user;

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

import ru.practicum.shareit.user.domain.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
  @Autowired
  private MockMvc mvc;

  @MockBean
  private UserService userService;

  @Autowired
  ObjectMapper mapper;

  UserDto userDto = UserDto
    .builder()
    .id(1L)
    .email("mail@mail.ru")
    .name("John Doe")
    .build();

  @BeforeEach
  void setUp(WebApplicationContext wac) {
    mvc = MockMvcBuilders
      .webAppContextSetup(wac)
      .build();

    userDto = UserDto
      .builder()
      .id(1L)
      .email("mail@mail.ru")
      .name("John Doe")
      .build();
  }

  @Test
  void saveNewUser() throws Exception {
    when(userService.create(any()))
      .thenReturn(userDto);

    mvc.perform(post("/users")
        .content(mapper.writeValueAsString(userDto))
        .characterEncoding(StandardCharsets.UTF_8)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
      .andExpect(jsonPath("$.name", is(userDto.getName())))
      .andExpect(jsonPath("$.email", is(userDto.getEmail())));
  }

  @Test
  void updateUser() throws Exception {
    userDto.setName("Doe John");

    when(userService.update(any()))
      .thenReturn(userDto);

    mvc.perform(patch("/users/1")
        .content(mapper.writeValueAsString(userDto))
        .characterEncoding(StandardCharsets.UTF_8)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
      .andExpect(jsonPath("$.name", is(userDto.getName())))
      .andExpect(jsonPath("$.email", is(userDto.getEmail())));
  }

  @Test
  void deleteUser() throws Exception {
    mvc.perform(delete("/users/1"))
      .andExpect(status().is(204));
  }

  @Test
  void getAllUsers() throws Exception {
    when(userService.findAll())
      .thenReturn(List.of(userDto));

    mvc.perform(get("/users"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(1)))
      .andExpect(jsonPath("$[0].id", is(userDto.getId()), Long.class))
      .andExpect(jsonPath("$[0].name", is(userDto.getName())))
      .andExpect(jsonPath("$[0].email", is(userDto.getEmail())));
  }

  @Test
  void getOneUser() throws Exception {
    when(userService.findOne(1L))
      .thenReturn(userDto);

    mvc.perform(get("/users/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
      .andExpect(jsonPath("$.name", is(userDto.getName())))
      .andExpect(jsonPath("$.email", is(userDto.getEmail())));
  }
}
