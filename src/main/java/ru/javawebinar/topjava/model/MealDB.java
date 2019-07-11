package ru.javawebinar.topjava.model;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MealDB {
    private static int idMeal = 0;

    private static List<Meal> meals = create();

    public static List<Meal> getMeals() {
        return meals;
    }

    public static void add(Meal meal) {
        meals.add(meal.setId(idMeal++));
    }

    public static void delete(int id) {
        meals.remove(id);
        idMeal = id;
        for (int i = id; i<meals.size(); i++) {
            meals.get(i).setId(idMeal++);
        }
    }

    private MealDB() {
    }

    private static List<Meal> create() {
        meals = Arrays.asList(
                new Meal(idMeal++, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(idMeal++, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(idMeal++, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(idMeal++, LocalDateTime.of(2015, Month.MAY, 31, 11, 0), "Завтрак", 1000),
                new Meal(idMeal++, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(idMeal++, LocalDateTime.of(2015, Month.MAY, 31, 21, 0), "Ужин", 510)
        );
        List<Meal> list = new ArrayList<>();
        list.addAll(meals);
        return Collections.synchronizedList(list);
    }
}
