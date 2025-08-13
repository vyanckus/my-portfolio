package com.example.springnewsapi.repositories;

import com.example.springnewsapi.entity.Category;
import com.example.springnewsapi.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findAllByCategory(Category category);
}
