package com.springboot.blog.service;

import com.springboot.blog.entity.Category;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;


    @Test
    void addCategoryWithAllOk() {

        Long id = 7L;
        String name = "testName";
        String description = "testDescription";

        Category categoryExpected = new Category(id, name, description, null);

        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(categoryExpected);

        CategoryDto  categoryDto = new CategoryDto(id, name, description);

        Mockito.when(modelMapper.map(Mockito.any(), Mockito.eq(CategoryDto.class))).thenReturn(categoryDto);

        Mockito.when(modelMapper.map(Mockito.any(), Mockito.eq(Category.class))).thenReturn(categoryExpected);

        CategoryDto categoryDtoExpected = categoryServiceImpl.addCategory(categoryDto);

        assertEquals(categoryDto.getId(), categoryDtoExpected.getId());
        assertEquals(categoryDto.getName(), categoryDtoExpected.getName());
        assertEquals(categoryDto.getDescription(), categoryDtoExpected.getDescription());

    }

    @Test
    void getCategory() {
        Category category = new Category();
        Long categoryId = 1L;
        category.setId(categoryId);

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());

        Mockito.when(modelMapper.map(Mockito.any(Category.class), Mockito.eq(CategoryDto.class)))
                .thenReturn(categoryDto);

        CategoryDto result = categoryService.getCategory(categoryDto.getId());

        assertNotNull(result);
        assertEquals(category.getId(), result.getId());
    }

    //Luque
    @Test
    void getAllCategories_ReturnsAllCategories() {
        List<Category> categories = List.of(new Category(), new Category());
        Mockito.when(categoryRepository.findAll()).thenReturn(categories);

        List<CategoryDto> dtoList = List.of(new CategoryDto(), new CategoryDto());
        Mockito.when(modelMapper.map(Mockito.any(), Mockito.eq(CategoryDto.class)))
                .thenReturn(dtoList.get(0))
                .thenReturn(dtoList.get(1));

        List<CategoryDto> result = categoryService.getAllCategories();

        assertEquals(dtoList, result);
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void updateCategory_ReturnsCategoryDto() {

        Long id = 1L;

        Category oldCategory = new Category(id,"Pelis","Lista de peliculas",new ArrayList<>());
        CategoryDto newCategory = new CategoryDto(id,"Series","Lista de series");
        Category updateCategory = new Category(id,"Series","Lista de series",new ArrayList<>());


        Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.of(oldCategory));
        Mockito.when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(updateCategory);
        Mockito.when(modelMapper.map(Mockito.any(), Mockito.eq(CategoryDto.class)))
                .thenReturn(newCategory);

        Assertions.assertEquals(newCategory, categoryService.updateCategory(newCategory,id));

    }

    @Test
    void updateCategory_ResourceNotFoundException() {

        Long id = 1L;

        CategoryDto newCategory = new CategoryDto(id,"Series","Lista de series");
        Mockito.when(categoryRepository.findById(id)).thenThrow(new ResourceNotFoundException("Category","id",id));

        Assertions.assertThrows(ResourceNotFoundException.class,() -> categoryService.updateCategory(newCategory,id));

    }

    @Test
    void deleteCategoryWithSuccess() {

        Long id = 1L;
        String name = "testName";
        String description = "testDescription";

        Category categoryExpected = new Category(id, name, description, null);

        Mockito.when(categoryRepository.findById(categoryExpected.getId())).thenReturn(Optional.of(categoryExpected));

        categoryServiceImpl.deleteCategory(categoryExpected.getId());

        Mockito.verify(categoryRepository, Mockito.times(1)).delete(categoryExpected);

    }

    @Test
    void deleteCategoryWithIdNotFound(){

        Long id = 1L;
        String name = "testName";
        String description = "testDescription";

        Category categoryExpected = new Category(id, name, description, null);

        Mockito.when(categoryRepository.findById(categoryExpected.getId())).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> categoryServiceImpl.deleteCategory(categoryExpected.getId()));
    }
}