package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;
import sun.rmi.server.UnicastServerRef;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        getFilteredWithExceededOptional(mealList, LocalTime.of(7, 0), LocalTime.of(11, 0), 2000);
    }

    public static List<UserMealWithExceed> getFilteredWithExceededOptional(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> countCalories = mealList.stream()
                .collect(Collectors.groupingBy(x -> x.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));
        return mealList.stream()
                .filter(x -> TimeUtil.isBetween(x.getDateTime().toLocalTime(), startTime, endTime))
                .map(x -> {
                    boolean exceed = countCalories.get(x.getDateTime().toLocalDate()) > caloriesPerDay;
                    return new UserMealWithExceed(x.getDateTime(), x.getDescription(), x.getCalories(), exceed);
                })
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> countCalories = new HashMap<>();
        for (UserMeal userMeal : mealList) {
            LocalDate date = userMeal.getDateTime().toLocalDate();
            int calories = userMeal.getCalories();
            countCalories.put(date, countCalories.getOrDefault(date, 0) + calories);
        }
        List<UserMealWithExceed> listUserMealWithExceed = new ArrayList<>();
        for (UserMeal userMeal : mealList) {
            LocalDateTime dateTime = userMeal.getDateTime();
            LocalTime time = dateTime.toLocalTime();
            if (TimeUtil.isBetween(time, startTime, endTime)) {
                LocalDate date = dateTime.toLocalDate();
                String description = userMeal.getDescription();
                int calories = userMeal.getCalories();
                boolean exceed = countCalories.get(date) > caloriesPerDay;
                listUserMealWithExceed.add(new UserMealWithExceed(dateTime, description, calories, exceed));
            }
        }
        return listUserMealWithExceed;
    }
}
