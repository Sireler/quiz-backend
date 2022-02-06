package com.sireler.quiz.controller;

import com.sireler.quiz.dto.LoginRequestDto;
import com.sireler.quiz.dto.RegisterRequestDto;
import com.sireler.quiz.model.User;
import com.sireler.quiz.security.JwtTokenProvider;
import com.sireler.quiz.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
    public String login(@RequestBody LoginRequestDto loginRequestDto) {
        return "login";
    }

    @PostMapping("register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {

        User user = modelMapper.map(registerRequestDto, User.class);
        userService.register(user);

        return ResponseEntity.ok(user);
    }
}
