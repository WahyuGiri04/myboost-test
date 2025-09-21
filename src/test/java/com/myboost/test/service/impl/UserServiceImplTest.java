package com.myboost.test.service.impl;

import com.myboost.test.dto.request.UserRequest;
import com.myboost.test.dto.response.UserResponse;
import com.myboost.test.entity.User;
import com.myboost.test.exception.ResourceNotFoundException;
import com.myboost.test.mapper.UserMapper;
import com.myboost.test.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, new UserMapper());
    }

    @Test
    void findAll_returnsMappedUsers() {
        User user = createUser(1, "John", "Doe");
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserResponse> responses = userService.findAll();

        assertThat(responses).hasSize(1);
        UserResponse response = responses.getFirst();
        assertThat(response.id()).isEqualTo(1);
        assertThat(response.firstName()).isEqualTo("John");
        verify(userRepository).findAll();
    }

    @Test
    void findById_returnsUser_whenExists() {
        User user = createUser(7, "Alice", "Smith");
        when(userRepository.findById(7)).thenReturn(Optional.of(user));

        UserResponse response = userService.findById(7);

        assertThat(response.lastName()).isEqualTo("Smith");
        verify(userRepository).findById(7);
    }

    @Test
    void findById_throwsException_whenMissing() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findById(99));
    }

    @Test
    void create_savesAndReturnsResponse() {
        UserRequest request = new UserRequest("John", "Doe", "john@example.com", "123", "system", "system");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User saved = invocation.getArgument(0, User.class);
            saved.setId(42);
            return saved;
        });

        UserResponse response = userService.create(request);

        assertThat(response.id()).isEqualTo(42);
        assertThat(response.email()).isEqualTo("john@example.com");
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getFirstName()).isEqualTo("John");
    }

    @Test
    void update_modifiesExistingUser() {
        User existing = createUser(10, "Old", "Name");
        when(userRepository.findById(10)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);
        UserRequest request = new UserRequest("New", "Name", "new@example.com", "12345", "system", "editor");

        UserResponse response = userService.update(10, request);

        assertThat(response.firstName()).isEqualTo("New");
        assertThat(existing.getEmail()).isEqualTo("new@example.com");
        verify(userRepository).save(existing);
    }

    @Test
    void delete_removesUser_whenExists() {
        when(userRepository.existsById(5)).thenReturn(true);
        doNothing().when(userRepository).deleteById(5);

        userService.delete(5);

        verify(userRepository).deleteById(5);
    }

    @Test
    void delete_throws_whenMissing() {
        when(userRepository.existsById(33)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.delete(33));
    }

    private User createUser(int id, String firstName, String lastName) {
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail("test@example.com");
        return user;
    }
}
