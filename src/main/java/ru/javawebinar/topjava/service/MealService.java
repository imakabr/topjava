package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Collection;

public interface MealService {
    Meal create(Meal meal, Integer userId);

    void update(Meal meal, Integer userId);

    void delete(int mealId, Integer userId) throws NotFoundException;;

    Meal get(int mealId, Integer userId) throws NotFoundException;;

    Collection<Meal> getAll(Integer userId);

    Collection<Meal> getFilteredByDates(Integer userId, LocalDate startDate, LocalDate endDate);
}