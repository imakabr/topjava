package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealDB;

import java.util.List;

public class MealDaoDB implements Dao<Meal> {
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
        MealDB.update(meal);
    }

    @Override
    public List<Meal> getAll() {
        return MealDB.getAll();
    }

    @Override
    public Meal getById(int mealId) {
        return MealDB.get(mealId);
    }
}
