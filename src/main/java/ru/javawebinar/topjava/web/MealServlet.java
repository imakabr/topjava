package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.Dao;
import ru.javawebinar.topjava.dao.MealDaoDB;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);
    private final Dao<Meal> dao = new MealDaoDB();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action != null && action.equalsIgnoreCase("delete")) {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            dao.delete(mealId);
            response.sendRedirect("meals");
        } else if (action != null && action.equalsIgnoreCase("edit")) {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            Meal meal = dao.getById(mealId);
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("/mealEdit.jsp").forward(request, response);
        } else if (action != null && action.equalsIgnoreCase("add")) {
            request.getRequestDispatcher("/mealEdit.jsp").forward(request, response);
        } else {
            List<MealTo> listMealTo = MealsUtil.getFilteredWithExcess(dao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
            request.setAttribute("meals", listMealTo);
            log.debug("doGet to /meals.jsp");
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"), dateTimeFormatter);
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = new Meal(dateTime, description, calories);
        String userid = request.getParameter("userid");
        if (userid == null || userid.isEmpty()) {
            dao.add(meal);
        } else {
            meal.setId(Integer.parseInt(userid));
            dao.update(meal);
        }
        List<MealTo> listMealTo = MealsUtil.getFilteredWithExcess(dao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
        request.setAttribute("meals", listMealTo);
        log.debug("doPost to /meals.jsp");
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
