package com.example.springnewsapi.services;

import com.example.springnewsapi.dto.NewsDto;
import com.example.springnewsapi.entity.Category;
import com.example.springnewsapi.entity.News;
import com.example.springnewsapi.exceptions.CategoryNotFoundException;
import com.example.springnewsapi.exceptions.InvalidDataException;
import com.example.springnewsapi.exceptions.NewsNotFoundException;
import com.example.springnewsapi.repositories.CategoryRepository;
import com.example.springnewsapi.repositories.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsCRUDService implements CRUDService<NewsDto> {

    private final NewsRepository newsRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public NewsDto getById(Long id) {
        log.info("Get news by ID: " + id);
        News news = newsRepository.findById(id).orElseThrow(() -> new NewsNotFoundException(id));
        return mapToDto(news);
    }

    @Override
    public Collection<NewsDto> getAll() {
        log.info("Get all news");
        return newsRepository.findAll()
                .stream()
                .map(NewsCRUDService::mapToDto)
                .toList();
    }

    @Override
    public NewsDto create(NewsDto newsDto) {
        log.info("Create news");
        invalidNewsTitle(newsDto.getTitle());
        invalidNewsText(newsDto.getText());
        News news = mapToEntity(newsDto);
        String categoryTitle = validateCategoryTitle(newsDto);
        Category category = categoryRepository.findByTitle(categoryTitle)
                .orElseThrow(() -> new CategoryNotFoundException(categoryTitle));
        news.setCategory(category);
        News saveNews = newsRepository.save(news);
        return mapToDto(saveNews);
    }

    @Override
    public NewsDto update(NewsDto newsDto) {
        log.info("Update news");
        invalidNewsTitle(newsDto.getTitle());
        invalidNewsText(newsDto.getText());
        Long id = newsDto.getId();
        News news = newsRepository.findById(id).orElseThrow(() -> new NewsNotFoundException(id));
        news.setTitle(newsDto.getTitle());
        news.setText(newsDto.getText());
        String categoryTitle = validateCategoryTitle(newsDto);
        Category category = categoryRepository.findByTitle(categoryTitle)
                .orElseThrow(() -> new CategoryNotFoundException(categoryTitle));
        news.setCategory(category);
        News saveNews = newsRepository.save(news);
        return mapToDto(saveNews);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Delete news " + id);
        News news = newsRepository.findById(id).orElseThrow(() -> new NewsNotFoundException(id));

        if (!newsRepository.existsById(id)) {
            throw new NewsNotFoundException(id);
        }

        newsRepository.deleteById(id);
    }

    public List<NewsDto> getNewsByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        List<News> newsList = newsRepository.findAllByCategory(category);
        return newsList.stream()
                .map(NewsCRUDService::mapToDto)
                .collect(Collectors.toList());
    }

    private boolean isBlank(String data) {
        return data == null || data.trim().isEmpty();
    }

    private void invalidNewsTitle(String title) {
        if (isBlank(title)) {
            throw new InvalidDataException("Заголовок новости не может быть пустым.");
        }
    }

    private void invalidNewsText(String text) {
        if (isBlank(text)) {
            throw new InvalidDataException("Текст новости не может быть пустым.");
        }
    }

    private String validateCategoryTitle(NewsDto newsDto) {
        if (isBlank(newsDto.getCategory())) {
            throw new IllegalArgumentException("Название категории не может быть null.");
        }
        return newsDto.getCategory();
    }

    public static NewsDto mapToDto(News news) {
        NewsDto newsDto = new NewsDto();
        newsDto.setId(news.getId());
        newsDto.setTitle(news.getTitle());
        newsDto.setText(news.getText());
        newsDto.setDate(news.getDate());
        newsDto.setCategory(news.getCategory().getTitle());
        return newsDto;
    }

    public static News mapToEntity(NewsDto newsDto) {
        News news = new News();
        news.setTitle(newsDto.getTitle());
        news.setText(newsDto.getText());
        news.setDate(newsDto.getDate());
        return news;
    }
}
