package com.sireler.quiz.service;

import com.sireler.quiz.exception.ApiException;
import com.sireler.quiz.model.User;
import com.sireler.quiz.repository.UserRepository;
import com.sireler.quiz.security.JwtTokenProvider;
import com.sireler.quiz.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(
                userRepository,
                passwordEncoder,
                authenticationManager,
                jwtTokenProvider
        );
    }

    @Test
    void whenValidUsername_thenUserShouldBeFound() {
        String username = "Test";
        User user = new User();
        user.setId(1L);
        user.setUsername(username);
        user.setPassword("password");
        user.setEmail("test@mail.com");

        Mockito.when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(user);

        User found = userService.findByUsername(username);

        Assertions.assertEquals(username, found.getUsername());
    }

    @Test
    void whenValidUser_thenUserShouldBeSaved() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Save");
        user.setEmail("save@mail.com");
        user.setPassword("password");

        Mockito.when(userRepository.save(user))
                .thenReturn(user);

        User registered = userService.register(user);

        Assertions.assertEquals(user.getUsername(), registered.getUsername());
    }

    @Test
    void whenValidUsernameAndPassword_thenShouldReturnToken() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Test");
        user.setPassword("password");
        user.setEmail("test@mail.com");

        Mockito.when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(user);

        userService.getToken(user.getUsername(), user.getPassword());

        Mockito.verify(jwtTokenProvider, Mockito.times(1))
                .createToken(user.getUsername());
    }

    @Test
    void whenUsernameAlreadyExists_thenExceptionShouldBeThrown() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Save");
        user.setEmail("save@mail.com");
        user.setPassword("password");

        Mockito.when(userRepository.existsByUsername(user.getUsername()))
                .thenReturn(true);

        Exception exception = Assertions.assertThrows(ApiException.class, () -> {
            userService.register(user);
        });

        String expectedMessage = "Username is already taken";
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void whenEmailAlreadyExists_thenExceptionShouldBeThrown() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Save");
        user.setEmail("save@mail.com");
        user.setPassword("password");

        Mockito.when(userRepository.existsByEmail(user.getEmail()))
                .thenReturn(true);

        Exception exception = Assertions.assertThrows(ApiException.class, () -> {
            userService.register(user);
        });

        String expectedMessage = "Email is already taken";
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }
}
