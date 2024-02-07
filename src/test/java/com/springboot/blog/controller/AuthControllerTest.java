package com.springboot.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.exception.InternalServerErrorException;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.GlobalExceptionHandler;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.RegisterDto;
import com.springboot.blog.security.JwtTokenProvider;
import com.springboot.blog.service.AuthService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthController authController;


    @Test
    public void login_successful() throws Exception {

        String token = "generated-token";

        when(authService.login(any(LoginDto.class))).thenReturn(token);

        LoginDto loginDto = new LoginDto("username", "password");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("generated-token"));
    }

    @Test
    public void login_badRequest() throws Exception {

        when(authService.login(any(LoginDto.class))).thenReturn(null);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void login_internalServerError() throws Exception {

        LoginDto loginDto = new LoginDto("username", "password");

        Mockito.when(authService.login(Mockito.any(LoginDto.class))).thenThrow(InternalServerErrorException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isInternalServerError());
                //.andExpect(jsonPath("$.message").value("Credenciales erróneas"));
    }

    @Test
    void register_whenValidInput_thenReturns201()throws Exception{

        RegisterDto registerDto = new RegisterDto("manolo","manolo","manolo.manolo@gmail.com","manolo1234");

        mockMvc.perform(post("/api/auth/register")
                .content(objectMapper.writeValueAsString(registerDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}