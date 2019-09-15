package ru.javawebinar.topjava;

import org.springframework.test.web.servlet.ResultMatcher;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.TestUtil.readListFromJsonMvcResult;

public class MealToTestData {

    public static void assertMatch(Iterable<MealTo> actual, Iterable<MealTo> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

    public static ResultMatcher contentJson(Meal... expected) {
        return result -> assertMatch(readListFromJsonMvcResult(result, MealTo.class), MealsUtil.getWithExcess(List.of(expected), SecurityUtil.authUserCaloriesPerDay()));
    }
}
