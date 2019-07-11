package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MealsSingle {
    private static List<Meal> ourInstance = getMeals();

    public static List<Meal> getInstanceMeals() {
        return ourInstance;
    }

    private MealsSingle() {
    }

    private static List<Meal> getMeals() {
        return Collections.synchronizedList(Arrays.asList(
                new Meal(0, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(1, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(2, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(3, LocalDateTime.of(2015, Month.MAY, 31, 11, 0), "Завтрак", 1000),
                new Meal(4, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(5, LocalDateTime.of(2015, Month.MAY, 31, 21, 0), "Ужин", 510)
        ));
    }
}
