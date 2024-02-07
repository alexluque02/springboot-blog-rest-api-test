package com.springboot.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;


import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
@Sql(value = "classpath:insert-data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:delete-data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CategoryControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtTokenProvider jwtProvider;

    String adminToken;
    String userToken;

    HttpHeaders header = new HttpHeaders();
    CategoryDto dto = new CategoryDto();

    @BeforeEach
    @Sql("classpath:delete-data.sql")
    public void setup() {
        
        Collection<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        Authentication auth = new UsernamePasswordAuthenticationToken("krobert151","tiburonMolon123",authorities);
        adminToken = jwtProvider.generateToken(auth);

        Collection<GrantedAuthority> authorities2 = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        Authentication auth2 = new UsernamePasswordAuthenticationToken("krobert152","tiburonMolon123", authorities2);
        userToken = jwtProvider.generateToken(auth2);

    }

    @Test
    public void getCategory_thenReturnOk() throws Exception{
        long categoryId = 1L;
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<CategoryDto> categoryDtoResponseEntity = restTemplate.getForEntity("http://localhost:"+port+"/api/v1/categories/"+categoryId, CategoryDto.class);
        assertEquals(200, categoryDtoResponseEntity.getStatusCode().value());
        assertEquals("Teal", Objects.requireNonNull(categoryDtoResponseEntity.getBody()).getName());
    }

    @Test
    public void getCategory_thenNotFound() throws Exception {
        long categoryId = 7L;
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<CategoryDto> categoryDtoResponseEntity = restTemplate.getForEntity("http://localhost:" + port + "/api/v1/categories/" + categoryId, CategoryDto.class);
        assertEquals(404, categoryDtoResponseEntity.getStatusCode().value());
    }

    @Test
    void updateCategory_thenReturnOk(){
        header.setContentType(MediaType.APPLICATION_JSON);
        header.setBearerAuth(adminToken);

        dto.setName("Manolo32");
        dto.setDescription("Manolo32");

        Long categoryId = 1L;

        HttpEntity<CategoryDto> objectRequest = new HttpEntity<>(dto, header);
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        ResponseEntity<CategoryDto> response = testRestTemplate.exchange("http://localhost:" + port + "/api/v1/categories/{id}",
                HttpMethod.PUT, objectRequest, CategoryDto.class, categoryId);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Manolo32", response.getBody().getName());
        assertEquals("Manolo32", response.getBody().getDescription());
    }

    @Test
    void updateCategory_thenReturnUnauthorized(){
        header.setContentType(MediaType.APPLICATION_JSON);
        header.setBearerAuth(userToken);

        dto.setName("Manolo32");
        dto.setDescription("Manolo32");

        Long categoryId = 1L;

        HttpEntity<CategoryDto> objectRequest = new HttpEntity<>(dto, header);
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        ResponseEntity<CategoryDto> response = testRestTemplate.exchange("http://localhost:" + port + "/api/v1/categories/{id}",
                HttpMethod.PUT, objectRequest, CategoryDto.class, categoryId);


        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    }

    @Test
    void updateCategory_thenReturnUnauthorized2(){
        header.setContentType(MediaType.APPLICATION_JSON);

        dto.setName("Manolo32");
        dto.setDescription("Manolo32");

        Long categoryId = 1L;

        HttpEntity<CategoryDto> objectRequest = new HttpEntity<>(dto, header);
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        ResponseEntity<CategoryDto> response = testRestTemplate.exchange("http://localhost:" + port + "/api/v1/categories/{id}",
                HttpMethod.PUT, objectRequest, CategoryDto.class, categoryId);


        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    }

    @Test
    void updateCategory_thenReturnNotFound(){
        header.setContentType(MediaType.APPLICATION_JSON);
        header.setBearerAuth(adminToken);

        dto.setName("Manolo32");
        dto.setDescription("Manolo32");

        Long categoryId = 40L;

        HttpEntity<CategoryDto> objectRequest = new HttpEntity<>(dto, header);
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        ResponseEntity<CategoryDto> response = testRestTemplate.exchange("http://localhost:" + port + "/api/v1/categories/{id}",
                HttpMethod.PUT, objectRequest, CategoryDto.class, categoryId);


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    // Fernando
    @Test
    void deleteCategoryWithStatusCode200_OK(){
        header.setContentType(MediaType.APPLICATION_JSON);
        header.setBearerAuth(adminToken);
        HttpEntity<CategoryDto> objectRequest = new HttpEntity<>(null, header);
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        long categoryId = 1L;

        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/v1/categories/"+categoryId, HttpMethod.DELETE, objectRequest, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // Fernando
    @Test
    void deleteCategoryWithNotFoundIdReturnStatusCode404_NotFound(){
        header.setContentType(MediaType.APPLICATION_JSON);
        header.setBearerAuth(adminToken);
        HttpEntity<CategoryDto> objectRequest = new HttpEntity<>(null, header);
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        long categoryId = 123L;

        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/v1/categories/"+categoryId, HttpMethod.DELETE, objectRequest, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    
}
