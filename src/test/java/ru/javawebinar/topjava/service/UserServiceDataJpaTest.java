package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.datajpa.CrudUserRepository;

import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(value = Profiles.DATAJPA)
public class UserServiceDataJpaTest extends UserServiceTest {

    @Autowired
    CrudUserRepository crudUserRepository;

    @Test
    public void getWithMeals() throws Exception {
        User user = crudUserRepository.getWithMeals(USER_ID);
        assertMatch(user, USERWITHMEALS);
        MealTestData.assertMatch(user.getMeals(), USERWITHMEALS.getMeals());
    }
}
