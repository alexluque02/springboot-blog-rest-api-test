package com.springboot.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
@Sql(value = "classpath:delete-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = "classpath:insert-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class PostControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private MultiValueMap<String, String> headers;

    @BeforeEach
    public void setup(){
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    public void getAllPosts_ReturnsOk(){
        ResponseEntity<PostResponse> response = testRestTemplate.getForEntity("http://localhost:"+port+"/api/posts", PostResponse.class);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(11, Objects.requireNonNull(response.getBody()).getTotalElements());
        assertEquals(1, response.getBody().getContent().get(0).getId());
    }

    @Test
    @Sql("classpath:delete-data.sql")
    public void getAllPosts_ReturnsOkWOResults(){
        ResponseEntity<PostResponse> response = testRestTemplate.getForEntity("http://localhost:"+port+"/api/posts", PostResponse.class);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(0, Objects.requireNonNull(response.getBody()).getTotalElements());
    }

    @Test
    public void getPostByCategory_ReturnsOk(){
        long categoryId = 3;
        ResponseEntity<PostDto[]> response = testRestTemplate.getForEntity("http://localhost:"+port+"/api/posts/category/"+categoryId, PostDto[].class);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(4, Objects.requireNonNull(response.getBody()).length);
    }

    @Test
    public void getPostByCategory_ReturnsNoPosts(){
        long categoryId = 4;
        ResponseEntity<PostDto[]> response = testRestTemplate.getForEntity("http://localhost:"+port+"/api/posts/category/"+categoryId, PostDto[].class);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(0, Objects.requireNonNull(response.getBody()).length);
    }

    @Test
    public void getPostByCategory_ReturnsCategoryNotFound(){
        long categoryId = 7;
        ResponseEntity<PostDto> response = testRestTemplate.getForEntity("http://localhost:"+port+"/api/posts/category/"+categoryId, PostDto.class);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void deletePost_AdminRoleReturnsOk(){
        LoginDto loginDto = new LoginDto("amatushevich4@nifty.com", "zE5#8$x7\"mk>");
        String userToken = jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));
        System.out.println(userToken);
        headers=new LinkedMultiValueMap<>();
        headers.add("content-type","application/json");
        headers.add("Authorization","Bearer "+ userToken);
        ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:"+port+"/api/posts/"+1, HttpMethod.DELETE,new HttpEntity<>("Post entity deleted successfully.", headers), String.class);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void deletePost_UserRoleReturns401(){
        LoginDto loginDto = new LoginDto("sbrane1", "aH5_V1Oar1");
        String userToken = jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));
        System.out.println(userToken);
        headers=new LinkedMultiValueMap<>();
        headers.add("content-type","application/json");
        headers.add("Authorization","Bearer "+ userToken);
        ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:"+port+"/api/posts/"+1, HttpMethod.DELETE,new HttpEntity<>("Post entity deleted successfully.", headers), String.class);
        assertEquals(401, response.getStatusCode().value());
    }

    @Test
    public void deletePost_AnonymousUserReturns401(){
        ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:"+port+"/api/posts/"+1, HttpMethod.DELETE,new HttpEntity<>("Post entity deleted successfully.", headers), String.class);
        assertEquals(401, response.getStatusCode().value());
    }

    @Test
    public void deletePost_ReturnsNotFound(){
        LoginDto loginDto = new LoginDto("amatushevich4@nifty.com", "zE5#8$x7\"mk>");
        String userToken = jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));
        System.out.println(userToken);
        headers=new LinkedMultiValueMap<>();
        headers.add("content-type","application/json");
        headers.add("Authorization","Bearer "+ userToken);
        ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:"+port+"/api/posts/"+21, HttpMethod.DELETE,new HttpEntity<>("Post entity deleted successfully.", headers), String.class);
        assertEquals(404, response.getStatusCode().value());
    }
}
