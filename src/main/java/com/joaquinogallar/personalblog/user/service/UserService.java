package com.joaquinogallar.personalblog.user.service;

import com.joaquinogallar.personalblog.user.exception.UserNotFoundException;
import com.joaquinogallar.personalblog.user.dto.UserRequest;
import com.joaquinogallar.personalblog.user.dto.UserResponse;
import com.joaquinogallar.personalblog.user.entity.User;
import com.joaquinogallar.personalblog.user.mapper.UserMapper;
import com.joaquinogallar.personalblog.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public void checkUsernameAndEmailAvailability(UserRequest userEntity) {
        if(userRepository.existsByUsername(userEntity.username()))
            throw new IllegalArgumentException("Error: username already in use");

        if(userRepository.existsByEmail(userEntity.email()))
            throw new IllegalArgumentException("Error: email already in use");
    }

    public void checkUsernameAndEmailAvailability(UserRequest request, UUID userId) {

        if(userRepository.existsByUsernameAndIdNot(request.username(), userId)) {
            throw new IllegalArgumentException("Error: username already in use");
        }

        if(userRepository.existsByEmailAndIdNot(request.email(), userId)) {
            throw new IllegalArgumentException("Error: email already in use");
        }
    }

    // GET
    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userMapper.mapUserToDto(userRepository.findAll(pageable));
    }

    @Override
    public UserResponse getUserById(UUID id) {
        return userMapper.mapUserToDto(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User " + id + " not found")));
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        return userMapper.mapUserToDto(userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User " + email + " not found")));
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        return userMapper.mapUserToDto(userRepository.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException("User " + username + " not found")));
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // CREATE
    @Override
    @Transactional
    public String createUser(UserRequest userEntity) {
        checkUsernameAndEmailAvailability(userEntity);

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
    @Transactional
    public String updateUser(UUID id, UserRequest userEntity) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User " + id + " not found"));

        checkUsernameAndEmailAvailability(userEntity, id);

        user.setUsername(userEntity.username());
        user.setEmail(userEntity.email());

        return "User " + id + " updated successfully";
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // DELETE
    @Transactional
    @Override
    public String deleteUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User " + id + " not found"));

        userRepository.delete(user);

        return "User " + id + " deleted successfully";
    }
}
