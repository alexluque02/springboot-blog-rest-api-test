package com.springboot.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.security.JwtTokenProvider;
import com.springboot.blog.service.PostService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc(addFilters = false)
class PostControllerWOSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @InjectMocks
    private PostController postController;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void createPost() {
    }

    //Luque
    @Test
    void getAllPosts_WithResults() throws Exception {
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "title";
        String sortDir = "ASC";
        Post post1 = new Post();
        post1.setId(1L);
        post1.setTitle("Alex");
        Post post2 = new Post();
        post2.setTitle("Pepe");
        post2.setId(1L);
        List<Post> posts = List.of(post1, post2);
        ModelMapper modelMapper = new ModelMapper();
        List<PostDto> postDtoList = posts.stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .toList();

        PostResponse postResponse = new PostResponse(postDtoList, 0 , 1, 2, 1, true);
        Mockito.when(postService.getAllPosts(pageNo, pageSize, sortBy, sortDir)).thenReturn(postResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts")
                .param("pageNo", String.valueOf(pageNo))
                .param("pageSize", String.valueOf(pageSize))
                .param("sortBy", sortBy)
                .param("sortDir", sortDir)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));

    }

    @Test
    void getAllPosts_NoResults() throws Exception {
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "title";
        String sortDir = "ASC";

        List<PostDto> postDto = List.of();
        PostResponse postResponse = new PostResponse(postDto, 0 , 1, 2, 1, true);
        Mockito.when(postService.getAllPosts(pageNo, pageSize, sortBy, sortDir)).thenReturn(postResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts")
                        .param("pageNo", String.valueOf(pageNo))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("sortBy", sortBy)
                        .param("sortDir", sortDir)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));

    }

    @Test
    void getPostByIdWithStatusCode200_OK() throws Exception{
        ModelMapper modelMapper = new ModelMapper();
        long postId = 1L;
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Title");
        PostDto postDto = modelMapper.map(post, PostDto.class);

        Mockito.when(postService.getPostById(postId)).thenReturn(postDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/{id}", postDto.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void getPostByIdWithIdNullOrNotExistsThrowException() throws Exception {
        long postId = 2L;

        Mockito.when(postService.getPostById(postId)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts/{id}", postId))
                .andExpect(status().isNotFound());
    }


    //Luque
    @Test
    void getPostsByCategory_Response200() throws Exception {
        Category category = new Category();
        category.setId(1L);

        Post post1 = new Post();
        post1.setId(1L);
        post1.setCategory(category);

        Post post2 = new Post();
        post2.setId(2L);
        post2.setCategory(category);

        List <Post> posts = List.of(post1, post2);

        PostDto postDto1 = new PostDto();
        postDto1.setId(post1.getId());
        postDto1.setCategoryId(post1.getCategory().getId());

        PostDto postDto2 = new PostDto();
        postDto2.setId(post2.getId());
        postDto2.setCategoryId(post2.getCategory().getId());

        ModelMapper modelMapper = new ModelMapper();
        List<PostDto> postDtoList = posts.stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .toList();

        Mockito.when(postService.getPostsByCategory(category.getId())).thenReturn(postDtoList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/category/{id}", category.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    //Luque
    @Test
    void getPostsByCategory_CategoryNotFound() throws Exception {
        Post post1 = new Post();
        post1.setId(1L);

        Post post2 = new Post();
        post2.setId(2L);

        List <Post> posts = List.of(post1, post2);

        PostDto postDto1 = new PostDto();
        postDto1.setId(post1.getId());

        PostDto postDto2 = new PostDto();
        postDto2.setId(post2.getId());

        ModelMapper modelMapper = new ModelMapper();
        List<PostDto> postDtoList = posts.stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .toList();

        Mockito.when(postService.getPostsByCategory(1L)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/category/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}