package com.example.springnewsapi.controllers;

import com.example.springnewsapi.dto.CategoryDto;
import com.example.springnewsapi.dto.ErrorMessage;
import com.example.springnewsapi.exceptions.CategoryNotFoundException;
import com.example.springnewsapi.exceptions.InvalidDataException;
import com.example.springnewsapi.services.CategoryCRUDService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryCRUDService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        try {
            CategoryDto categoryDto = categoryService.getById(id);
            return new ResponseEntity<>(categoryDto, HttpStatus.OK);
        } catch (CategoryNotFoundException e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public Collection<CategoryDto> getAllCategories() {
        return categoryService.getAll();
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryDto categoryDto) {
        try {
            CategoryDto createCategoryDto = categoryService.create(categoryDto);
            return new ResponseEntity<>(createCategoryDto, HttpStatus.CREATED);
        } catch (InvalidDataException e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDto categoryDto) {
        try {
            CategoryDto updateCategoryDto = categoryService.update(categoryDto);
            return new ResponseEntity<>(updateCategoryDto, HttpStatus.OK);
        } catch (CategoryNotFoundException e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        } catch (InvalidDataException e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CategoryNotFoundException e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }
    }
}
