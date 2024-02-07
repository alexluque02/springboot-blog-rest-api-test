package com.springboot.blog.repository;

import com.springboot.blog.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.springboot.blog.repository.config.ConfigTestClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest extends ConfigTestClass {

    @Autowired
    UserRepository userRepository;

    @Test
    void findByEmailNotFound() {
        Optional<User> userOptional = userRepository.findByEmail("fakeEmail@gmail.com");

        assertTrue(userOptional.isEmpty());
    }

    @Test
    void findByUsernameOrEmail_UsernameExists() {
        Optional<User> user = userRepository.findByUsernameOrEmail("sbrane1", "");
        assertTrue(user.isPresent());
        assertEquals("Silva", user.get().getName());
        assertEquals(2L, user.get().getId());
    }

    @Test
    void findByUsernameOrEmail_EmailExists() {
        Optional<User> user = userRepository.findByUsernameOrEmail("", "tpetteford0@linkedin.com");
        assertTrue(user.isPresent());
        assertEquals("Tomi", user.get().getName());
        assertEquals(1L, user.get().getId());
    }

    @Test
    void findByUsernameOrEmail_TwoResults() {
        assertThrows(IncorrectResultSizeDataAccessException.class, () -> {
            userRepository.findByUsernameOrEmail("sbrane1", "tpetteford0@linkedin.com");
        });
    }

    @Test
    void findByUsernameOrEmail_BothExist() {
        Optional<User> user = userRepository.findByUsernameOrEmail("jjosuweit2", "jdelisle2@mysql.com");
        assertTrue(user.isPresent());
        assertEquals("Janene", user.get().getName());
        assertEquals(3L, user.get().getId());
    }

    @Test
    void findByUsernameOrEmail_BothDontExist() {
        Optional<User> user = userRepository.findByUsernameOrEmail("pepito", "pepito@gmail.com");
        assertFalse(user.isPresent());
    }

    @Test
    void findByUsername() {
        Optional<User> user = userRepository.findByUsername("tvenneur0");
        Assertions.assertNotNull(user);
        Assertions.assertEquals(user.get().getUsername(), "tvenneur0");

    }

    @Test
    void existsByUsernameFound() {
        boolean usuarioEncontrado = userRepository.existsByUsername("tvenneur0");
        assertTrue(usuarioEncontrado);
    }

    @Test
    void existsByUsernameNotFound() {
        boolean usuarioEncontrado = userRepository.existsByUsername("fake_user");
        assertFalse(usuarioEncontrado);
    }

    @Test
    void existsByEmailFound() {
        boolean usuarioEncontrado = userRepository.existsByEmail("tpetteford0@linkedin.com");

        assertTrue(usuarioEncontrado);
    }

    @Test
    void existsByEmailNotFound() {
        boolean usuarioEncontrado = userRepository.existsByEmail("fakeEmail@gmail.com");

        assertFalse(usuarioEncontrado);
    }

}