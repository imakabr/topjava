package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;



import static ru.javawebinar.topjava.MealTestData.USER_ID;
import static ru.javawebinar.topjava.MealTestData.START_SEQ_MEAL;
import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
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
        Meal expected = MealTestData.MEALS.get(0);
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    public void delete() {
        List<Meal> meals = service.getAll(USER_ID);
        meals = meals.stream()
                .filter(x -> x.getId() != START_SEQ_MEAL)
                .collect(Collectors.toList());
        service.delete(START_SEQ_MEAL, USER_ID);
        assertThat(meals).usingFieldByFieldElementComparator().isEqualTo(service.getAll(USER_ID));
    }

    @Test
    public void getBetweenDates() {
        List<Meal> actual = service.getBetweenDates(LocalDate.of(2015, Month.MAY, 30), LocalDate.of(2015, Month.MAY, 30), USER_ID);
        List<Meal> expected = MealTestData.getBetweenDates(LocalDate.of(2015, Month.MAY, 30), LocalDate.of(2015, Month.MAY, 30));
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> actual = service.getBetweenDateTimes(LocalDateTime.of(2015, Month.MAY, 30, 12, 0), LocalDateTime.of(2015, Month.MAY, 30, 15, 0), USER_ID);
        List<Meal> expected = MealTestData.getBetweenDateTimes(LocalDateTime.of(2015, Month.MAY, 30, 12, 0), LocalDateTime.of(2015, Month.MAY, 30, 15, 0));
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

    @Test
    public void getAll() {
        List<Meal> actual = service.getAll(USER_ID);
        List<Meal> expected = MealTestData.getSortedMeals();
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