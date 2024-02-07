package com.springboot.blog.repository;

import com.springboot.blog.entity.Post;
import com.springboot.blog.repository.config.ConfigTestClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class PostRepositoryTest extends ConfigTestClass {

    @Autowired
    private PostRepository postRepository;

    // Roberto Rebolledo Naharro
    @Test
    void findByCategoryId() {

        List<Post> post = postRepository.findByCategoryId(6L);

        Assertions.assertNotNull(post);
        Assertions.assertEquals(post.get(0).getTitle(),"Monster Walks, The");
        Assertions.assertEquals(post.size(),3);

    }
}