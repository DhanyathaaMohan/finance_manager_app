package com.finance_manager.dao;

import com.finance_manager.util.DatabaseConnection;
import com.finance_manager.model.Category;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


public class CategoryDAO{
    private static final String LOAD_TODOS = "SELECT * FROM category";

    private Category getCategoryRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        return new Category(id, name);  
    }

    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(LOAD_TODOS);
            ResultSet rs = stmt.executeQuery())
        {
            while (rs.next()) {
                categories.add(getCategoryRow(rs));

        }
        }
        return categories;
    }       
}