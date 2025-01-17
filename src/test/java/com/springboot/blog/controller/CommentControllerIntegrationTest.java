package com.springboot.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
@Sql(value = "classpath:delete-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = "classpath:insert-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CommentControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MultiValueMap<String, String> headers;


    @BeforeEach
    public void setup() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        LoginDto loginDto = new LoginDto("tpetteford0@linkedin.com", "$2a$12$V2STGXRVuoOEqKtAtKZJ3ePwcVAb/GZ7y4NTKhrlZ1MJy6AWiLyXe");
        String userToken = jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));
        System.out.println(userToken);
        headers=new LinkedMultiValueMap<>();
        headers.add("content-type","application/json");
        headers.add("Authorization","Bearer "+ userToken);
    }

    // Roberto Rebolledo Naharro
    @Test
    void getCommentByPostId_thenReturnOk(){

        long postId = 1L;
        ResponseEntity<List<CommentDto>> response = testRestTemplate.exchange(
                "/api/v1/posts/"+postId+"/comments",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CommentDto>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody().get(0).getName(),"Lén");

    }


    //Fernando
    @Test
    void getCommentByIdWith200_OKResponse(){
        long commentId = 1L;
        long postId = 1L;

        CommentDto commentDto = new CommentDto();
        commentDto.setId(commentId);

        String path = "http://localhost:"+port+"/api/v1/posts/"+postId+"/comments/"+commentId;

        ResponseEntity<CommentDto> expectedResponse = testRestTemplate.getForEntity(path, CommentDto.class);

        assertEquals(HttpStatus.OK, expectedResponse.getStatusCode());
        assertEquals(commentDto.getId(), expectedResponse.getBody().getId());

    }

    //Fernando
    @Test
    void getCommentByIdWithNonExistsCommentId404_NotFoundResponse(){
        long commentId = 234L;
        long postId = 1L;

        CommentDto commentDto = new CommentDto();
        commentDto.setId(commentId);

        String path = "http://localhost:"+port+"/api/v1/posts/"+postId+"/comments/"+commentId;

        ResponseEntity<CommentDto> expectedResponse = testRestTemplate.getForEntity(path, CommentDto.class);
    }

    @Test
    void createComment_thenReturnCreated(){
        long postId = 1L;
        long commentId = 1L;
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setName("Paco");
        commentDto.setEmail("paco@gmail.com");
        commentDto.setBody("Lorem ipsum dolor sit amet");

        String path = "http://localhost:" + port + "/api/v1/posts/" + postId + "/comments";

        ResponseEntity<CommentDto> responseEntity = testRestTemplate.exchange(
                path, HttpMethod.POST, new HttpEntity<>(commentDto, headers), CommentDto.class
        );

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Paco", responseEntity.getBody().getName());
        assertEquals(commentId, responseEntity.getBody().getId());
    }

    //Luque
    @Test
    void updateComment_thenReturnOk(){
        long postId = 1;
        long commentId = 2;
        CommentDto commentDto = new CommentDto();
        commentDto.setId(2);
        commentDto.setName("Comentario modificado");
        commentDto.setBody("Cuerpo modificado");
        commentDto.setEmail("tpetteford0@linkedin.com");
        String path = "http://localhost:"+port+"/api/v1/posts/"+postId+"/comments/"+commentId;

        ResponseEntity<CommentDto> response = testRestTemplate.exchange(path,
                HttpMethod.PUT,new HttpEntity<>(commentDto, headers), CommentDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Cuerpo modificado",response.getBody().getBody());
        assertEquals(commentId ,response.getBody().getId());
    }

    //Luque
    @Test
    void updateComment_thenReturnPostIdNotFound(){
        long postId = 71;
        long commentId = 2;
        CommentDto commentDto = new CommentDto();
        commentDto.setId(2);
        commentDto.setName("Comentario modificado");
        commentDto.setBody("Cuerpo modificado");
        commentDto.setEmail("tpetteford0@linkedin.com");
        String path = "http://localhost:"+port+"/api/v1/posts/"+postId+"/comments/"+commentId;

        ResponseEntity<CommentDto> response = testRestTemplate.exchange(path,
                HttpMethod.PUT,new HttpEntity<>(commentDto, headers), CommentDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    //Luque
    @Test
    void updateComment_thenReturnCategoryIdNotFound(){
        long postId = 71;
        long commentId = 90;
        CommentDto commentDto = new CommentDto();
        commentDto.setId(90);
        commentDto.setName("Comentario modificado");
        commentDto.setBody("Cuerpo modificado");
        commentDto.setEmail("tpetteford0@linkedin.com");
        String path = "http://localhost:"+port+"/api/v1/posts/"+postId+"/comments/"+commentId;

        ResponseEntity<CommentDto> response = testRestTemplate.exchange(path,
                HttpMethod.PUT,new HttpEntity<>(commentDto, headers), CommentDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    //Luque
    @Test
    void updateComment_PostsIdDontMatch(){
        long postId = 1;
        long commentId = 5;
        CommentDto commentDto = new CommentDto();
        commentDto.setId(5);
        commentDto.setName("Comentario modificado");
        commentDto.setBody("Cuerpo modificado");
        commentDto.setEmail("tpetteford0@linkedin.com");
        String path = "http://localhost:"+port+"/api/v1/posts/"+postId+"/comments/"+commentId;

        ResponseEntity<CommentDto> response = testRestTemplate.exchange(path,
                HttpMethod.PUT,new HttpEntity<>(commentDto, headers), CommentDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    //Luque
    @Test
    void updateComment_DtoIsWrong(){
        long postId = 1;
        long commentId = 5;
        CommentDto commentDto = new CommentDto();
        String path = "http://localhost:"+port+"/api/v1/posts/"+postId+"/comments/"+commentId;

        ResponseEntity<CommentDto> response = testRestTemplate.exchange(path,
                HttpMethod.PUT,new HttpEntity<>(commentDto, headers), CommentDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    //Luque
    @Test
    void updateComment_NoDto(){
        long postId = 1;
        long commentId = 5;
        String path = "http://localhost:"+port+"/api/v1/posts/"+postId+"/comments/"+commentId;

        ResponseEntity<CommentDto> response = testRestTemplate.exchange(path,
                HttpMethod.PUT,new HttpEntity<>(null, headers), CommentDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(0, response.getBody().getId());
    }

    //Roberto Rebolledo Naharro
    @Test
    void deleteComment_Ok(){
        long postId = 1;
        long commentId = 1;



        String path = "http://localhost:"+port+"/api/v1/posts/"+postId+"/comments/"+commentId;

        ResponseEntity<String> response = testRestTemplate.exchange(
                path,HttpMethod.DELETE,new HttpEntity<>(null, headers),String.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Comment deleted successfully",response.getBody());


    }

    //Roberto Rebolledo Naharro
    @Test
    void deleteComment_NotFound(){
        long postId = 10000;
        long commentId = 1000;



        String path = "http://localhost:"+port+"/api/v1/posts/"+postId+"/comments/"+commentId;

        ResponseEntity<String> response = testRestTemplate.exchange(
                path,HttpMethod.DELETE,new HttpEntity<>(null, headers),String.class);

        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    //Roberto Rebolledo Naharro
    @Test
    void deleteComment_BadRequest(){
        long postId = 5;
        long commentId = 1;



        String path = "http://localhost:"+port+"/api/v1/posts/"+postId+"/comments/"+commentId;

        ResponseEntity<String> response = testRestTemplate.exchange(
                path,HttpMethod.DELETE,new HttpEntity<>(null, headers),String.class);

        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }

}
