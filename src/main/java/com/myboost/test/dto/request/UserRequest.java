package com.myboost.test.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank @Size(max = 500) String firstName,
        @NotBlank @Size(max = 500) String lastName,
        @Email @Size(max = 255) String email,
        @Size(max = 50) String phone,
        @Size(max = 100) String createdBy,
        @Size(max = 100) String updatedBy
) {
}
