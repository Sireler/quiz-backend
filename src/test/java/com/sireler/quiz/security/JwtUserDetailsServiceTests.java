package com.sireler.quiz.security;

import com.sireler.quiz.model.User;
import com.sireler.quiz.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class JwtUserDetailsServiceTests {

    @Mock
    private UserRepository userRepository;

    private JwtUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = new JwtUserDetailsService(userRepository);
    }

    @Test
    void whenValidUsernameProvided_thenUserShouldBeLoaded() {
        String username = "Test";
        User user = new User();
        user.setUsername(username);

        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        Assertions.assertEquals(username, userDetails.getUsername());
    }

    @Test
    void whenInvalidUsernameProvided_thenExceptionShouldBeThrown() {
        String username = "Test";

        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(null);

        Exception exception = Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });

        String expectedMessage = username + " not found";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}
