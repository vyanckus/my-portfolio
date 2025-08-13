package com.example.springnewsapi.controllers;

import com.example.springnewsapi.dto.ErrorMessage;
import com.example.springnewsapi.dto.NewsDto;
import com.example.springnewsapi.exceptions.CategoryNotFoundException;
import com.example.springnewsapi.exceptions.InvalidDataException;
import com.example.springnewsapi.exceptions.NewsNotFoundException;
import com.example.springnewsapi.services.NewsCRUDService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsCRUDService newsService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getNewsById(@PathVariable Long id) {
        try {
            NewsDto newsDto = newsService.getById(id);
            return new ResponseEntity<>(newsDto, HttpStatus.OK);
        } catch (NewsNotFoundException e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public Collection<NewsDto> getAllNews() {
        return newsService.getAll();
    }

    @PostMapping
    public ResponseEntity<?> createNews(@RequestBody NewsDto newsDto) {
        try {
            NewsDto createNewsDto = newsService.create(newsDto);
            return new ResponseEntity<>(createNewsDto, HttpStatus.CREATED);
        } catch (InvalidDataException e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateNews(@RequestBody NewsDto newsDto) {
        try {
            NewsDto updateNewsDto = newsService.update(newsDto);
            return new ResponseEntity<>(updateNewsDto, HttpStatus.OK);
        } catch (NewsNotFoundException e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        } catch (InvalidDataException e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable Long id) {
        try {
            newsService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NewsNotFoundException e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<?> getNewsByCategoryId(@PathVariable Long id) {
        try {
            List<NewsDto> newsDto = newsService.getNewsByCategoryId(id);
            return new ResponseEntity<>(newsDto, HttpStatus.OK);
        } catch (CategoryNotFoundException e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }
    }
}
