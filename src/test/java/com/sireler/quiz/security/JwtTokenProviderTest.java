package com.sireler.quiz.security;

import com.sireler.quiz.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {
    
    @Mock
    private UserDetailsService userDetailsService;

    private JwtTokenProvider tokenProvider;
    
    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(tokenProvider, "validTime", 3600000L);
        ReflectionTestUtils.invokeMethod(tokenProvider, "init");
    }

    @Test
    void whenUsernameProvided_thenTokenShouldBeCreated() {
        String username = "Test";
        String token = tokenProvider.createToken(username);

        Assertions.assertNotNull(token);
    }

    @Test
    void whenValidTokenProvided_thenUserShouldBeAuthenticated() {
        String username = "Test";
        User user = new User();
        user.setUsername(username);

        UserDetails userDetails = JwtUserDetailsFactory.create(user);

        Mockito.when(userDetailsService.loadUserByUsername(username))
                .thenReturn(userDetails);

        String token = tokenProvider.createToken(username);
        Authentication authentication = tokenProvider.getAuthentication(token);
        UserDetails authenticationUserDetails = (UserDetails) authentication.getPrincipal();

        Assertions.assertEquals(username, authenticationUserDetails.getUsername());
        Assertions.assertTrue(authentication.isAuthenticated());
    }

    @Test
    void whenValidRequestHeaderProvided_thenTokenShouldBeReturned() {
        String token = "secret_token";
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);

        String resolvedToken = tokenProvider.resolveToken(request);

        Assertions.assertEquals(token, resolvedToken);
    }

    @Test
    void whenRequestHeaderNotProvided_thenTokenShouldBeNull() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization"))
                .thenReturn(null);

        String resolvedToken = tokenProvider.resolveToken(request);

        Assertions.assertNull(resolvedToken);
    }

    @Test
    void whenTokenValid_thenValidateShouldBeTrue() {
        String username = "Test";
        String token = tokenProvider.createToken(username);

        Assertions.assertTrue(tokenProvider.validateToken(token));
    }

    @Test
    void whenTokenExpired_thenExceptionShouldBeThrown() throws InterruptedException {
        String username = "Test";
        ReflectionTestUtils.setField(tokenProvider, "validTime", 5L);
        String token = tokenProvider.createToken(username);
        Thread.sleep(10);

        Exception exception = Assertions.assertThrows(JwtAuthenticationException.class, () -> {
            tokenProvider.validateToken(token);
        });

        String expectedMessage = "JWT token is expired or invalid";
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }
}
