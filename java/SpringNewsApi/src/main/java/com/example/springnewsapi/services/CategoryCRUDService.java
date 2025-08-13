package com.example.springnewsapi.services;

import com.example.springnewsapi.dto.CategoryDto;
import com.example.springnewsapi.entity.Category;
import com.example.springnewsapi.exceptions.CategoryNotFoundException;
import com.example.springnewsapi.exceptions.InvalidDataException;
import com.example.springnewsapi.exceptions.NewsNotFoundException;
import com.example.springnewsapi.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryCRUDService implements CRUDService<CategoryDto> {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto getById(Long id) {
        log.info("Get category by ID: " + id);
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        return mapToDto(category);
    }

    @Override
    public Collection<CategoryDto> getAll() {
        log.info("Get all categories");
        return categoryRepository.findAll()
                .stream()
                .map(CategoryCRUDService::mapToDto)
                .toList();
    }

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        log.info("Create category");
        invalidCategoryTitle(categoryDto.getTitle());
        Category category = mapToEntity(categoryDto);
        Category saveCategory = categoryRepository.save(category);
        return mapToDto(saveCategory);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        log.info("Update category");
        invalidCategoryTitle(categoryDto.getTitle());
        Long id = categoryDto.getId();
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        category.setTitle(categoryDto.getTitle());
        Category saveCategory = categoryRepository.save(category);
        return mapToDto(saveCategory);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Delete category " + id);
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));

        if (!categoryRepository.existsById(id)) {
            throw new NewsNotFoundException(id);
        }

        categoryRepository.deleteById(id);
    }

    private boolean isBlank(String data) {
        return data == null || data.trim().isEmpty();
    }

    private void invalidCategoryTitle(String title) {
        if (isBlank(title)) {
            throw new InvalidDataException("Название категории не может быть пустым.");
        }
    }

    public static CategoryDto mapToDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setTitle(category.getTitle());
        return categoryDto;
    }

    public static Category mapToEntity(CategoryDto categoryDto) {
        Category category = new Category();
        category.setTitle(categoryDto.getTitle());
        return category;
    }
}
