package com.itechart.contacts.dao.generic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Rostislav on 25-Feb-15.)
 */
public final class ConnectionFactory {

    public static Connection openConnection() {
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/contacts_rostislav";
            String name = "root";
            String password = "root";

            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(url, name, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Connection hasn't opened.");
    }
}
