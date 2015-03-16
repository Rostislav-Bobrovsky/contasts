package com.itechart.contacts.dao.generic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Rostislav on 25-Feb-15.)
 */
public final class ConnectionFactory {
    private final static Logger logger = LogManager.getLogger(ConnectionFactory.class);

    public static Connection openConnection() {
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/contacts_rostislav";
            String name = "root";
            String password = "root";

            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(url, name, password);
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("{}:{}; exception {}; {}", Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage(), e);
        }
        throw new RuntimeException("Connection hasn't opened.");
    }
}
