package com.example.hastanetakipsistemi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Doktor {
    private int doktorID;
    private String ad;
    private String soyad;
    private String uzmanlikAlani;
    private String tcKimlikNo;
    private String telefon;
    private String ePosta;

    public Doktor(int doktorID, String ad, String soyad, String uzmanlikAlani, String tcKimlikNo, String telefon, String ePosta) {
        this.doktorID = doktorID;
        this.ad = ad;
        this.soyad = soyad;
        this.uzmanlikAlani = uzmanlikAlani;
        this.tcKimlikNo = tcKimlikNo;
        this.telefon = telefon;
        this.ePosta = ePosta;
    }



    public int getDoktorID() { return doktorID; }
    public String getAd() { return ad; }
    public String getSoyad() { return soyad; }
    public String getUzmanlikAlani() { return uzmanlikAlani; }
    public String getTcKimlikNo() { return tcKimlikNo; }
    public String getTelefon() { return telefon; }
    public String getEPosta() { return ePosta; }

    private final String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=HastaneVeriTabani;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";

    public void doktorEkle(String ad, String soyad, String uzmanlikAlani, String tcKimlikNo, String telefon, String ePosta) {
        String query = "INSERT INTO Doktorlar (Ad, Soyad, UzmanlikAlani, TcKimlikNo, Telefon, EPosta) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, ad);
            preparedStatement.setString(2, soyad);
            preparedStatement.setString(3, uzmanlikAlani);
            preparedStatement.setString(4, tcKimlikNo);
            preparedStatement.setString(5, telefon);
            preparedStatement.setString(6, ePosta);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Doktor başarıyla eklendi!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
