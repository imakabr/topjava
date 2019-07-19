package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealRepository {
    Meal save(Meal meal, Integer userId);

    // false if not found
    boolean delete(int mealId, Integer userId);

    // null if not found
    Meal get(int mealId, Integer userId);

    Collection<Meal> getAll(Integer userId);
}
