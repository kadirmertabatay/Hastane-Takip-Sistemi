package com.example.hastanetakipsistemi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Personel {
    private int personelID;
    private String ad;
    private String soyad;
    private String gorev;
    private String telefon;
    private int bolumID;

    public Personel(){}
    public Personel(int personelID, String ad, String soyad, String gorev, String telefon, int bolumID) {
        this.personelID = personelID;
        this.ad = ad;
        this.soyad = soyad;
        this.gorev = gorev;
        this.telefon = telefon;
        this.bolumID = bolumID;
    }

    public int getPersonelID() { return personelID; }
    public String getAd() { return ad; }
    public String getSoyad() { return soyad; }
    public String getGorev() { return gorev; }
    public String getTelefon() { return telefon; }
    public int getBolumID() { return bolumID; }

    String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=HastaneVeriTabani;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";

    public void personelEkle(String ad, String soyad, String gorev, String telefon, int bolumID) {
        String query = "INSERT INTO Personel (Ad, Soyad, Gorev, Telefon, BolumID) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, ad);
            preparedStatement.setString(2, soyad);
            preparedStatement.setString(3, gorev);
            preparedStatement.setString(4, telefon);
            preparedStatement.setInt(5, bolumID);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Personel başarıyla eklendi!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
