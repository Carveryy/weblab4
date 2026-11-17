<%@ page import="java.util.List" %>
<%@ page import="weblab4.data.Category" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% List<Category> categories = (List<Category>) request.getAttribute("categories"); %>
<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <title>Управління категоріями</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f8f9fa; }
        .nav { margin-bottom: 20px; padding: 10px; background: #e9ecef; border-radius: 5px; }
        .nav a { margin-right: 15px; text-decoration: none; font-weight: bold; color: #0d6efd; }
        table { width: 100%; border-collapse: collapse; margin-bottom: 30px; background-color: white; }
        th, td { border: 1px solid #dee2e6; padding: 10px; text-align: left; }
        th { background-color: #e9ecef; }
        form { max-width: 400px; padding: 20px; background: white; border: 1px solid #ccc; border-radius: 5px; }
        input { width: 100%; padding: 8px; box-sizing: border-box; margin-bottom: 10px; }
        button { background-color: #198754; color: white; border: none; padding: 10px; cursor: pointer; border-radius: 3px; }
    </style>
</head>
<body>
<div class="nav">
    <a href="drinks">🥤 Напої</a>
    <a href="categories">📂 Категорії</a>
</div>

<h2>Список категорій</h2>
<table>
    <tr>
        <th>ID</th>
        <th>Назва</th>
        <th>Дії</th>
    </tr>
    <% if (categories != null) {
        for (Category cat : categories) { %>
    <tr>
        <td><%= cat.getId() %></td>
        <td><%= cat.getName() %></td>
        <td>
            <a href="categories?action=edit&id=<%= cat.getId() %>">Редагувати</a> |
            <a href="categories?action=delete&id=<%= cat.getId() %>" onclick="return confirm('Видалити категорію?')">Видалити</a>
        </td>
    </tr>
    <%  } } %>
</table>

<hr>
<h3>Додати нову категорію</h3>
<form action="categories?action=add" method="post"
      onsubmit="return confirm('Додати нову категорію?');">
    <label>Назва категорії:</label>
    <input type="text" name="name" required>
    <button type="submit">Додати</button>
</form>
</body>
</html>