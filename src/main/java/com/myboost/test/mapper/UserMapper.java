package com.myboost.test.mapper;

import com.myboost.test.dto.request.UserRequest;
import com.myboost.test.dto.response.UserResponse;
import com.myboost.test.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequest request) {
        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setCreatedBy(request.createdBy());
        user.setUpdatedBy(request.updatedBy());
        return user;
    }

    public void updateEntity(UserRequest request, User entity) {
        entity.setFirstName(request.firstName());
        entity.setLastName(request.lastName());
        entity.setEmail(request.email());
        entity.setPhone(request.phone());
        if (request.updatedBy() != null) {
            entity.setUpdatedBy(request.updatedBy());
        }
    }

    public UserResponse toResponse(User entity) {
        return new UserResponse(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getCreatedBy(),
                entity.getUpdatedBy(),
                entity.getCreatedDatetime(),
                entity.getUpdatedDatetime()
        );
    }
}
