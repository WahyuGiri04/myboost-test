package com.myboost.test.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record PurchaseOrderDetailRequest(
        @NotNull Integer itemId,
        @NotNull @Positive Integer itemQty,
        @NotNull @PositiveOrZero Integer itemCost,
        @NotNull @PositiveOrZero Integer itemPrice,
        @Size(max = 100) String createdBy,
        @Size(max = 100) String updatedBy
) {
}
