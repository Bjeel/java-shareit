package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.domain.Item;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
public class ItemRepository {
  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public ItemRepository(
    JdbcTemplate jdbcTemplate,
    NamedParameterJdbcTemplate namedParameterJdbcTemplate
  ) {
    this.jdbcTemplate = jdbcTemplate;
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }


  public Item create(Item item) {
    log.info("Создание айтема: {}", item);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    String sqlQuery = "INSERT INTO items (name, description, available, owner) VALUES (?, ?, ?, ?)";

    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, item.getName());
      ps.setString(2, item.getDescription());
      ps.setBoolean(3, item.getAvailable());
      ps.setLong(4, item.getOwner());

      return ps;
    }, keyHolder);

    Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();

    return Item
      .builder()
      .id(id)
      .name(item.getName())
      .description(item.getDescription())
      .available(item.getAvailable())
      .owner(item.getOwner())
      .build();
  }

  public Item findOne(Long itemId) {
    log.info("Получение айтема с id = {}", itemId);

    String sqlQuery = "SELECT * FROM items WHERE id = ?";

    return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToItem, itemId);
  }

  public List<Item> findAll(Long userId) {
    log.info("Получение всех айтемов для пользователя {}", userId);

    String sqlQuery = "SELECT * FROM items WHERE owner = ?";

    return jdbcTemplate.query(sqlQuery, this::mapRowToItem, userId);
  }

  public List<Item> findAll(String text) {
    log.info("Получение всех айтемов где название или описание содержит {}", text);

    MapSqlParameterSource parameters = new MapSqlParameterSource();
    parameters.addValue("text", text);

    String sqlQuery = "SELECT * FROM items WHERE available = true AND ( " +
      "LOWER(name) LIKE LOWER(CONCAT('%',:text,'%')) OR " +
      "LOWER(description) LIKE LOWER(CONCAT('%',:text,'%'))) ";

    return namedParameterJdbcTemplate.query(sqlQuery, parameters, this::mapRowToItem);
  }

  public Item update(Item item) {
    log.info("Обновление айтема");

    MapSqlParameterSource parameters = new MapSqlParameterSource();
    parameters.addValue("name", item.getName());
    parameters.addValue("description", item.getDescription());
    parameters.addValue("available", item.getAvailable());
    parameters.addValue("owner", item.getOwner());
    parameters.addValue("id", item.getId());

    String sqlQuery = "UPDATE items SET " +
      "name = " +
      "CASE WHEN :name IS NULL THEN name " +
      "ELSE :name END, " +
      "description = " +
      "CASE WHEN :description IS NULL THEN description " +
      "ELSE :description END, " +
      "available = " +
      "CASE WHEN :available IS NULL THEN available " +
      "ELSE :available END " +
      "WHERE id = :id AND owner = :owner ";

    if (namedParameterJdbcTemplate.update(sqlQuery, parameters) > 0) {
      return findOne(item.getId());
    }

    throw new EntityNotFoundException("Нет подходящей айтема для обновления");
  }

  public boolean delete(Long itemId) {
    log.info("Удаление айтема с id = {}", itemId);

    String sqlQuery = "DELETE FROM items WHERE id = ?";

    return jdbcTemplate.update(sqlQuery, itemId) > 0;
  }

  private Item mapRowToItem(ResultSet resultSet, int rowNum) throws SQLException {
    return Item
      .builder()
      .id(resultSet.getLong("id"))
      .name(resultSet.getString("name"))
      .description(resultSet.getString("description"))
      .available(resultSet.getBoolean("available"))
      .owner(resultSet.getLong("owner"))
      .build();
  }
}
