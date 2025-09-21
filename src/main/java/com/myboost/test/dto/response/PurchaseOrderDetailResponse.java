package com.myboost.test.dto.response;

import java.time.OffsetDateTime;

public record PurchaseOrderDetailResponse(
        Integer id,
        ItemResponse item,
        Integer itemQty,
        Integer itemCost,
        Integer itemPrice,
        String createdBy,
        String updatedBy,
        OffsetDateTime createdDatetime,
        OffsetDateTime updatedDatetime
) {
}
