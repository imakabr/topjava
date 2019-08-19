package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Transactional
    int deleteByIdAndUserId(int id, int userId);

    Meal getById(int id);

    List<Meal> getAllByUserIdOrderByDateTimeDesc(int userId);

    @Modifying
    @Query("SELECT m FROM Meal m " +
            "WHERE m.user.id=:userId AND m.dateTime BETWEEN :startDate AND :endDate ORDER BY m.dateTime DESC")
    List<Meal> getBetween(@Param("userId") int id, @Param("startDate") LocalDateTime start, @Param("endDate") LocalDateTime end);

    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.user WHERE m.id=:id ")
    Meal getWithUser(@Param("id") int id);
}
