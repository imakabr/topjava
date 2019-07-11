package ru.javawebinar.topjava.dao;

import java.util.List;

public interface Dao<T> {
    void add(T object);
    void delete(int Id);
    void update(T object);
    List<T> getAll();
    T getById(int Id);
}