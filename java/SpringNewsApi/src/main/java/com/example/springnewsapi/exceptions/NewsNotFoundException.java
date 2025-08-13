package com.example.springnewsapi.exceptions;

public class NewsNotFoundException extends RuntimeException{
    public NewsNotFoundException(Long newsId) {
        super("Новость с ID " + newsId + " не найдена.");
    }
}
