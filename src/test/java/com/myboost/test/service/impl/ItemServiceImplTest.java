package com.myboost.test.service.impl;

import com.myboost.test.dto.request.ItemRequest;
import com.myboost.test.dto.response.ItemResponse;
import com.myboost.test.entity.Item;
import com.myboost.test.exception.ResourceNotFoundException;
import com.myboost.test.mapper.ItemMapper;
import com.myboost.test.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(itemRepository, new ItemMapper());
    }

    @Test
    void findAll_returnsItems() {
        Item item = createItem(1, "Laptop");
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<ItemResponse> responses = itemService.findAll();

        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().name()).isEqualTo("Laptop");
        verify(itemRepository).findAll();
    }

    @Test
    void findById_returnsItem() {
        Item item = createItem(2, "Mouse");
        when(itemRepository.findById(2)).thenReturn(Optional.of(item));

        ItemResponse response = itemService.findById(2);

        assertThat(response.name()).isEqualTo("Mouse");
        verify(itemRepository).findById(2);
    }

    @Test
    void findById_throws_whenMissing() {
        when(itemRepository.findById(9)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> itemService.findById(9));
    }

    @Test
    void create_persistsItem() {
        ItemRequest request = new ItemRequest("Keyboard", "Mechanical", 100, 70, "system", "system");
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> {
            Item saved = invocation.getArgument(0, Item.class);
            saved.setId(8);
            return saved;
        });

        ItemResponse response = itemService.create(request);

        assertThat(response.id()).isEqualTo(8);
        ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);
        verify(itemRepository).save(captor.capture());
        assertThat(captor.getValue().getDescription()).isEqualTo("Mechanical");
    }

    @Test
    void update_updatesItem() {
        Item existing = createItem(3, "Old");
        when(itemRepository.findById(3)).thenReturn(Optional.of(existing));
        when(itemRepository.save(existing)).thenReturn(existing);
        ItemRequest request = new ItemRequest("New", "Updated", 150, 90, "system", "editor");

        ItemResponse response = itemService.update(3, request);

        assertThat(response.name()).isEqualTo("New");
        assertThat(existing.getPrice()).isEqualTo(150);
        verify(itemRepository).save(existing);
    }

    @Test
    void delete_removesItem_whenExists() {
        when(itemRepository.existsById(4)).thenReturn(true);
        doNothing().when(itemRepository).deleteById(4);

        itemService.delete(4);

        verify(itemRepository).deleteById(4);
    }

    @Test
    void delete_throws_whenMissing() {
        when(itemRepository.existsById(11)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> itemService.delete(11));
    }

    private Item createItem(int id, String name) {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setDescription("desc");
        item.setPrice(10);
        item.setCost(5);
        return item;
    }
}
