package ru.javawebinar.topjava.model;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MealDB {
    private volatile static int idMeal = 0;

    private final static List<Meal> meals;

    static {
        meals = Collections.synchronizedList(new ArrayList<>());
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 11, 0), "Завтрак", 1000));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 21, 0), "Ужин", 510));
        add(new Meal(LocalDateTime.of(2015, Month.JUNE, 01, 12, 25), "Завтрак", 300));
        add(new Meal(LocalDateTime.of(2015, Month.JUNE, 01, 14, 34), "Обед", 1100));
        add(new Meal(LocalDateTime.of(2015, Month.JUNE, 2, 13, 15), "Обед", 2300));
    }

    public static List<Meal> getAll() {
        synchronized (meals) {
            return meals;
        }
    }

    public static void add(Meal meal) {
        synchronized (meals) {
            meals.add(meal.setId(idMeal++));
        }
    }

    public static void delete(int id) {
        synchronized (meals) {
            meals.remove(id);
            idMeal = id;
            for (int i = id; i < meals.size(); i++) {
                meals.get(i).setId(idMeal++);
            }
        }
    }

    public static void update(Meal meal) {
        synchronized (meals) {
            int id = meal.getId();
            meals.set(id, meal);
        }
    }

    public static Meal get(int id) {
        synchronized (meals) {
            return meals.get(id);
        }
    }

    private MealDB() {
    }

}
