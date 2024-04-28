package com.alexstrive.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    List<T> getAll();

    Optional<T> getById(Long id);

    Optional<T> create(T entity);

    boolean updateById(Long id, T entity);

    boolean deleteById(Long id);
}
