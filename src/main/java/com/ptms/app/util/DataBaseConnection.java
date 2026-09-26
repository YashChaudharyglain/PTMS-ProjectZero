package com.ptms.app.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    private static final String dbUrl = "jdbc:mysql://localhost:3306/DB_NAME";
    private static final String username = "DB_USER";
    private static final String password = "DB_PASSWORD";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, username, password);
    }
    /*
    public static void main(String[] args) {

        try {
            Connection connection = DataBaseConnection.getConnection();

            System.out.println("Database connected successfully!");

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        }

     */

}
