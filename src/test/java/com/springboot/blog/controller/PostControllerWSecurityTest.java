package com.springboot.blog.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.blog.entity.Category;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerWSecurityTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private PostController postController;

    @MockBean
    private PostService postService;

    @BeforeEach
    public void setup() {

    }


    @Test
    @WithMockUser(username = "username",  roles = {"USER","ADMIN"})
    void updatePost() throws Exception {
        long categoryId = 1L;
        long postId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("categoria");
        category.setDescription("Lorem ipsum dolor sit amet");
        PostDto postDto = new PostDto();
        postDto.setId(postId);
        postDto.setTitle("Publicacion trucha");
        postDto.setDescription("Descripcion trucha");
        postDto.setContent("Contenido de calidad");
        postDto.setCategoryId(category.getId());

        Mockito.when(postService.updatePost(postDto, categoryId)).thenReturn(postDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/posts/{id}", postId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(postDto.getTitle())));

    }


    @Test
    @WithMockUser(username = "username",  roles = {"USER","ADMIN"})
    void deletePostById_thenReturnHttp200() throws Exception {
        Long idPost = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/{id}", idPost)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Post entity deleted successfully."));

        verify(postService, times(1)).deletePostById(idPost);
    }


    @Test
    @WithMockUser(username = "username",  roles = {"USER","ADMIN"})
    void deletePostById_thenReturnHttp404() throws Exception {
        Long idPost = 1L;

        doThrow(new ResourceNotFoundException("Post", "id", idPost)).when(postService).deletePostById(idPost);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/{id}", idPost)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "username",  roles = {"USER"})
    void deletePostById_thenReturnHttp401() throws Exception {
        Long idPost = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/{id}", idPost)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verify(postService, never()).deletePostById(idPost);
    }



}
