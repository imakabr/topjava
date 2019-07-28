package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final List<Meal> MEALS;
    public static final int START_SEQ_MEAL = START_SEQ + 2;
    public static final int USER_ID = START_SEQ;

    static {
        int id = START_SEQ_MEAL;
        MEALS = Arrays.asList(
                new Meal(id++, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(id++, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(id++, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(id++, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(id++, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(id++, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
    }


    public static List<Meal> getSortedMeals() {
        return MEALS.stream()
                .sorted(Comparator.comparing(Meal::getId).reversed())
                .collect(Collectors.toList());
    }

    public static List<Meal> getBetweenDates(LocalDate startDate, LocalDate endDate) {
        return getSortedMeals().stream()
                .filter(meal -> Util.isBetween(meal.getDate(), startDate, endDate))
                .collect(Collectors.toList());
    }

    public static List<Meal> getBetweenDateTimes(LocalDateTime startDate, LocalDateTime endDate) {
        return getSortedMeals().stream()
                .filter(meal -> Util.isBetween(meal.getDateTime(), startDate, endDate))
                .collect(Collectors.toList());
    }

}
