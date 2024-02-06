package com.springboot.blog.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.security.JwtAuthenticationFilter;
import com.springboot.blog.service.CommentService;
import com.springboot.blog.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(CommentController.class)
@ExtendWith(MockitoExtension.class)
public class PostControllerWSecurityTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PostService postService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private JwtAuthenticationFilter  jwtAuthenticationFilter;

    @InjectMocks
    private PostController postController;

    @Autowired
    private WebApplicationContext webApplicationContext;




    private Long idPost;
    private Long idCategory;

    @BeforeEach
    void setUp(){
        idPost=1L;
        idCategory=1L;
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    }


    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deletePostById_thenReturnHttp200() throws Exception {
        Long idPost = 1L;
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/{id}", idPost)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Post entity deleted successfully."));

        verify(postService, times(1)).deletePostById(idPost);
    }


    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deletePostById_thenReturnHttp404() throws Exception {
        Long idPost = 1L;
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();

        doThrow(new ResourceNotFoundException("Post", "id", idPost)).when(postService).deletePostById(idPost);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/{id}", idPost)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void deletePostById_thenReturnHttp401() throws Exception {
        // Given
        Long idPost = 1L;
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();

        // When and Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/{id}", idPost)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verify(postService, never()).deletePostById(idPost);
    }




}
