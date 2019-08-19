package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(value = Profiles.DATAJPA)
public class UserServiceDataJpaTest extends UserServiceTest {
    @Test
    public void get() throws Exception {
        User user = service.get(USER_ID);
        assertMatch(user, USER);
    }
}
