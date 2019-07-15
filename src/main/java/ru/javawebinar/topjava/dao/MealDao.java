package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDao implements Dao<Meal> {
    private final AtomicInteger idMeal;
    private final ConcurrentHashMap<Integer, Meal> meals;

    public MealDao() {
        idMeal = new AtomicInteger(0);
        meals = new ConcurrentHashMap<>();
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 11, 0), "Завтрак", 1000));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 21, 0), "Ужин", 510));
        add(new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 12, 25), "Завтрак", 300));
        add(new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 14, 34), "Обед", 1100));
        add(new Meal(LocalDateTime.of(2015, Month.JUNE, 2, 13, 15), "Обед", 2300));
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal getById(int Id) {
        return meals.get(Id);
    }

    @Override
    public Meal add(Meal meal) {
        int id = idMeal.getAndIncrement();
        meal.setId(id);
        return meals.put(id, meal);
    }

    @Override
    public void delete(int id) {
        meals.remove(id);
    }

    @Override
    public void update(Meal meal) {
        meals.put(meal.getId(), meal);
    }

}
