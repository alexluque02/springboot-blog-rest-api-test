package com.springboot.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void updatePost() {
    }


    //Roberto Rebolledo Naharro
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


    //Roberto Rebolledo Naharro
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

    //Roberto Rebolledo Naharro
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
