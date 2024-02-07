package com.springboot.blog.repository;

import com.springboot.blog.entity.Comment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.springboot.blog.repository.config.ConfigTestClass;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class CommentRepositoryTest extends ConfigTestClass{

    @Autowired
    CommentRepository commentRepository;

    @Test
    void findByPostId_CommentsExist() {
        List<Comment> comments = commentRepository.findByPostId(9);
        assertNotNull(comments);
        assertEquals(3, comments.size());
        assertEquals("In blandit ultrices enim.", comments.get(0).getBody());
    }

    @Test
    void findByPostId_NoComments(){
        List<Comment> comments = commentRepository.findByPostId(11);
        assertTrue(comments.isEmpty());
    }

    @Test
    void findByPostId_PostIdDontExist(){
        List<Comment> comments = commentRepository.findByPostId(99);
        assertTrue(comments.isEmpty());
    }

}