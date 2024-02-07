package com.springboot.blog.service;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.impl.PostServiceImpl;
import org.checkerframework.checker.units.qual.C;
import org.checkerframework.checker.units.qual.N;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    PostRepository postRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    PostServiceImpl postService;

    @Test
    void createPost() {
    }

    @Test
    void getAllPosts_OrderAsc() {
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "title";
        String sortDir = "ASC";

        Sort sort = Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Post post1 = new Post();
        post1.setId(1L);
        post1.setTitle("Alex");

        Post post2 = new Post();
        post2.setId(2L);
        post2.setTitle("Pepe");

        List<Post> postList = List.of(post2, post1);
        Page<Post> postPage = new PageImpl<>(postList);

        Mockito.when(postRepository.findAll(Mockito.eq(pageable))).thenReturn(postPage);

        PostDto postDto = new PostDto();
        postDto.setId(post2.getId());
        postDto.setTitle(post2.getTitle());

        PostDto postDto2 = new PostDto();
        postDto2.setId(post1.getId());
        postDto2.setTitle(post1.getTitle());

        List<PostDto> content= List.of(postDto, postDto2);
        Mockito.when(modelMapper.map(Mockito.any(), Mockito.eq(PostDto.class)))
                .thenReturn(content.get(0))
                .thenReturn(content.get(1));

        PostResponse result = postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);

        assertEquals(1 , result.getTotalPages());
        assertEquals(2 , result.getTotalElements());
        assertEquals(2L, result.getContent().get(0).getId());
    }

    @Test
    void getAllPosts_OrderDesc() {
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "title";
        String sortDir = "DESC";

        Sort sort = Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Post post1 = new Post();
        post1.setId(1L);
        post1.setTitle("Alex");

        Post post2 = new Post();
        post2.setId(2L);
        post2.setTitle("Pepe");

        List<Post> postList = List.of(post1, post2);
        Page<Post> postPage = new PageImpl<>(postList);

        Mockito.when(postRepository.findAll(Mockito.eq(pageable))).thenReturn(postPage);

        PostDto postDto = new PostDto();
        postDto.setId(post1.getId());
        postDto.setTitle(post1.getTitle());

        PostDto postDto2 = new PostDto();
        postDto2.setId(post2.getId());
        postDto2.setTitle(post2.getTitle());

        List<PostDto> content= List.of(postDto, postDto2);
        Mockito.when(modelMapper.map(Mockito.any(), Mockito.eq(PostDto.class)))
                .thenReturn(content.get(0))
                .thenReturn(content.get(1));

        PostResponse result = postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);

        assertEquals(1 , result.getTotalPages());
        assertEquals(2 , result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).getId());
    }

    @Test
    void getPostByIdWithSuccess() {

        Category category = new Category(1L, "Category 1", null, null);

        Post post = new Post(1L, "Post1", "Description", null, null, null);

        PostDto postDto = new PostDto();

        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());

        Mockito.when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        Mockito.when(modelMapper.map(post, PostDto.class)).thenReturn(postDto);

        PostDto expectedResult = postService.getPostById(post.getId());

        assertEquals(postDto.getId(), expectedResult.getId());
        assertEquals(postDto.getTitle(), expectedResult.getTitle());
    }

    @Test
    void getPostByIdWithIdNullOrNotExistsThrowException(){

        Post post = new Post();
        post.setId(1L);

        assertThrows(Exception.class, () -> postService.getPostById(2L));
    }

    @Test
    void updatePost() {
    }

    @Test
    void deletePostById() {
    }

    @Test
    void getPostsByCategory_CategoryNotFound() {
        Long categoryId = 1L;
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> postService.getPostsByCategory(categoryId));
    }

    @Test
    void getPostsByCategory_ReturnsEmptyPostsList(){
        Category category = new Category();
        category.setId(1L);

        Mockito.when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        Mockito.when(postRepository.findByCategoryId(category.getId())).thenReturn(Collections.emptyList());

        List<PostDto> result = postService.getPostsByCategory(category.getId());
        assertTrue(result.isEmpty());
    }

    @Test
    void getPostsByCategory_ReturnsPostsList(){
        Category category = new Category();
        category.setId(1L);

        Post post1 = new Post();
        post1.setId(1L);
        post1.setCategory(category);

        Post post2 = new Post();
        post2.setId(2L);
        post2.setCategory(category);

        List <Post> posts = List.of(post1, post2);

        Mockito.when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        Mockito.when(postRepository.findByCategoryId(category.getId())).thenReturn(posts);

        PostDto postDto1 = new PostDto();
        postDto1.setId(post1.getId());
        postDto1.setCategoryId(post1.getCategory().getId());

        PostDto postDto2 = new PostDto();
        postDto2.setId(post2.getId());
        postDto2.setCategoryId(post2.getCategory().getId());

        List<PostDto> postDtoList = List.of(postDto1, postDto2);
        Mockito.when(modelMapper.map(Mockito.any(Post.class), Mockito.eq(PostDto.class)))
                .thenReturn(postDtoList.get(0))
                .thenReturn(postDtoList.get(1));

        List<PostDto> result = postService.getPostsByCategory(category.getId());
        System.out.println(result.size());
        System.out.println(result.get(0));
        System.out.println(result.get(1));
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(category.getId(), result.get(0).getCategoryId());
    }
}