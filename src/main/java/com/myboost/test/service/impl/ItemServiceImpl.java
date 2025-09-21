package com.myboost.test.service.impl;

import com.myboost.test.dto.request.ItemRequest;
import com.myboost.test.dto.response.ItemResponse;
import com.myboost.test.entity.Item;
import com.myboost.test.exception.ResourceNotFoundException;
import com.myboost.test.mapper.ItemMapper;
import com.myboost.test.repository.ItemRepository;
import com.myboost.test.service.ItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemServiceImpl(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponse> findAll() {
        return itemRepository.findAll().stream()
                .map(itemMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponse findById(Integer id) {
        Item item = getItem(id);
        return itemMapper.toResponse(item);
    }

    @Override
    public ItemResponse create(ItemRequest request) {
        Item entity = itemMapper.toEntity(request);
        Item saved = itemRepository.save(entity);
        return itemMapper.toResponse(saved);
    }

    @Override
    public ItemResponse update(Integer id, ItemRequest request) {
        Item entity = getItem(id);
        itemMapper.updateEntity(request, entity);
        Item saved = itemRepository.save(entity);
        return itemMapper.toResponse(saved);
    }

    @Override
    public void delete(Integer id) {
        if (!itemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Item with id %d not found".formatted(id));
        }
        itemRepository.deleteById(id);
    }

    private Item getItem(Integer id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item with id %d not found".formatted(id)));
    }
}
