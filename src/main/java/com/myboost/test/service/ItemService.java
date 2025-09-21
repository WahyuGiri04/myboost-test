package com.myboost.test.service;

import com.myboost.test.dto.request.ItemRequest;
import com.myboost.test.dto.response.ItemResponse;

import java.util.List;

public interface ItemService {

    List<ItemResponse> findAll();

    ItemResponse findById(Integer id);

    ItemResponse create(ItemRequest request);

    ItemResponse update(Integer id, ItemRequest request);

    void delete(Integer id);
}
