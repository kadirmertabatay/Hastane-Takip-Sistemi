package com.example.hastanetakipsistemi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Bolum {

    public Bolum(){}
    private int bolumID;
    private String ad;
    private String lokasyon;

    public Bolum(int bolumID, String ad, String lokasyon) {
        this.bolumID = bolumID;
        this.ad = ad;
        this.lokasyon = lokasyon;
    }

    public int getBolumID() { return bolumID; }
    public String getAd() { return ad; }
    public String getLokasyon() { return lokasyon; }

    String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=HastaneVeriTabani;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";

    public void bolumEkle(String ad, String lokasyon) {
        String query = "INSERT INTO Bolumler (Ad, Lokasyon) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, ad);
            preparedStatement.setString(2, lokasyon);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Bölüm başarıyla eklendi!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
