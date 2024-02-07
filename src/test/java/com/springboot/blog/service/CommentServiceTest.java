package com.springboot.blog.service;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void createComment() {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setName("Paco");
        commentDto.setEmail("paco@gmail.com");
        commentDto.setBody("Lorem ipsum dolor sit amet");

        Post post = new Post(
                1L,
                "Title",
                "Lorem ipsum dolor sit amet",
                "asdfasdf",
                new HashSet<>(),
                new Category()
        );

        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setBody(commentDto.getBody());
        comment.setEmail(commentDto.getEmail());
        comment.setPost(post);

        when(modelMapper.map(commentDto, Comment.class)).thenReturn(comment);
        when(postRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(post));
        when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);

        CommentDto result = commentService.createComment(post.getId(), commentDto);

        assertEquals("Paco", result.getName());
        verify(commentRepository, Mockito.times(1)).save(Mockito.any(Comment.class));
        verify(postRepository).findById(eq(1L));
    }

    @Test
    void getCommentsByPostId() {

        long postId= 1L;

        Category cat1 = new Category(1L,"Categoria1","description1", new ArrayList<>());

        Post post1 = new Post(postId, "post1","description1","content1",new HashSet<>(),cat1);

        List<Comment> listComment = new ArrayList<>(
                List.of(
                        new Comment(1L,"comentario1","comentario1@gmail.com","texto de comentario 1",post1),
                        new Comment(2L,"comentario2","comentario2@gmail.com","texto de comentario 2",post1),
                        new Comment(3L,"comentario3","comentario3@gmail.com","texto de comentario 3",post1),
                        new Comment(4L,"comentario4","comentario4@gmail.com","texto de comentario 4",post1)
                        )
        );


        when(commentRepository.findByPostId(postId)).thenReturn(listComment);

        Assertions.assertEquals(commentService.getCommentsByPostId(postId).size(),4);
    }

    @Test
    void getCommentByIdWithSuccess() {

        Long postId = 1L;
        Long commentId = 1L;

        Post post = new Post(postId, null, null, null, null, null);
        Comment comment = new Comment(commentId, null, null, null, post);
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setBody("Comment");

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(modelMapper.map(comment, CommentDto.class)).thenReturn(commentDto);

        CommentDto expectedResult = commentService.getCommentById(postId, commentId);

        assertEquals(commentDto.getId(), expectedResult.getId());
        assertEquals(commentDto.getBody(), expectedResult.getBody());
    }

    @Test
    void getCommentByIdWithEmptyContentThrowException(){
        Long postId = null;
        Long commentId = null;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> commentService.getCommentById(postId, commentId));
    }

    @Test
    void getCommentByIdWithAnInvalidCommentIdThrowException(){
        Long postId = 1L;
        Long commentId = null;

        assertThrows(Exception.class, () -> commentService.getCommentById(postId, commentId));
    }

    @Test
    void getCommentByIdWithAValidCommentIdButInvalidPostIdThrowException(){
        Long postId = null;
        Long commentId = 1L;

        when(postRepository.findById(postId)).thenReturn(Optional.of(new Post()));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(new Comment()));

        assertThrows(Exception.class, () -> commentService.getCommentById(postId, commentId));
    }

    @Test
    void updateComment_PostNotFound() {
        Long postId = 1L;
        long commentId = 2L;
        CommentDto commentRequest = new CommentDto();

        when(postRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.updateComment(postId, commentId, commentRequest));
    }

    @Test
    void updateComment_CommentNotFound(){
        Post post = new Post();
        post.setId(1L);
        long commentId = 2L;
        CommentDto commentRequest = new CommentDto();

        when(postRepository.findById(Mockito.any())).thenReturn(Optional.of(post));
        when(commentRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> commentService.updateComment(post.getId(), commentId, commentRequest));
    }

    @Test
    void updateComment_CommentPostIdDifferent(){
        Post post = new Post();
        post.setId(1L);
        Post post2 = new Post();
        post.setId(2L);
        Comment comment = new Comment();
        comment.setId(1);
        comment.setPost(post);
        CommentDto commentRequest = new CommentDto();

        when(postRepository.findById(Mockito.any())).thenReturn(Optional.of(post2));
        when(commentRepository.findById(Mockito.any())).thenReturn(Optional.of(comment));

        assertThrows(BlogAPIException.class, () -> commentService.updateComment(post2.getId(), comment.getId(), commentRequest));
    }

    @Test
    void updateComment_Successful(){
        Long postId = 1L;
        long commentId = 1L;
        Post post = new Post();
        post.setId(1L);
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setPost(post);
        CommentDto commentRequest = new CommentDto();
        commentRequest.setBody("Esto es un comentario");
        commentRequest.setName("Comentario 1");
        commentRequest.setEmail("alex@gmail.com");

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(modelMapper.map(Mockito.any(), eq(CommentDto.class))).thenReturn(commentRequest);

        CommentDto updatedComment = commentService.updateComment(postId, commentId, commentRequest);

        assertEquals(commentRequest.getName(), updatedComment.getName());
        assertEquals(commentRequest.getEmail(), updatedComment.getEmail());
        assertEquals(commentRequest.getBody(), updatedComment.getBody());

    }

    @Test
    void deleteComment_PostNotFound() {

        Long postId = 1L;

        when(postRepository.findById(postId)).thenThrow(new ResourceNotFoundException("Post", "id", postId));

        assertThrows(ResourceNotFoundException.class, ()-> commentService.deleteComment(postId,postId));
        verify(commentRepository, never()).delete(any(Comment.class));

    }

    @Test
    void deleteComment_CommentNotFound() {
        Long commentId = 1L;

        lenient().when(commentRepository.findById(commentId))
                .thenThrow(new ResourceNotFoundException("Comment", "id", commentId));

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.deleteComment(commentId, commentId));
        verify(commentRepository, never()).delete(any(Comment.class));

    }

    @Test
    public void deleteComment_CommentDoesNotBelongToPost_ThrowException() {


        Post post1 = new Post(1L, "post1", "post1desc","post1content",new HashSet<>(),new Category());
        Post post2 = new Post(2L, "post2", "post2desc","post2content",new HashSet<>(),new Category());

        Comment comment1 = new Comment(1L,"comment1","comment1@comment1","comment1body",post1);

        when(postRepository.findById(post2.getId())).thenReturn(Optional.of(post2));


        when(commentRepository.findById(comment1.getId())).thenReturn(Optional.of(comment1));

        assertThrows(BlogAPIException.class, () -> commentService.deleteComment(post2.getId(), comment1.getId()));

        verify(commentRepository, never()).delete(any(Comment.class));
    }

    @Test
    public void deleteComment_ValidComment_DeletesComment() {

        Post post1 = new Post(1L, "post1", "post1desc","post1content",new HashSet<>(),new Category());

        Comment comment1 = new Comment(1L,"comment1","comment1@comment1","comment1body",post1);

        when(postRepository.findById(post1.getId())).thenReturn(Optional.of(post1));

        when(commentRepository.findById(comment1.getId())).thenReturn(Optional.of(comment1));
        commentService.deleteComment(post1.getId(), comment1.getId());

        verify(commentRepository, times(1)).delete(comment1);
    }
}