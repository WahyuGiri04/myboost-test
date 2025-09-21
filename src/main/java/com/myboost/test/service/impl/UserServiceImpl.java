package com.myboost.test.service.impl;

import com.myboost.test.dto.request.UserRequest;
import com.myboost.test.dto.response.UserResponse;
import com.myboost.test.entity.User;
import com.myboost.test.exception.ResourceNotFoundException;
import com.myboost.test.mapper.UserMapper;
import com.myboost.test.repository.UserRepository;
import com.myboost.test.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findById(Integer id) {
        User user = getUser(id);
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse create(UserRequest request) {
        User user = userMapper.toEntity(request);
        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    @Override
    public UserResponse update(Integer id, UserRequest request) {
        User user = getUser(id);
        userMapper.updateEntity(request, user);
        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    @Override
    public void delete(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with id %d not found".formatted(id));
        }
        userRepository.deleteById(id);
    }

    private User getUser(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id %d not found".formatted(id)));
    }
}
