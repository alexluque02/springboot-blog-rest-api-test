package com.springboot.blog.controller.SecurityContext;

import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {



    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser user) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();



        User usuario =
                new User(1L, user.name(), "Mogollon", "curro@mogollon.com","Curro_123", Set.of(new Role((short) 1,user.role())));
        Authentication auth =
                UsernamePasswordAuthenticationToken.authenticated( usuario,  "password", usuario.getRoles().stream()
                        .map(role -> "ROLE_"+role)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()));
        context.setAuthentication(auth);
        return context;
    }
}
