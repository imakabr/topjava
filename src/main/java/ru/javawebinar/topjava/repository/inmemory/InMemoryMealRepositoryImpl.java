package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(x -> save(x, 1));
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        Map<Integer, Meal> userRepo = getRepo(userId);
        if (meal.isNew()) {
            log.info("new Meal");
            meal.setId(counter.incrementAndGet());
            userRepo.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        return userRepo.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int mealId, Integer userId) {
        Map<Integer, Meal> userRepo = getRepo(userId);
        return userRepo.remove(mealId) != null;
    }

    @Override
    public Meal get(int mealId, Integer userId) {
        Map<Integer, Meal> userRepo = getRepo(userId);
        return userRepo.get(mealId);
    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        return getFiltered(userId, meal -> DateTimeUtil.isBetween(meal.getDate(), LocalDate.MIN, LocalDate.MAX));
    }

    @Override
    public Collection<Meal> getFilteredByDates(Integer userId, LocalDate startDate, LocalDate endDate) {
        return getFiltered(userId, meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate));
    }

    private Collection<Meal> getFiltered(Integer userId, Predicate<Meal> filter) {
        Map<Integer, Meal> userRepo = getRepo(userId);
        return userRepo.values()
                .stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    private Map<Integer, Meal> getRepo(Integer userId) {
        return repository.computeIfAbsent(userId, val -> new ConcurrentHashMap<>());
    }
}

