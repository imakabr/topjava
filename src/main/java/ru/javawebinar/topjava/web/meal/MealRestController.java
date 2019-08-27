package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.AbstractMealController;


@Controller
public class MealRestController extends AbstractMealController {

    @Autowired
    public MealRestController(MealService service) {
        super(service);
    }
}