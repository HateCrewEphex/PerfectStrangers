package com.mycompany.perfectstrangers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/db_perfectstrangers?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new SQLException("No se pudo cargar el driver JDBC de MySQL.", ex);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
