package com.myboost.test.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.List;

public record PurchaseOrderRequest(
        @NotNull @PastOrPresent OffsetDateTime poDatetime,
        @Size(max = 500) String description,
        @NotNull @Size(min = 1) List<@Valid PurchaseOrderDetailRequest> details,
        @Size(max = 100) String createdBy,
        @Size(max = 100) String updatedBy
) {
}
