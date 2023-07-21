package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.EmailDuplicateException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.domain.User;
import ru.practicum.shareit.user.domain.UserDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
public class UserRepository {
  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public UserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  public User create(User user) {
    try {
      checkEmail(user.getEmail());
      String message = String.format("Пользователь с почтой = %s уже существует", user.getEmail());
      log.error(message);

      throw new EmailDuplicateException(message);
    } catch (DataAccessException e) {
      log.info("Создание пользователя: {}", user);
      KeyHolder keyHolder = new GeneratedKeyHolder();
      String sqlQuery = "INSERT INTO users (name, email) VALUES (?, ?)";

      jdbcTemplate.update(con -> {
        PreparedStatement ps = con.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, user.getName());
        ps.setString(2, user.getEmail());

        return ps;
      }, keyHolder);

      Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();

      return User
        .builder()
        .id(id)
        .name(user.getName())
        .email(user.getEmail())
        .build();
    }
  }

  public User findOne(Long userId) {
    log.info("Получение пользователя с id = {}", userId);

    String sqlQuery = "SELECT * FROM users WHERE id = ?";

    try {
      return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, userId);
    } catch (Exception e) {
      String message = String.format("Пользователь с id = %s не найден", userId);

      log.error(e.getMessage());
      throw new EntityNotFoundException(message);
    }

  }

  public void checkEmail(String email) {
    log.info("Получение пользователя с id = {}", email);

    String sqlQuery = "SELECT * FROM users WHERE email = ?";

    jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, email);
  }

  public List<User> findAll() {
    log.info("Получение всех пользователей");

    String sqlQuery = "SELECT * FROM users";

    return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
  }

  public User update(UserDto user) {
    log.info("Обновление пользователя с id = {}", user.getId());

    MapSqlParameterSource parameters = new MapSqlParameterSource();
    parameters.addValue("name", user.getName());
    parameters.addValue("email", user.getEmail());
    parameters.addValue("id", user.getId());


    String sqlQuery = "UPDATE users SET " +
      "name = " +
      "CASE WHEN :name IS NULL THEN name " +
      "ELSE :name END, " +
      "email = " +
      "CASE WHEN :email IS NULL THEN email " +
      "ELSE :email END " +
      "WHERE id = :id";

    return namedParameterJdbcTemplate
      .update(
        sqlQuery,
        parameters
      ) > 0 ?
      findOne(user.getId()) :
      null;
  }

  public boolean delete(Long userId) {
    log.info("Удаление пользователя с id = {}", userId);

    String sqlQuery = "DELETE FROM users WHERE id = ?";

    return jdbcTemplate.update(sqlQuery, userId) > 0;
  }

  private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
    return User
      .builder()
      .id(resultSet.getLong("id"))
      .email(resultSet.getString("email"))
      .name(resultSet.getString("name"))
      .build();
  }
}
