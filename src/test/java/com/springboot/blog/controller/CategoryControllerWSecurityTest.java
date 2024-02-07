package com.springboot.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.blog.controller.SecurityContext.WithMockCustomUser;
import com.springboot.blog.exception.ResourceNotFoundException;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

        when(categoryService.addCategory(categoryDto)).thenReturn(categoryDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/categories")
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
            Mockito.verify(categoryService, never()).addCategory(categoryDto);

    }

    @Test
    @WithMockUser(username = "username", roles = {"ADMIN"})
    void updateCategory_ResturnsOK() throws Exception {

        Long id = 1L;

        CategoryDto newCategory = new CategoryDto(id,"Series","Lista de series");
        when(categoryService.updateCategory(any(),eq(id))).thenReturn(newCategory);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategory))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is(newCategory.getName())));



    }

    @Test
    @WithMockUser(username = "username", roles = {"USER"})
    void updateCategory_ResturnsUnauthorized() throws Exception {

        Long id = 1L;

        CategoryDto newCategory = new CategoryDto(id,"Series","Lista de series");
        when(categoryService.updateCategory(any(),eq(id))).thenReturn(newCategory);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategory))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser(username = "username", roles = {"ADMIN"})
    void updateCategory_ResturnsNotFound() throws Exception {

        Long id = 1L;

        CategoryDto newCategory = new CategoryDto(id,"Series","Lista de series");

        Mockito.when(categoryService.updateCategory(any(),eq(id))).thenThrow(new ResourceNotFoundException("Category", "id", id));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategory))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

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
