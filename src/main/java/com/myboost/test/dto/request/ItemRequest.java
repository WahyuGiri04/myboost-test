package com.myboost.test.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ItemRequest(
        @NotBlank @Size(max = 500) String name,
        @Size(max = 500) String description,
        @NotNull @PositiveOrZero Integer price,
        @NotNull @PositiveOrZero Integer cost,
        @Size(max = 100) String createdBy,
        @Size(max = 100) String updatedBy
) {
}
