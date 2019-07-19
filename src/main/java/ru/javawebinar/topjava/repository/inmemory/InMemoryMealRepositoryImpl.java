package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(x -> save(x, 0));
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        Map<Integer, Meal> userRepo = repository.computeIfAbsent(userId, val -> new ConcurrentHashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            userRepo.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        return userRepo.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int mealId, Integer userId) {
        Map<Integer, Meal> userRepo = repository.computeIfAbsent(userId, val -> new ConcurrentHashMap<>());
        return userRepo.remove(mealId) != null;
    }

    @Override
    public Meal get(int mealId, Integer userId) {
        Map<Integer, Meal> userRepo = repository.computeIfAbsent(userId, val -> new ConcurrentHashMap<>());
        return userRepo.get(mealId);
    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        Map<Integer, Meal> userRepo = repository.computeIfAbsent(userId, val -> new ConcurrentHashMap<>());
        return userRepo.values();
    }
}

