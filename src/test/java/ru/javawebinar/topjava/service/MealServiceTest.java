package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-app-jdbc.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal actual = service.get(START_SEQ_MEAL, USER_ID);
        Meal expected = meal_1;
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    public void delete() {
        List<Meal> meals = Arrays.asList(meal_6, meal_5, meal_4, meal_3, meal_2);
        service.delete(START_SEQ_MEAL, USER_ID);
        assertThat(meals).usingFieldByFieldElementComparator().isEqualTo(service.getAll(USER_ID));
    }

    @Test
    public void getBetweenDates() {
        List<Meal> actual = service.getBetweenDates(LocalDate.of(2015, Month.MAY, 30), LocalDate.of(2015, Month.MAY, 30), USER_ID);
        List<Meal> expected = Arrays.asList(meal_3, meal_2, meal_1);
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> actual = service.getBetweenDateTimes(LocalDateTime.of(2015, Month.MAY, 30, 12, 0), LocalDateTime.of(2015, Month.MAY, 30, 15, 0), USER_ID);
        List<Meal> expected = Arrays.asList(meal_2);
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

    @Test
    public void getAll() {
        List<Meal> actual = service.getAll(USER_ID);
        List<Meal> expected = Arrays.asList(meal_6, meal_5, meal_4, meal_3, meal_2, meal_1);
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

    @Test
    public void update() {
        Meal meal = new Meal(START_SEQ_MEAL, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
        meal.setCalories(666);
        meal.setDescription("Сердце врага");
        service.update(meal, USER_ID);
        assertThat(meal).isEqualToComparingFieldByField(service.get(START_SEQ_MEAL, USER_ID));
    }

    @Test
    public void create() {
        Meal meal = new Meal(LocalDateTime.of(2019, Month.JULY, 28, 15, 0), "Жирный обед", 1500);
        Meal created = service.create(meal, USER_ID);
        assertThat(meal).isEqualToComparingFieldByField(service.get(created.getId(), USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(START_SEQ_MEAL, USER_ID+1);
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotFound() {
        service.delete(START_SEQ_MEAL, USER_ID+1);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        Meal meal = new Meal(START_SEQ_MEAL, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
        service.update(meal, USER_ID+1);
    }
}