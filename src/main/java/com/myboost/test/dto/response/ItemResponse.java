package com.myboost.test.dto.response;

import java.time.OffsetDateTime;

public record ItemResponse(
        Integer id,
        String name,
        String description,
        Integer price,
        Integer cost,
        String createdBy,
        String updatedBy,
        OffsetDateTime createdDatetime,
        OffsetDateTime updatedDatetime
) {
}
