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
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@WebServlet(value = {"", "/drinks"})
public class DrinkServlet extends HttpServlet {
    private DrinkDao drinkDao;
    private CategoryDao categoryDao;

    @Override
    public void init() {
        drinkDao = new DrinkDao();
        categoryDao = new CategoryDao();
    }

    private static Double parseDouble(String s) {
        if (s == null || s.isBlank()) return null;
        try {
            return Double.parseDouble(s.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Long parseLong(String s) {
        if (s == null || s.isBlank()) return null;
        try {
            return Long.parseLong(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Map<String, String> parseQuery(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        String query = request.getQueryString();
        if (query == null || query.isEmpty()) return params;

        for (String pair : query.split("&")) {
            if (pair.isEmpty()) continue;
            int idx = pair.indexOf('=');
            String key = idx >= 0 ? pair.substring(0, idx) : pair;
            String value = idx >= 0 ? pair.substring(idx + 1) : "";
            params.put(URLDecoder.decode(key, StandardCharsets.UTF_8),
                    URLDecoder.decode(value, StandardCharsets.UTF_8));
        }
        return params;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Map<String, String> q = parseQuery(request);
        String action = q.get("action");

        if (action == null) {
            String name = q.get("name");
            String category = q.get("category");
            String minVolume = q.get("minVolume");
            String maxVolume = q.get("maxVolume");
            String minPrice = q.get("minPrice");
            String maxPrice = q.get("maxPrice");

            boolean withoutCategory = "none".equals(category);
            Long categoryId = withoutCategory ? null : parseLong(category);

            request.setAttribute("drinks", drinkDao.searchDrinks(
                    name, categoryId, withoutCategory,
                    parseDouble(minVolume), parseDouble(maxVolume),
                    parseDouble(minPrice), parseDouble(maxPrice)));
            request.setAttribute("categories", categoryDao.getAllCategories());
            request.setAttribute("filterName", name);
            request.setAttribute("filterCategory", category);
            request.setAttribute("filterMinVolume", minVolume);
            request.setAttribute("filterMaxVolume", maxVolume);
            request.setAttribute("filterMinPrice", minPrice);
            request.setAttribute("filterMaxPrice", maxPrice);

            request.getRequestDispatcher("/drinks.jsp").forward(request, response);
        } else if ("edit".equals(action)) {
            Long id = Long.parseLong(q.get("id"));
            request.setAttribute("drinkToEdit", drinkDao.getDrinkById(id));
            request.setAttribute("categories", categoryDao.getAllCategories());
            request.getRequestDispatcher("/edit-drink.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/drinks");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            drinkDao.deleteDrink(id);
            response.sendRedirect(request.getContextPath() + "/drinks");
            return;
        }

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