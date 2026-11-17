<%@ page import="java.util.List" %>
<%@ page import="weblab4.data.Drink" %>
<%@ page import="weblab4.data.Category" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%!
    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }
%>
<%
    List<Drink> drinks = (List<Drink>) request.getAttribute("drinks");
    List<Category> categories = (List<Category>) request.getAttribute("categories");

    String filterName = (String) request.getAttribute("filterName");
    String filterCategory = (String) request.getAttribute("filterCategory");
    String filterMinVolume = (String) request.getAttribute("filterMinVolume");
    String filterMaxVolume = (String) request.getAttribute("filterMaxVolume");
    String filterMinPrice = (String) request.getAttribute("filterMinPrice");
    String filterMaxPrice = (String) request.getAttribute("filterMaxPrice");
%>
<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <title>Облік напоїв (CRUD)</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f8f9fa; }
        .nav { margin-bottom: 20px; padding: 10px; background: #e9ecef; border-radius: 5px; }
        .nav a { margin-right: 15px; text-decoration: none; font-weight: bold; color: #0d6efd; }
        table { width: 100%; border-collapse: collapse; margin-bottom: 30px; background-color: white; }
        th, td { border: 1px solid #dee2e6; padding: 10px; text-align: left; }
        th { background-color: #e9ecef; }
        hr { margin: 30px 0; }

        form { max-width: 400px; padding: 20px; background: white; border: 1px solid #ccc; border-radius: 5px; }
        form div { margin-bottom: 10px; }
        input, select { width: 100%; padding: 8px; box-sizing: border-box; }
        button { background-color: #0d6efd; color: white; border: none; padding: 10px; cursor: pointer; border-radius: 3px; }

        /* Нижній ряд: додавання + пошук */
        .bottom-row { display: flex; gap: 30px; align-items: flex-start; flex-wrap: wrap; }
        .panel { flex: 1 1 380px; box-sizing: border-box; padding: 20px; background: white;
            border: 1px solid #ccc; border-radius: 5px; }
        .panel h3 { margin-top: 0; }
        .panel form { max-width: none; padding: 0; border: none; border-radius: 0; background: transparent; }

        /* Дії в таблиці */
        .actions { display: flex; gap: 12px; align-items: center; }
        form.inline { display: inline; max-width: none; padding: 0; border: none; background: transparent; }
        form.inline input { width: auto; }
        .btn-link { background: none; border: none; padding: 0; color: #dc3545; font-size: inherit; cursor: pointer; }
        .btn-link:hover { text-decoration: underline; }

        /* Кнопки пошуку */
        .form-buttons { display: flex; gap: 10px; align-items: center; }
        .btn-secondary { display: inline-block; padding: 10px; border-radius: 3px; background: #6c757d;
            color: white; text-decoration: none; }
    </style>
</head>
<body>
<div class="nav">
    <a href="drinks">🥤 Напої</a>
    <a href="categories">📂 Категорії</a>
</div>

<h2>Список напоїв</h2>
<p>Знайдено записів: <%= drinks != null ? drinks.size() : 0 %></p>

<table>
    <tr>
        <th>ID</th>
        <th>Назва</th>
        <th>Категорія</th>
        <th>Об'єм (л)</th>
        <th>Ціна (грн)</th>
        <th>Дії</th>
    </tr>
    <% if (drinks == null || drinks.isEmpty()) { %>
    <tr><td colspan="6">Нічого не знайдено</td></tr>
    <% } else {
        for (Drink drink : drinks) { %>
    <tr>
        <td><%= drink.getId() %></td>
        <td><%= esc(drink.getName()) %></td>
        <td><%= (drink.getCategory() != null) ? esc(drink.getCategory().getName()) : "Немає" %></td>
        <td><%= drink.getVolume() %></td>
        <td><%= drink.getPrice() %></td>
        <td>
            <div class="actions">
                <a href="drinks?action=edit&id=<%= drink.getId() %>">Редагувати</a>
                <form class="inline" action="drinks?action=delete" method="post"
                      onsubmit="return confirm('Ви дійсно хочете видалити цей напій? Ця дія незворотна.');">
                    <input type="hidden" name="id" value="<%= drink.getId() %>">
                    <button type="submit" class="btn-link">Видалити</button>
                </form>
            </div>
        </td>
    </tr>
    <%  }
    } %>
</table>

<hr>

<!-- Нижній ряд: додавання (ліворуч) і пошук (праворуч) -->
<div class="bottom-row">

    <div class="panel">
        <h3>Додати новий напій</h3>
        <form action="drinks?action=add" method="post"
              onsubmit="return confirm('Додати новий напій?');">
            <div>
                <label>Назва:</label>
                <input type="text" name="name" required>
            </div>
            <div>
                <label>Категорія:</label>
                <select name="categoryId">
                    <option value="">Оберіть категорію</option>
                    <% if (categories != null) {
                        for (Category cat : categories) { %>
                    <option value="<%= cat.getId() %>"><%= esc(cat.getName()) %></option>
                    <%  }
                    } %>
                </select>
            </div>
            <div>
                <label>Об'єм (л):</label>
                <input type="number" step="0.01" name="volume" required>
            </div>
            <div>
                <label>Ціна (грн):</label>
                <input type="number" step="0.01" name="price" required>
            </div>
            <button type="submit">Додати напій</button>
        </form>
    </div>

    <div class="panel">
        <h3>Пошук і фільтрація</h3>
        <form action="drinks" method="get">
            <div>
                <label>Назва містить:</label>
                <input type="text" name="name" value="<%= esc(filterName) %>">
            </div>
            <div>
                <label>Категорія:</label>
                <select name="category">
                    <option value="">Усі</option>
                    <option value="none" <%= "none".equals(filterCategory) ? "selected" : "" %>>Без категорії</option>
                    <% if (categories != null) {
                        for (Category cat : categories) {
                            boolean sel = String.valueOf(cat.getId()).equals(filterCategory); %>
                    <option value="<%= cat.getId() %>" <%= sel ? "selected" : "" %>><%= esc(cat.getName()) %></option>
                    <%  }
                    } %>
                </select>
            </div>
            <div>
                <label>Об'єм від (л):</label>
                <input type="number" step="0.01" name="minVolume" value="<%= esc(filterMinVolume) %>">
            </div>
            <div>
                <label>Об'єм до (л):</label>
                <input type="number" step="0.01" name="maxVolume" value="<%= esc(filterMaxVolume) %>">
            </div>
            <div>
                <label>Ціна від (грн):</label>
                <input type="number" step="0.01" name="minPrice" value="<%= esc(filterMinPrice) %>">
            </div>
            <div>
                <label>Ціна до (грн):</label>
                <input type="number" step="0.01" name="maxPrice" value="<%= esc(filterMaxPrice) %>">
            </div>
            <div class="form-buttons">
                <button type="submit">Знайти</button>
                <a class="btn-secondary" href="drinks">Скинути</a>
            </div>
        </form>
    </div>

</div>
</body>
</html>