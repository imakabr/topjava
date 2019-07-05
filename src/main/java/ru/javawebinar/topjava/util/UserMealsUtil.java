package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        //getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        List<UserMealWithExceed> list = getFilteredWithExceeded(mealList, LocalTime.of(15, 0), LocalTime.of(22, 0), 2000);
        show(list);
    }

    private static void show(List<UserMealWithExceed> list ) {
        for (UserMealWithExceed user : list) {
            String s = user.exceed ? "RED" : "GREEN";
            System.out.println("Date: " + user.dateTime.toString() + " Desc: " + user.description + " Calories: " + user.calories + " Color " + s);
        }
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with correctly exceeded field
        List<UserMeal> cache = new ArrayList<>();
        List<UserMealWithExceed> list = new ArrayList<>();
        LocalDate currDate = mealList.get(0).getDateTime().toLocalDate();
        int countCalories = caloriesPerDay;
        for (UserMeal userMeal : mealList) {
            LocalDate date = userMeal.getDateTime().toLocalDate();
            if (!currDate.equals(date)) {
                pushCache(cache, countCalories, list);
                countCalories = caloriesPerDay;
                currDate = date;
            }
            countCalories -= userMeal.getCalories();
            LocalTime time = userMeal.getDateTime().toLocalTime();
            if (TimeUtil.isBetween(time, startTime, endTime)) {
                cache.add(userMeal);
            }
        }
        pushCache(cache, countCalories, list);
        return list;
    }

    private static void pushCache(List<UserMeal> cache, int countCalories, List<UserMealWithExceed> list) {
        boolean exceed = countCalories < 0;
        for (UserMeal userMeal : cache) {
            LocalDateTime date = userMeal.getDateTime();
            String description = userMeal.getDescription();
            int calories = userMeal.getCalories();
            list.add(new UserMealWithExceed(date, description, calories, exceed));
        }
        cache.clear();
    }
}
