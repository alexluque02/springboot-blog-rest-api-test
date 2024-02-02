package com.springboot.blog.controller;

import com.springboot.blog.payload.RegisterDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-tes")
public class AuthControllerIntegration {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Sql("classpath:sql/data.sql")
    @Test
    public void authRegister_thenReturnCreated(){

        RegisterDto  registerDto = new RegisterDto("Roberto","krobert151","robertorebolledo151@gmail.com","lagartoMolon34#");

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:"+port+"/api/auth/register",registerDto, String.class);

        Assertions.assertEquals(201,response.getStatusCode().value());


    }

}
