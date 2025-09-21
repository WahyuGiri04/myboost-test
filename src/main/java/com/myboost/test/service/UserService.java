package com.myboost.test.service;

import com.myboost.test.dto.request.UserRequest;
import com.myboost.test.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> findAll();

    UserResponse findById(Integer id);

    UserResponse create(UserRequest request);

    UserResponse update(Integer id, UserRequest request);

    void delete(Integer id);
}
