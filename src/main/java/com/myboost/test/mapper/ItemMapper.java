package com.myboost.test.mapper;

import com.myboost.test.dto.request.ItemRequest;
import com.myboost.test.dto.response.ItemResponse;
import com.myboost.test.entity.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    public Item toEntity(ItemRequest request) {
        Item item = new Item();
        item.setName(request.name());
        item.setDescription(request.description());
        item.setPrice(request.price());
        item.setCost(request.cost());
        item.setCreatedBy(request.createdBy());
        item.setUpdatedBy(request.updatedBy());
        return item;
    }

    public void updateEntity(ItemRequest request, Item entity) {
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setPrice(request.price());
        entity.setCost(request.cost());
        if (request.updatedBy() != null) {
            entity.setUpdatedBy(request.updatedBy());
        }
    }

    public ItemResponse toResponse(Item entity) {
        return new ItemResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getCost(),
                entity.getCreatedBy(),
                entity.getUpdatedBy(),
                entity.getCreatedDatetime(),
                entity.getUpdatedDatetime()
        );
    }
}
