package com.sireler.quiz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sireler.quiz.dto.LoginRequestDto;
import com.sireler.quiz.dto.RegisterRequestDto;
import com.sireler.quiz.model.User;
import com.sireler.quiz.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Test
    void whenGivenValidUsernameAndPassword_thenShouldRegister() throws Exception {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setUsername("authTestUsername");
        registerRequestDto.setEmail("authTestEmail@test");
        registerRequestDto.setPassword("secret");

        mockMvc
                .perform(
                        post("/api/v1/auth/register")
                                .content(objectMapper.writeValueAsString(registerRequestDto))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("Successfully registered")));
    }

    @Test
    void whenUsernameExists_thenShouldReturnMessage() throws Exception {
        User user = new User();
        user.setUsername("usernameExists");
        user.setPassword("secret");
        user.setEmail("usernameExists@test");
        userService.register(user);

        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setUsername(user.getUsername());
        registerRequestDto.setEmail("authUsernameExists@test");
        registerRequestDto.setPassword("secret");

        mockMvc
                .perform(
                        post("/api/v1/auth/register")
                                .content(objectMapper.writeValueAsString(registerRequestDto))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.message", is("Username is already taken")));
    }

    @Test
    void whenEmailExists_thenShouldReturnMessage() throws Exception {
        User user = new User();
        user.setUsername("emailExists");
        user.setPassword("secret");
        user.setEmail("emailExists@test");
        userService.register(user);

        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setUsername("emailExistsUsername");
        registerRequestDto.setEmail(user.getEmail());
        registerRequestDto.setPassword("secret");

        mockMvc
                .perform(
                        post("/api/v1/auth/register")
                                .content(objectMapper.writeValueAsString(registerRequestDto))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.message", is("Email is already taken")));
    }

    @Test
    void whenUsernameAndPasswordValid_thenShouldReturnToken() throws Exception {
        User user = new User();
        user.setUsername("testLogin");
        user.setPassword("secret");
        user.setEmail("testLogin@test");
        userService.register(user);

        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsername(user.getUsername());
        loginRequestDto.setPassword("secret");

        mockMvc
                .perform(
                        post("/api/v1/auth/login")
                                .content(objectMapper.writeValueAsString(loginRequestDto))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void whenPasswordInvalid_thenShouldReturnMessage() throws Exception {
        User user = new User();
        user.setUsername("testLoginBadPass");
        user.setPassword("secret");
        user.setEmail("testLoginBadPass@test");
        userService.register(user);

        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsername(user.getUsername());
        loginRequestDto.setPassword("pass");

        mockMvc
                .perform(
                        post("/api/v1/auth/login")
                                .content(objectMapper.writeValueAsString(loginRequestDto))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.message", is("Invalid username or password.")));
    }
}
