package weblab4.dao;

import weblab4.data.Category;
import weblab4.data.Drink;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DrinkDao {
    private static final String URL = "jdbc:mysql://localhost:3306/drinksweb_db?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "kiril";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public List<Drink> getAllDrinks() {
        List<Drink> drinks = new ArrayList<>();
        String query = "SELECT d.id, d.name, d.volume, d.price, c.id AS cat_id, c.name AS cat_name " +
                "FROM drinks d LEFT JOIN categories c ON d.category_id = c.id";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Drink drink = new Drink();
                drink.setId(rs.getLong("id"));
                drink.setName(rs.getString("name"));
                drink.setVolume(rs.getDouble("volume"));
                drink.setPrice(rs.getDouble("price"));

                long catId = rs.getLong("cat_id");
                if (!rs.wasNull()) {
                    Category cat = new Category();
                    cat.setId(catId);
                    cat.setName(rs.getString("cat_name"));
                    drink.setCategory(cat);
                }
                drinks.add(drink);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return drinks;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT id, name FROM categories";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Category cat = new Category();
                cat.setId(rs.getLong("id"));
                cat.setName(rs.getString("name"));
                categories.add(cat);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return categories;
    }

    public void saveDrink(Drink drink, Long categoryId) {
        String query = "INSERT INTO drinks (name, volume, price, category_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, drink.getName());
            pstmt.setDouble(2, drink.getVolume());
            pstmt.setDouble(3, drink.getPrice());
            if (categoryId != null) pstmt.setLong(4, categoryId);
            else pstmt.setNull(4, Types.BIGINT);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public Drink getDrinkById(Long id) {
        String query = "SELECT d.id, d.name, d.volume, d.price, c.id AS cat_id, c.name AS cat_name " +
                "FROM drinks d LEFT JOIN categories c ON d.category_id = c.id WHERE d.id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Drink drink = new Drink();
                    drink.setId(rs.getLong("id"));
                    drink.setName(rs.getString("name"));
                    drink.setVolume(rs.getDouble("volume"));
                    drink.setPrice(rs.getDouble("price"));

                    long catId = rs.getLong("cat_id");
                    if (!rs.wasNull()) {
                        Category cat = new Category();
                        cat.setId(catId);
                        cat.setName(rs.getString("cat_name"));
                        drink.setCategory(cat);
                    }
                    return drink;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public void updateDrink(Drink drink, Long categoryId) {
        String query = "UPDATE drinks SET name = ?, volume = ?, price = ?, category_id = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, drink.getName());
            pstmt.setDouble(2, drink.getVolume());
            pstmt.setDouble(3, drink.getPrice());
            if (categoryId != null) pstmt.setLong(4, categoryId);
            else pstmt.setNull(4, Types.BIGINT);
            pstmt.setLong(5, drink.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void deleteDrink(Long id) {
        String query = "DELETE FROM drinks WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}