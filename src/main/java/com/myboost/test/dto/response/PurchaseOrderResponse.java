package com.myboost.test.dto.response;

import java.time.OffsetDateTime;
import java.util.List;

public record PurchaseOrderResponse(
        Integer id,
        OffsetDateTime poDatetime,
        String description,
        Integer totalPrice,
        Integer totalCost,
        List<PurchaseOrderDetailResponse> details,
        String createdBy,
        String updatedBy,
        OffsetDateTime createdDatetime,
        OffsetDateTime updatedDatetime
) {
}
