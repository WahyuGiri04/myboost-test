package com.myboost.test.dto.response;

import java.time.OffsetDateTime;

public record UserResponse(
        Integer id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String createdBy,
        String updatedBy,
        OffsetDateTime createdDatetime,
        OffsetDateTime updatedDatetime
) {
}
