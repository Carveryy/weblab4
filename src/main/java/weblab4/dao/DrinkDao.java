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

    private static final String SELECT_BASE =
            "SELECT d.id, d.name, d.volume, d.price, c.id AS cat_id, c.name AS cat_name " +
                    "FROM drinks d LEFT JOIN categories c ON d.category_id = c.id";

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

    private Drink mapDrink(ResultSet rs) throws SQLException {
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

    public List<Drink> getAllDrinks() {
        return searchDrinks(null, null, false, null, null, null, null);
    }

    public List<Drink> searchDrinks(String name, Long categoryId, boolean withoutCategory,
                                    Double minVolume, Double maxVolume,
                                    Double minPrice, Double maxPrice) {
        List<Drink> drinks = new ArrayList<>();
        StringBuilder sql = new StringBuilder(SELECT_BASE).append(" WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (name != null && !name.isBlank()) {
            sql.append(" AND LOWER(d.name) LIKE ?");
            params.add("%" + name.trim().toLowerCase() + "%");
        }
        if (withoutCategory) {
            sql.append(" AND d.category_id IS NULL");
        } else if (categoryId != null) {
            sql.append(" AND d.category_id = ?");
            params.add(categoryId);
        }
        if (minVolume != null) {
            sql.append(" AND d.volume >= ?");
            params.add(minVolume);
        }
        if (maxVolume != null) {
            sql.append(" AND d.volume <= ?");
            params.add(maxVolume);
        }
        if (minPrice != null) {
            sql.append(" AND d.price >= ?");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append(" AND d.price <= ?");
            params.add(maxPrice);
        }
        sql.append(" ORDER BY d.name");

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    drinks.add(mapDrink(rs));
                }
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
        String query = SELECT_BASE + " WHERE d.id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapDrink(rs);
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