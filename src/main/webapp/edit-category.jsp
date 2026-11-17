<%@ page import="weblab4.data.Category" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% Category cat = (Category) request.getAttribute("categoryToEdit"); %>
<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <title>Редагування категорії</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f8f9fa; }
        form { max-width: 400px; padding: 20px; background: white; border: 1px solid #ccc; border-radius: 5px; }
        input { width: 100%; padding: 8px; box-sizing: border-box; margin-bottom: 10px; }
        button { background-color: #0d6efd; color: white; border: none; padding: 10px; cursor: pointer; border-radius: 3px; }
    </style>
</head>
<body>
<h2>Редагувати категорію</h2>
<form action="categories?action=update" method="post"
      onsubmit="return confirm('Зберегти зміни?');">
    <input type="hidden" name="id" value="<%= cat.getId() %>">
    <label>Назва:</label>
    <input type="text" name="name" value="<%= cat.getName() %>" required>
    <button type="submit">Зберегти</button>
</form>
<p><a href="categories">Повернутися до списку</a></p>
</body>
</html>