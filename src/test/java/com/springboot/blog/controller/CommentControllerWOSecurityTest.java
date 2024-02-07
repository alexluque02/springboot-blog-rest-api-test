package com.springboot.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.blog.entity.Comment;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.security.JwtTokenProvider;
import com.springboot.blog.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CommentControllerWOSecurityTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentRepository commentRepository;

    @MockBean
    private CommentServiceImpl commentService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private WebApplicationContext webApplicationContext;


    @Test
    public void deleteComment_ValidComment_ReturnsOk() throws Exception {
        Long postId = 1L;
        Long commentId = 2L;
        Mockito.doNothing().when(commentRepository).delete(Mockito.any(Comment.class));

        doNothing().when(commentService).deleteComment(postId, commentId);



        mockMvc.perform(delete("/api/v1/posts/{postId}/comments/{id}", postId, commentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Comment deleted successfully"));

        verify(commentService, times(1)).deleteComment(postId, commentId);
    }

    @Test
    public void deleteComment_ValidComment_ReturnsNotFound() throws Exception {
        Long postId = 1L;
        Long commentId = 2L;
        Mockito.doNothing().when(commentRepository).delete(Mockito.any(Comment.class));

        Mockito.doThrow(new ResourceNotFoundException("Post", "id", postId)).when(commentService).deleteComment(postId, commentId);


        mockMvc.perform(delete("/api/v1/posts/{postId}/comments/{id}", postId, commentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

}
