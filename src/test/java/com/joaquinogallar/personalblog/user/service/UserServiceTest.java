package com.joaquinogallar.personalblog.user.service;

import com.joaquinogallar.personalblog.user.dto.UserRequest;
import com.joaquinogallar.personalblog.user.dto.UserResponse;
import com.joaquinogallar.personalblog.user.entity.Role;
import com.joaquinogallar.personalblog.user.entity.User;
import com.joaquinogallar.personalblog.user.exception.UserNotFoundException;
import com.joaquinogallar.personalblog.user.mapper.UserMapper;
import com.joaquinogallar.personalblog.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UUID userId;
    private User user;
    private UserResponse userResponse;
    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        user = User.builder()
                .id(userId)
                .username("joaquin")
                .email("joaquin@example.com")
                .passwordHash("hashed_password")
                .roles(Set.of(Role.USER))
                .isActive(true)
                .build();

        userResponse = new UserResponse(
                userId,
                "joaquin",
                "joaquin@example.com",
                LocalDateTime.now(),
                Set.of(Role.USER)
        );

        userRequest = new UserRequest("joaquin", "joaquin@example.com", "password123");
    }

    // -------------------------------------------------------------------------
    // GET
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("getAllUsers")
    class GetAllUsers {

        @Test
        @DisplayName("should return paginated list of users")
        void shouldReturnPaginatedUsers() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<User> userPage = new PageImpl<>(List.of(user));
            Page<UserResponse> responsePage = new PageImpl<>(List.of(userResponse));

            given(userRepository.findAll(pageable)).willReturn(userPage);
            given(userMapper.mapUserToDto(userPage)).willReturn(responsePage);

            Page<UserResponse> result = userService.getAllUsers(pageable);

            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getContent().get(0).username()).isEqualTo("joaquin");
        }
    }

    @Nested
    @DisplayName("getUserById")
    class GetUserById {

        @Test
        @DisplayName("should return user when id exists")
        void shouldReturnUserWhenIdExists() {
            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(userMapper.mapUserToDto(user)).willReturn(userResponse);

            UserResponse result = userService.getUserById(userId);

            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(userId);
            assertThat(result.username()).isEqualTo("joaquin");
        }

        @Test
        @DisplayName("should throw UserNotFoundException when id does not exist")
        void shouldThrowExceptionWhenIdNotFound() {
            UUID unknownId = UUID.randomUUID();
            given(userRepository.findById(unknownId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> userService.getUserById(unknownId))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining(unknownId.toString());
        }
    }

    @Nested
    @DisplayName("getUserByEmail")
    class GetUserByEmail {

        @Test
        @DisplayName("should return user when email exists")
        void shouldReturnUserWhenEmailExists() {
            given(userRepository.findUserByEmail("joaquin@example.com")).willReturn(Optional.of(user));
            given(userMapper.mapUserToDto(user)).willReturn(userResponse);

            UserResponse result = userService.getUserByEmail("joaquin@example.com");

            assertThat(result.email()).isEqualTo("joaquin@example.com");
        }

        @Test
        @DisplayName("should throw UserNotFoundException when email does not exist")
        void shouldThrowExceptionWhenEmailNotFound() {
            given(userRepository.findUserByEmail("ghost@example.com")).willReturn(Optional.empty());

            assertThatThrownBy(() -> userService.getUserByEmail("ghost@example.com"))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining("ghost@example.com");
        }
    }

    @Nested
    @DisplayName("getUserByUsername")
    class GetUserByUsername {

        @Test
        @DisplayName("should return user when username exists")
        void shouldReturnUserWhenUsernameExists() {
            given(userRepository.findUserByUsername("joaquin")).willReturn(Optional.of(user));
            given(userMapper.mapUserToDto(user)).willReturn(userResponse);

            UserResponse result = userService.getUserByUsername("joaquin");

            assertThat(result.username()).isEqualTo("joaquin");
        }

        @Test
        @DisplayName("should throw UserNotFoundException when username does not exist")
        void shouldThrowExceptionWhenUsernameNotFound() {
            given(userRepository.findUserByUsername("ghost")).willReturn(Optional.empty());

            assertThatThrownBy(() -> userService.getUserByUsername("ghost"))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining("ghost");
        }
    }

    // -------------------------------------------------------------------------
    // CREATE
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("createUser")
    class CreateUser {

        @Test
        @DisplayName("should create user and return success message")
        void shouldCreateUserSuccessfully() {
            given(userRepository.existsByUsername("joaquin")).willReturn(false);
            given(userRepository.existsByEmail("joaquin@example.com")).willReturn(false);

            String result = userService.createUser(userRequest);

            verify(userRepository).save(any(User.class));
            assertThat(result).isEqualTo("User created successfully");
        }

        @Test
        @DisplayName("should throw IllegalArgumentException when username is already taken")
        void shouldThrowWhenUsernameAlreadyExists() {
            given(userRepository.existsByUsername("joaquin")).willReturn(true);

            assertThatThrownBy(() -> userService.createUser(userRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("username already in use");

            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("should throw IllegalArgumentException when email is already taken")
        void shouldThrowWhenEmailAlreadyExists() {
            given(userRepository.existsByUsername("joaquin")).willReturn(false);
            given(userRepository.existsByEmail("joaquin@example.com")).willReturn(true);

            assertThatThrownBy(() -> userService.createUser(userRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("email already in use");

            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("should persist user with correct username and email")
        void shouldPersistUserWithCorrectFields() {
            given(userRepository.existsByUsername("joaquin")).willReturn(false);
            given(userRepository.existsByEmail("joaquin@example.com")).willReturn(false);

            userService.createUser(userRequest);

            verify(userRepository).save(argThat(savedUser ->
                    savedUser.getUsername().equals("joaquin") &&
                            savedUser.getEmail().equals("joaquin@example.com")
            ));
        }
    }

    // -------------------------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("updateUser")
    class UpdateUser {

        @Test
        @DisplayName("should update user and return success message")
        void shouldUpdateUserSuccessfully() {
            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(userRepository.existsByUsernameAndIdNot("joaquin", userId)).willReturn(false);
            given(userRepository.existsByEmailAndIdNot("joaquin@example.com", userId)).willReturn(false);

            String result = userService.updateUser(userId, userRequest);

            assertThat(result).contains(userId.toString());
            assertThat(result).containsIgnoringCase("updated");
        }

        @Test
        @DisplayName("should throw UserNotFoundException when user to update does not exist")
        void shouldThrowWhenUserToUpdateNotFound() {
            UUID unknownId = UUID.randomUUID();
            given(userRepository.findById(unknownId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> userService.updateUser(unknownId, userRequest))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining(unknownId.toString());
        }

        @Test
        @DisplayName("should throw IllegalArgumentException when new username belongs to another user")
        void shouldThrowWhenUsernameConflictsWithAnotherUser() {
            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(userRepository.existsByUsernameAndIdNot("joaquin", userId)).willReturn(true);

            assertThatThrownBy(() -> userService.updateUser(userId, userRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("username already in use");
        }

        @Test
        @DisplayName("should throw IllegalArgumentException when new email belongs to another user")
        void shouldThrowWhenEmailConflictsWithAnotherUser() {
            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(userRepository.existsByUsernameAndIdNot("joaquin", userId)).willReturn(false);
            given(userRepository.existsByEmailAndIdNot("joaquin@example.com", userId)).willReturn(true);

            assertThatThrownBy(() -> userService.updateUser(userId, userRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("email already in use");
        }

        @Test
        @DisplayName("should apply new username and email to the entity")
        void shouldApplyNewFieldsToUser() {
            UserRequest updatedRequest = new UserRequest("newname", "new@example.com", "newpass");

            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(userRepository.existsByUsernameAndIdNot("newname", userId)).willReturn(false);
            given(userRepository.existsByEmailAndIdNot("new@example.com", userId)).willReturn(false);

            userService.updateUser(userId, updatedRequest);

            assertThat(user.getUsername()).isEqualTo("newname");
            assertThat(user.getEmail()).isEqualTo("new@example.com");
        }
    }

    // -------------------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("deleteUser")
    class DeleteUser {

        @Test
        @DisplayName("should delete user and return success message")
        void shouldDeleteUserSuccessfully() {
            given(userRepository.findById(userId)).willReturn(Optional.of(user));

            String result = userService.deleteUser(userId);

            verify(userRepository).delete(user);
            assertThat(result).contains(userId.toString());
            assertThat(result).containsIgnoringCase("deleted");
        }

        @Test
        @DisplayName("should throw UserNotFoundException when user to delete does not exist")
        void shouldThrowWhenUserToDeleteNotFound() {
            UUID unknownId = UUID.randomUUID();
            given(userRepository.findById(unknownId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> userService.deleteUser(unknownId))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining(unknownId.toString());

            verify(userRepository, never()).delete(any());
        }
    }
}