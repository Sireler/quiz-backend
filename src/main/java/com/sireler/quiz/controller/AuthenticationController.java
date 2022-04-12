package com.sireler.quiz.controller;

import com.sireler.quiz.dto.ApiResponse;
import com.sireler.quiz.dto.LoginRequestDto;
import com.sireler.quiz.dto.LoginResponseDto;
import com.sireler.quiz.dto.RegisterRequestDto;
import com.sireler.quiz.exception.ApiException;
import com.sireler.quiz.model.User;
import com.sireler.quiz.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthenticationController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    private static final String INVALID_USERNAME_OR_PASSWORD_MESSAGE = "Invalid username or password.";

    public AuthenticationController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        try {
            String username = loginRequestDto.getUsername();
            String password = loginRequestDto.getPassword();

            String token = userService.getToken(username, password);
            LoginResponseDto loginResponseDto = new LoginResponseDto(token);

            return ResponseEntity.ok(loginResponseDto);
        } catch (AuthenticationException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, INVALID_USERNAME_OR_PASSWORD_MESSAGE);
        }
    }

    @PostMapping("register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        User user = modelMapper.map(registerRequestDto, User.class);
        userService.register(user);

        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Successfully registered"));
    }
}
