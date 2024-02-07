package com.springboot.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.security.JwtTokenProvider;
import com.springboot.blog.service.CategoryService;
import com.springboot.blog.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
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

import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerWSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private CategoryController categoryController;

    @MockBean
    private CategoryService categoryService;

    @BeforeEach
   public void setup() {
    }
    @Test
    @WithMockUser(username = "username", roles = "{ADMIN}")
    void addCategoryWithResponse201Created() throws Exception {
        Long categoryId = 101L;
        String name = "Category 1";
        String description = "description";

        CategoryDto categoryDto = new CategoryDto(categoryId, name, description);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/categories", categoryDto)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isCreated());

    }

    @Test
    @WithMockUser(authorities = {"USER"})
     void addCategoryWithResponse401Unauthorized() throws Exception{
        Long categoryId = 1L;
        String name = "Category 1";
        String description = "description";

        CategoryDto categoryDto = new CategoryDto(categoryId, name, description);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/categories", categoryDto)
                .content(objectMapper.writeValueAsString(categoryDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        Mockito.verify(categoryService, never()).addCategory(categoryDto);
    }

    @Test
    @WithMockUser(username = "username", roles = "{ADMIN}")
    void addCategoryWithResponse400BadRequest() throws Exception{

        CategoryDto categoryDto = new CategoryDto();

        Mockito.when(categoryService.addCategory(categoryDto)).thenReturn(categoryDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/categories")
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
            Mockito.verify(categoryService, never()).addCategory(categoryDto);

    }


    @Test
    @WithMockUser(username = "username", roles = {"ADMIN"})
    void deleteCategoryWith200ResponseOK() throws Exception{

        Long idCategoria = 1L;

        CategoryDto categoryDto = new CategoryDto(idCategoria, null, null);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/categories/{id}", categoryDto.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "username", roles = {"ADMIN"})
    void deleteCategoriaWith404ResponseNotFound() throws Exception {

        Long idCategoria = null;

        CategoryDto categoryDto = new CategoryDto(idCategoria, null, null);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/categories/{id}", categoryDto.getId()))
                .andExpect(status().isNotFound());
    }

}
