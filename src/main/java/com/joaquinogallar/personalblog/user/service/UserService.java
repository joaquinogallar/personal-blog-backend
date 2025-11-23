package com.joaquinogallar.personalblog.user.service;

import com.joaquinogallar.personalblog.user.dto.UserRequest;
import com.joaquinogallar.personalblog.user.dto.UserResponse;
import com.joaquinogallar.personalblog.user.entity.User;
import com.joaquinogallar.personalblog.user.mapper.UserMapper;
import com.joaquinogallar.personalblog.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService implements IUserSerivce {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    // GET
    @Override
    public List<UserResponse> getAllUsers() {
        return userMapper.mapUserToDto(userRepository.findAll());
    }

    @Override
    public UserResponse getUserById(UUID id) {
        return userMapper.mapUserToDto(userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User " + id + " not found")));
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // CREATE
    @Override
    public String createUser(UserRequest userEntity) {
        User newUser = User.builder()
                .username(userEntity.username())
                .email(userEntity.email())
                .passwordHash(userEntity.password()) // this should be hashed when security is implemented
                .build();

        userRepository.save(newUser);

        return "User created successfully";
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // UPDATE
    @Override
    public String updateUser(UUID id, UserResponse userEntity) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User " + id + " not found"));

        user.setUsername(userEntity.username());
        user.setEmail(userEntity.email());

        return "User " + id + " updated successfully";
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // DELETE
    @Override
    public String deleteUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User " + id + " not found"));

        userRepository.delete(user);

        return "User " + id + " deleted successfully";
    }
}
