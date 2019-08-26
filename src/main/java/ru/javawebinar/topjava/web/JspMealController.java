package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController {

    @Autowired
    private MealService service;

    @Autowired
    MealRestController mealRestController;

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        mealRestController.delete(Integer.parseInt(id));
        return "redirect:/meals";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable String id, Model model) {
        final Meal meal = mealRestController.get(Integer.parseInt(id));
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/create")
    public String create(Model model) {
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping()
    public String meals(HttpServletRequest request, Model model) {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
//            case "delete":
//                int id = getId(request);
//                mealRestController.delete(id);
//                return "redirect:meals";
            case "create":
            case "update":
//                final Meal meal = "create".equals(action) ?
//                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
//                        mealRestController.get(getId(request));
//                model.addAttribute("meal", meal);
//                return "mealForm";
            case "filter":
                LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
                LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
                LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
                LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
                model.addAttribute("meals", mealRestController.getBetween(startDate, startTime, endDate, endTime));
                return "meals";
            case "all":
            default:
                model.addAttribute("meals", mealRestController.getAll());
        }
            return "meals";
    }

    @PostMapping()
    public String setMeal(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (StringUtils.isEmpty(request.getParameter("id"))) {
            mealRestController.create(meal);
        } else {
            mealRestController.update(meal, getId(request));
        }
        return "redirect:meals";
    }

        private int getId(HttpServletRequest request) {
            String paramId = Objects.requireNonNull(request.getParameter("id"));
            return Integer.parseInt(paramId);
        }
}
