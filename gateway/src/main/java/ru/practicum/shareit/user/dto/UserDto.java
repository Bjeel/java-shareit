package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserDto {
  private Long id;

  @NotBlank(groups = UserMarker.OnCreate.class)
  private String name;

  @NotBlank(groups = UserMarker.OnCreate.class)
  @Email
  private String email;
}
