package ru.practicum.shareit.item.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "items")
public class Item {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotBlank
  private Long id;

  @NotBlank
  private String name;

  @NotBlank
  private String description;

  @NotNull
  private Boolean available;
  
  private Long owner;

  private Long request;
}
