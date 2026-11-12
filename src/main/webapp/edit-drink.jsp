<%@ page import="weblab4.data.Drink" %>
<%@ page import="weblab4.data.Category" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Drink drink = (Drink) request.getAttribute("drinkToEdit");
    List<Category> categories = (List<Category>) request.getAttribute("categories");
%>
<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <title>Редагування напою</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f8f9fa; }
        form { max-width: 400px; padding: 20px; background: white; border: 1px solid #ccc; border-radius: 5px; }
        div { margin-bottom: 10px; }
        input, select { width: 100%; padding: 8px; box-sizing: border-box; }
        button { background-color: #0d6efd; color: white; border: none; padding: 10px; cursor: pointer; border-radius: 3px; }
    </style>
</head>
<body>
<h2>Редагувати напій: <%= drink.getName() %></h2>

<form action="drinks?action=update" method="post">
    <input type="hidden" name="id" value="<%= drink.getId() %>">

    <div>
        <label>Назва:</label>
        <input type="text" name="name" value="<%= drink.getName() %>" required>
    </div>
    <div>
        <label>Категорія:</label>
        <select name="categoryId">
            <option value="">-- Оберіть категорію --</option>
            <% if (categories != null) {
                for (Category cat : categories) {
                    boolean isSelected = (drink.getCategory() != null && drink.getCategory().getId().equals(cat.getId()));
            %>
            <option value="<%= cat.getId() %>" <%= isSelected ? "selected" : "" %>><%= cat.getName() %></option>
            <%  }
            } %>
        </select>
    </div>
    <div>
        <label>Об'єм (л):</label>
        <input type="number" step="0.01" name="volume" value="<%= drink.getVolume() %>" required>
    </div>
    <div>
        <label>Ціна (грн):</label>
        <input type="number" step="0.01" name="price" value="<%= drink.getPrice() %>" required>
    </div>
    <button type="submit">Зберегти зміни</button>
</form>

<p><a href="drinks">Повернутися до списку</a></p>
</body>
</html>