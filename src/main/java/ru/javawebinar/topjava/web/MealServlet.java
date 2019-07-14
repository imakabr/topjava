package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.Dao;
import ru.javawebinar.topjava.dao.MealDao;
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
    private final Dao<Meal> dao = new MealDao();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");

        switch (action != null ? action : "all") {
            case "delete":
                int mealId = Integer.parseInt(request.getParameter("mealId"));
                dao.delete(mealId);
                response.sendRedirect("meals");
                break;
            case "edit":
                mealId = Integer.parseInt(request.getParameter("mealId"));
                Meal meal = dao.getById(mealId);
                request.setAttribute("meal", meal);
            case "add":
                request.getRequestDispatcher("/mealEdit.jsp").forward(request, response);
                break;
            default:
                List<MealTo> listMealTo = MealsUtil.getFilteredWithExcess(dao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
                request.setAttribute("meals", listMealTo);
                log.debug("doGet to /meals.jsp");
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"), dateTimeFormatter);
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = new Meal(dateTime, description, calories);
        String mealId = request.getParameter("mealId");
        if (mealId == null || mealId.isEmpty()) {
            dao.add(meal);
        } else {
            meal.setId(Integer.parseInt(mealId));
            dao.update(meal);
        }
        log.debug("doPost redirect to /meals.jsp");
        response.sendRedirect("meals");
    }
}
