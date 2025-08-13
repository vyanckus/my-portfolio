package com.example.springnewsapi.exceptions;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(Long categoryId) {
        super("Категория с ID " + categoryId + " не найдена.");
    }

    public CategoryNotFoundException(String categoryTitle) {
        super("Категория с названием " + categoryTitle + " не найдена.");
    }
}
