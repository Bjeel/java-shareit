package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.domain.Booking;
import ru.practicum.shareit.booking.domain.BookingDto;
import ru.practicum.shareit.booking.domain.BookingFullDto;
import ru.practicum.shareit.config.PersistenceConfig;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"db.name=test"})
@SpringJUnitConfig({PersistenceConfig.class, BookingServiceImpl.class})
class BookingServiceImplTest {
  private final EntityManager em;
  private final BookingService service;

  BookingFullDto bookingFullDto = BookingFullDto
    .builder()
    .id(1L)
    .booker(null)
    .item(null)
    .end(LocalDateTime.parse("2025-08-04T13:26:59"))
    .start(LocalDateTime.parse("2025-08-03T13:26:59"))
    .status(Status.WAITING)
    .build();

  BookingDto bookingDto = BookingDto
    .builder()
    .id(1L)
    .booker(1L)
    .end(LocalDateTime.parse("2025-08-04T13:26:59"))
    .start(LocalDateTime.parse("2025-08-03T13:26:59"))
    .build();

  @BeforeEach
  void setUp() {
    bookingFullDto = BookingFullDto
      .builder()
      .id(1L)
      .booker(null)
      .item(null)
      .end(LocalDateTime.parse("2025-08-04T13:26:59"))
      .start(LocalDateTime.parse("2025-08-03T13:26:59"))
      .status(Status.WAITING)
      .build();

    bookingDto = BookingDto
      .builder()
      .id(1L)
      .booker(1L)
      .end(LocalDateTime.parse("2025-08-04T13:26:59"))
      .start(LocalDateTime.parse("2025-08-03T13:26:59"))
      .build();
  }

  @Test
  void saveBooking() {
    // when
    service.create(bookingDto);

    // then
    TypedQuery<Booking> query = em.createQuery("Select u from Booking u where u.id = :id", Booking.class);
    Booking user = query.setParameter("id", bookingDto.getId())
      .getSingleResult();

    assertThat(user.getId(), notNullValue());
  }
}
