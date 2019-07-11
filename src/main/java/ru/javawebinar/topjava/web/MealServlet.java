package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.Dao;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsFactory;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);
    private final Dao<Meal> dao = new MealDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action != null && action.equalsIgnoreCase("delete")){
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            dao.delete(mealId);
            List<MealTo> listMealTo = MealsUtil.getFilteredWithExcess(dao.getAll(), LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
            request.setAttribute("listMealTo", listMealTo);
        } else if (action != null && action.equalsIgnoreCase("edit")){
            int userId = Integer.parseInt(request.getParameter("userId"));
            Meal meal = dao.getById(userId);
            request.setAttribute("user", meal);
        } else if (action != null && action.equalsIgnoreCase("listUser")){
            request.setAttribute("users", dao.getAll());
        } else {
            List<MealTo> listMealTo = MealsUtil.getFilteredWithExcess(dao.getAll(), LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
            request.setAttribute("listMealTo", listMealTo);
        }
            log.debug("forward listMealTo to /meals.jsp");
            request.getRequestDispatcher("/meals.jsp").forward(request, response);

    }
}
