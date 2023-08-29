package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.comments.domain.CommentDto;
import ru.practicum.shareit.item.domain.ItemDto;

@Service
public class ItemClient extends BaseClient {
  private static final String API_PREFIX = "/items";

  @Autowired
  public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
    super(
      builder
        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
        .build()
    );
  }

  public ResponseEntity<Object> create(ItemDto itemDto) {
    return post("/", itemDto.getOwner(), itemDto);
  }

  public ResponseEntity<Object> findOne(Long itemId, Long userId) {
    return get("/" + itemId, userId);
  }

  public ResponseEntity<Object> findAll(Long userId) {
    return get("/", userId);
  }

  public ResponseEntity<Object> update(ItemDto itemDto) {
    return patch("/" + itemDto.getId(), itemDto.getOwner(), itemDto);
  }

  public void delete(Long itemId) {
    delete("/" + itemId);
  }

  public ResponseEntity<Object> addComment(CommentDto commentDto) {
    return post(String.format("/%s/comment", commentDto.getItemId()), commentDto.getAuthorId(), commentDto);
  }

  public ResponseEntity<Object> search(String text) {
    return get(String.format("/search?text=%s", text));
  }
}
