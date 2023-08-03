package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.domain.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
  List<ItemRequest> findAllByRequester(Long requesterId);

  List<ItemRequest> findAllByRequesterIsNot(PageRequest page, Long userId);
}
