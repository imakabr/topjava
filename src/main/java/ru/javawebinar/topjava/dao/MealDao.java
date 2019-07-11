package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealDB;

import java.util.List;

public class MealDao implements Dao<Meal> {
    @Override
    public void add(Meal meal) {
        MealDB.add(meal);
    }

    @Override
    public void delete(int mealId) {
        MealDB.delete(mealId);
    }

    @Override
    public void update(Meal meal) {

    }

    @Override
    public List<Meal> getAll() {
        return MealDB.getMeals();
    }

    @Override
    public Meal getById(int mealId) {
        return null;
    }
}
