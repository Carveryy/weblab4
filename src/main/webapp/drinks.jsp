<%@ page import="java.util.List" %>
<%@ page import="weblab4.data.Drink" %>
<%@ page import="weblab4.data.Category" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<Drink> drinks = (List<Drink>) request.getAttribute("drinks");
    List<Category> categories = (List<Category>) request.getAttribute("categories");
%>
<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <title>Облік напоїв (CRUD)</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f8f9fa; }
        table { width: 100%; border-collapse: collapse; margin-bottom: 30px; background-color: white; }
        th, td { border: 1px solid #dee2e6; padding: 10px; text-align: left; }
        th { background-color: #e9ecef; }
        form { max-width: 400px; padding: 20px; background: white; border: 1px solid #ccc; border-radius: 5px; }
        div { margin-bottom: 10px; }
        input, select { width: 100%; padding: 8px; box-sizing: border-box; }
        button { background-color: #0d6efd; color: white; border: none; padding: 10px; cursor: pointer; border-radius: 3px; }
    </style>
</head>
<body>
<div style="margin-bottom: 20px; padding: 10px; background: #e9ecef; border-radius: 5px;">
    <a href="drinks" style="margin-right: 15px; text-decoration: none; font-weight: bold; color: #0d6efd;">🥤 Напої</a>
    <a href="categories" style="text-decoration: none; font-weight: bold; color: #0d6efd;">📂 Категорії</a>
</div>
<h2>Список напоїв</h2>
<table>
    <tr>
        <th>ID</th>
        <th>Назва</th>
        <th>Категорія</th>
        <th>Об'єм (л)</th>
        <th>Ціна (грн)</th>
        <th>Дії</th>
    </tr>
    <% if (drinks != null) {
        for (Drink drink : drinks) { %>
    <tr>
        <td><%= drink.getId() %></td>
        <td><%= drink.getName() %></td>
        <td><%= (drink.getCategory() != null) ? drink.getCategory().getName() : "Немає" %></td>
        <td><%= drink.getVolume() %></td>
        <td><%= drink.getPrice() %></td>
        <td>
            <a href="drinks?action=edit&id=<%= drink.getId() %>">Редагувати</a> |
            <a href="drinks?action=delete&id=<%= drink.getId() %>" onclick="return confirm('Видалити?')">Видалити</a>
        </td>
    </tr>
    <%  }
    } %>
</table>

<hr>

<h3>Додати новий напій</h3>
<form action="drinks?action=add" method="post">
    <div>
        <label>Назва:</label>
        <input type="text" name="name" required>
    </div>
    <div>
        <label>Категорія:</label>
        <select name="categoryId">
            <option value="">-- Оберіть категорію --</option>
            <% if (categories != null) {
                for (Category cat : categories) { %>
            <option value="<%= cat.getId() %>"><%= cat.getName() %></option>
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
</body>
</html>