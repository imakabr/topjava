package ru.javawebinar.topjava.web;

import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


import static org.slf4j.LoggerFactory.getLogger;

public class UserServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("user");
        switch (action == null ? "all" : action) {
            case "1":
                SecurityUtil.setAuthUserId(Integer.parseInt(action));
                break;
            case "2":
                SecurityUtil.setAuthUserId(Integer.parseInt(action));
                break;
            case "all":
            default:
                break;
        }
        log.debug("forward to users");
        request.setAttribute("user", SecurityUtil.authUserId());
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }
}
