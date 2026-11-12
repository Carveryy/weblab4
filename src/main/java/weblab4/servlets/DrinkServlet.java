package weblab4.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import weblab4.dao.CategoryDao;
import weblab4.dao.DrinkDao;
import weblab4.data.Drink;

import java.io.IOException;

@WebServlet(value = {"", "/drinks"})
public class DrinkServlet extends HttpServlet {
    private DrinkDao drinkDao;
    private CategoryDao categoryDao;

    @Override
    public void init() {
        drinkDao = new DrinkDao();
        categoryDao = new CategoryDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            request.setAttribute("drinks", drinkDao.getAllDrinks());
            request.setAttribute("categories", categoryDao.getAllCategories());
            request.getRequestDispatcher("/drinks.jsp").forward(request, response);
        } else if ("edit".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            request.setAttribute("drinkToEdit", drinkDao.getDrinkById(id));
            request.setAttribute("categories", categoryDao.getAllCategories());
            request.getRequestDispatcher("/edit-drink.jsp").forward(request, response);
        } else if ("delete".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            drinkDao.deleteDrink(id);
            response.sendRedirect(request.getContextPath() + "/drinks");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        String name = request.getParameter("name");
        Double volume = Double.parseDouble(request.getParameter("volume"));
        Double price = Double.parseDouble(request.getParameter("price"));

        String catIdParam = request.getParameter("categoryId");
        Long categoryId = (catIdParam != null && !catIdParam.isEmpty()) ? Long.parseLong(catIdParam) : null;

        if ("add".equals(action)) {
            Drink drink = new Drink();
            drink.setName(name);
            drink.setVolume(volume);
            drink.setPrice(price);
            drinkDao.saveDrink(drink, categoryId);
        } else if ("update".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            Drink drink = new Drink();
            drink.setId(id);
            drink.setName(name);
            drink.setVolume(volume);
            drink.setPrice(price);
            drinkDao.updateDrink(drink, categoryId);
        }

        response.sendRedirect(request.getContextPath() + "/drinks");
    }
}