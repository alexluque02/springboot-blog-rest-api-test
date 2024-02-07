package com.springboot.blog.service;

import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.security.JwtTokenProvider;
import com.springboot.blog.service.impl.AuthServiceImpl;
import com.springboot.blog.entity.Role;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.payload.RegisterDto;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    //Luque
    @Test
    void login_ValidCredentials_ReturnToken() {
        LoginDto loginDto = new LoginDto("username", "password");
        String expectedToken = "generated-token";
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword());

        Mockito.when(authenticationManager.authenticate(Mockito.any()))
                .thenReturn(authentication);

        Mockito.when(jwtTokenProvider.generateToken(Mockito.any()))
                .thenReturn(expectedToken);

        String actualToken = authService.login(loginDto);

        assertEquals(expectedToken, actualToken);
    }

    //Luque
    @Test
    void login_InvalidCredentials_ThrowsException() {
        LoginDto loginDto = new LoginDto("invalidUsername", "invalidPassword");

        Mockito.when(authenticationManager.authenticate(Mockito.any()))
                .thenThrow(new AuthenticationServiceException("Invalid credentials"));

        assertThrows(AuthenticationServiceException.class, () -> authService.login(loginDto));
    }

    //Luque
    @Test
    void register_usernameExistsEx() {

        RegisterDto registerDto = new RegisterDto("Manolo","Manolo","manolo.manolo@gmail.com","Manolo1234");


        Mockito.when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(true);


        BlogAPIException exception = assertThrows(BlogAPIException.class, () -> {
            authService.register(registerDto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Username is already exists!.", exception.getMessage());
    }

    //Luque
    @Test
    void register_emailExistsEx() {

        RegisterDto registerDto = new RegisterDto("Manolo","Manolo","manolo.manolo@gmail.com","Manolo1234");


        Mockito.when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(true);


        BlogAPIException exception = assertThrows(BlogAPIException.class, () -> {
            authService.register(registerDto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Email is already exists!.", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "Eleanora,ecowhig0,echallener0@telegraph.co.uk,kL5X02_bN9,$2a$04$NraFEjLNGQ4akdErdspSX.IyU3PekDBH.0y6e0.wyJ07.w7tLXsWC",
            "Karna,kblackley1,kbiggins1@desdev.cn,fK9_H>L9W%#}=,$2a$04$a0zmr9cSVNLCTX07pBL0A.KE6JP.qHkuPhxS96mh16XCkjqCDE.oG",
            "Wildon,wtortoishell2,wsabate2@themeforest.net,yO6*f#nfai(aYwwS,$2a$04$hoZr5q28lQ.44zWQ7rDYeui0qgTdooSB49f6GBazdkZhUqSF5SPq.",
            "Court,cwrightson3,cgregg3@amazon.com,hC1+,R@e,$2a$04$FHVmqWb34BdhHji/dKBNC.nw17GexlutIyK6Wc1rgoaqdjDRFL9LC",
            "Allie,aepelett4,alockyer4@ucoz.ru,aF5`aWj|Hse,$2a$04$YHPv7HP0Mchr42ifVHThvOW8l7iKA7Ic7hbHyP4q/Np3z7aIbF.b6",
            "James,jdrescher5,jbrackenbury5@newsvine.com,,$2a$04$DkClk17SLn6R6FdTOivDcuKd8SdjXTnWkacuhgOx6f.Kjt90WEWr6",
            "Livvy,lmockett6,,sM6h1CNrsv%,$2a$04$2sjoySAZwkIQ.vXmAdiweuyGDtgcu3bfRo6J02BdXhxFCjX8DBKcq",
            "Lenora,,lpicard7@blogs.com,iY8{#+S'3r<mKoD,$2a$04$BA9s13WwIwH3N1Hyuww6ZeRFogv1xPHQRVhx/OFZ.C7cth4tlAssK",
            ",,,yI7(!nd8+fSB$TGk,$2a$04$ONYVhoHx2RjqwpW2WJpTt.BuvnCWJl563nFSPCekEcWo0P1/cCK7O"
    })
    void register(String name, String username, String email, String password, String encondePassword){
        RegisterDto registerDto = new RegisterDto(name,username,email,password);

        Role role = new Role((short) 1,"ROLE_USER");
        Mockito.when(passwordEncoder.encode(registerDto.getPassword())).thenReturn(encondePassword);
        Mockito.when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));

        assertEquals(authService.register(registerDto),"User registered successfully!.");

    }

}