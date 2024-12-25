package com.example.hastanetakipsistemi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MSSQLConnect {

    public static void main(String[] args) {
        String serverName = "LAPTOPB\\SQLEXPRESS"; // Use double backslashes for escaping
        String database = "HastaneVeriTabani";

        //String connectionUrl = String.format("jdbc:sqlserver://%s;databaseName=%s;integratedSecurity=true;", serverName, database);

        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=HastaneVeriTabani;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";

        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            System.out.println("Connection successful!");
        } catch (SQLException ex) {
            System.out.println("Connection failed: " + ex.getMessage());
        }
    }
}
