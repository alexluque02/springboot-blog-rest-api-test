package com.springboot.blog.repository;

import com.springboot.blog.entity.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.springboot.blog.repository.config.ConfigTestClass;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

class RoleRepositoryTest extends ConfigTestClass{

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void findByName() {
        Optional<Role> role = roleRepository.findByName("ROLE_USER");
        Assertions.assertNotNull(role);
        Assertions.assertEquals(role.get().getName(),"ROLE_USER");
    }
}