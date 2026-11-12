package weblab4.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
// Видалили імпорт DrinkDao, додали CategoryDao
import weblab4.dao.CategoryDao;
import weblab4.data.Category;

import java.io.IOException;

@WebServlet("/categories")
public class CategoryServlet extends HttpServlet {
    // Змінили тип змінної
    private CategoryDao categoryDao;

    @Override
    public void init() {
        // Ініціалізуємо новий DAO
        categoryDao = new CategoryDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            // Використовуємо categoryDao
            request.setAttribute("categories", categoryDao.getAllCategories());
            request.getRequestDispatcher("/categories.jsp").forward(request, response);
        } else if ("edit".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            request.setAttribute("categoryToEdit", categoryDao.getCategoryById(id));
            request.getRequestDispatcher("/edit-category.jsp").forward(request, response);
        } else if ("delete".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            categoryDao.deleteCategory(id);
            response.sendRedirect(request.getContextPath() + "/categories");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        String name = request.getParameter("name");

        if ("add".equals(action)) {
            Category cat = new Category();
            cat.setName(name);
            categoryDao.saveCategory(cat);
        } else if ("update".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            Category cat = new Category();
            cat.setId(id);
            cat.setName(name);
            categoryDao.updateCategory(cat);
        }
        response.sendRedirect(request.getContextPath() + "/categories");
    }
}