package com.myboost.test.mapper;

import com.myboost.test.dto.response.ItemResponse;
import com.myboost.test.dto.response.PurchaseOrderDetailResponse;
import com.myboost.test.dto.response.PurchaseOrderResponse;
import com.myboost.test.entity.PurchaseOrderDetail;
import com.myboost.test.entity.PurchaseOrderHeader;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PurchaseOrderMapper {

    private final ItemMapper itemMapper;

    public PurchaseOrderMapper(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    public PurchaseOrderResponse toResponse(PurchaseOrderHeader header) {
        List<PurchaseOrderDetailResponse> detailResponses = header.getDetails().stream()
                .map(this::toDetailResponse)
                .toList();

        return new PurchaseOrderResponse(
                header.getId(),
                header.getPoDatetime(),
                header.getDescription(),
                header.getTotalPrice(),
                header.getTotalCost(),
                detailResponses,
                header.getCreatedBy(),
                header.getUpdatedBy(),
                header.getCreatedDatetime(),
                header.getUpdatedDatetime()
        );
    }

    private PurchaseOrderDetailResponse toDetailResponse(PurchaseOrderDetail detail) {
        ItemResponse itemResponse = itemMapper.toResponse(detail.getItem());
        return new PurchaseOrderDetailResponse(
                detail.getId(),
                itemResponse,
                detail.getItemQty(),
                detail.getItemCost(),
                detail.getItemPrice(),
                detail.getCreatedBy(),
                detail.getUpdatedBy(),
                detail.getCreatedDatetime(),
                detail.getUpdatedDatetime()
        );
    }
}
