package com.example.springnewsapi.services;

import java.util.Collection;

public interface CRUDService<T> {
    T getById(Long id);
    Collection<T> getAll();
    T create(T item);
    T update(T item);
    void deleteById(Long id);
}
