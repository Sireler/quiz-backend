package com.sireler.quiz.controller;

import com.sireler.quiz.dto.LoginRequestDto;
import com.sireler.quiz.dto.LoginResponseDto;
import com.sireler.quiz.dto.RegisterRequestDto;
import com.sireler.quiz.exception.ApiException;
import com.sireler.quiz.model.User;
import com.sireler.quiz.security.JwtTokenProvider;
import com.sireler.quiz.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthenticationController {

    private AuthenticationManager authenticationManager;

    private JwtTokenProvider jwtTokenProvider;

    private UserService userService;

    private ModelMapper modelMapper;

    private static final String USER_NOT_FOUND_MESSAGE = "User with username %s not found.";

    private static final String INVALID_USERNAME_OR_PASSWORD_MESSAGE = "Invalid username or password.";

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtTokenProvider jwtTokenProvider,
                                    UserService userService,
                                    ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        try {
            String username = loginRequestDto.getUsername();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, loginRequestDto.getPassword())
            );

            User user = userService.findByUsername(username);

            if (user == null) {
                throw new ApiException(HttpStatus.BAD_REQUEST, String.format(USER_NOT_FOUND_MESSAGE, username));
            }

            String token = jwtTokenProvider.createToken(username);
            LoginResponseDto loginResponseDto = new LoginResponseDto(token);

            return ResponseEntity.ok(loginResponseDto);
        } catch (AuthenticationException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, INVALID_USERNAME_OR_PASSWORD_MESSAGE);
        }
    }

    @PostMapping("register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {

        User user = modelMapper.map(registerRequestDto, User.class);
        userService.register(user);

        return ResponseEntity.ok(user);
    }
}
