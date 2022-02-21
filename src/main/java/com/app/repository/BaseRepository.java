package com.app.repository;

import java.util.List;

public interface BaseRepository<T, I> {

    List<T> getAll();

    T getById(I id);

    boolean existsById(I id);

    void save(T elem);

    void update(T elem);

    void saveOrUpdate(T elem);

    T deleteById(I id);

    void clear();
}
