package com.example.hastanetakipsistemi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BolumController {

    @FXML
    private TextField txtBolumID, txtAd, txtLokasyon, txtAra;

    @FXML
    private TableView<Bolum> tableView;

    @FXML
    private TableColumn<Bolum, Integer> bolumID;

    @FXML
    private TableColumn<Bolum, String> ad;

    @FXML
    private TableColumn<Bolum, String> lokasyon;

    private ObservableList<Bolum> bolumListesi;

    private final String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=HastaneVeriTabani;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";

    public void initialize() {
        bolumID.setCellValueFactory(new PropertyValueFactory<>("bolumID"));
        ad.setCellValueFactory(new PropertyValueFactory<>("ad"));
        lokasyon.setCellValueFactory(new PropertyValueFactory<>("lokasyon"));

        bolumListesi = FXCollections.observableArrayList();
        tabloyuGuncelle();
    }

    @FXML
    private void bolumEkleButonu() {
        String ad = txtAd.getText();
        String lokasyon = txtLokasyon.getText();

        if (bolumEkle(ad, lokasyon)) {
            txtAd.clear();
            txtLokasyon.clear();
            tabloyuGuncelle();
        }
    }

    private boolean bolumEkle(String ad, String lokasyon) {
        String query = "INSERT INTO Bolumler (Ad, Lokasyon) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, ad);
            preparedStatement.setString(2, lokasyon);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Başarılı");
                alert.setHeaderText("Kayıt Eklendi");
                alert.setContentText("Bölüm başarıyla eklendi!");
                alert.showAndWait();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @FXML
    private void bolumSilButonu() {
        String bolumIDText = txtBolumID.getText();

        if (bolumIDText.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Uyarı");
            alert.setHeaderText("Geçersiz Giriş");
            alert.setContentText("Lütfen bir BolumID giriniz.");
            alert.showAndWait();
            return;
        }

        try {
            int bolumID = Integer.parseInt(bolumIDText);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Silme İşlemi");
            alert.setHeaderText("Bölüm Silme");
            alert.setContentText("BolumID " + bolumID + " olan kaydı silmek istediğinize emin misiniz?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                String query = "DELETE FROM Bolumler WHERE BolumID = ?";
                try (Connection connection = DriverManager.getConnection(connectionUrl);
                     PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setInt(1, bolumID);
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Başarılı");
                        successAlert.setHeaderText("Silme İşlemi Tamamlandı");
                        successAlert.setContentText("Bölüm başarıyla silindi!");
                        successAlert.showAndWait();
                        tabloyuGuncelle();
                        temizle();
                    } else {
                        Alert alertFail = new Alert(Alert.AlertType.WARNING);
                        alertFail.setTitle("Başarısız");
                        alertFail.setHeaderText("Silme Başarısız");
                        alertFail.setContentText("Belirtilen ID bulunamadı.");
                        alertFail.showAndWait();
                    }
                }
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hata");
            alert.setHeaderText("Geçersiz Giriş");
            alert.setContentText("Lütfen geçerli bir BolumID giriniz.");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void bolumAraButonu() {
        try {
            int arananID = Integer.parseInt(txtBolumID.getText());

            String query = "SELECT * FROM Bolumler WHERE BolumID = ?";
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, arananID);
                ResultSet resultSet = preparedStatement.executeQuery();

                ObservableList<Bolum> aramaSonuclari = FXCollections.observableArrayList();

                if (resultSet.next()) {
                    int bolumID = resultSet.getInt("BolumID");
                    String ad = resultSet.getString("Ad");
                    String lokasyon = resultSet.getString("Lokasyon");

                    Bolum bolum = new Bolum(bolumID, ad, lokasyon);
                    aramaSonuclari.add(bolum);
                }

                tableView.setItems(aramaSonuclari);
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hata");
            alert.setHeaderText("Geçersiz Giriş");
            alert.setContentText("Lütfen geçerli bir BolumID giriniz.");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void bolumGuncelleButonu() {
        try {
            int bolumID = Integer.parseInt(txtBolumID.getText());
            String ad = txtAd.getText();
            String lokasyon = txtLokasyon.getText();

            String query = "UPDATE Bolumler SET Ad = ?, Lokasyon = ? WHERE BolumID = ?";
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, ad);
                preparedStatement.setString(2, lokasyon);
                preparedStatement.setInt(3, bolumID);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Başarılı");
                    alert.setHeaderText("Güncelleme Tamamlandı");
                    alert.setContentText("Bölüm başarıyla güncellendi!");
                    alert.showAndWait();

                    tabloyuGuncelle();
                    temizle();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Başarısız");
                    alert.setHeaderText("Güncelleme Başarısız");
                    alert.setContentText("Belirtilen BolumID bulunamadı.");
                    alert.showAndWait();
                }
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hata");
            alert.setHeaderText("Geçersiz Giriş");
            alert.setContentText("Lütfen geçerli bir BolumID giriniz.");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void tabloyuGuncelle() {
        bolumListesi.clear();
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Bolumler")) {

            while (resultSet.next()) {
                int bolumID = resultSet.getInt("BolumID");
                String ad = resultSet.getString("Ad");
                String lokasyon = resultSet.getString("Lokasyon");

                Bolum bolum = new Bolum(bolumID, ad, lokasyon);
                bolumListesi.add(bolum);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.setItems(bolumListesi);
    }

    private void temizle() {
        txtBolumID.clear();
        txtAd.clear();
        txtLokasyon.clear();
    }
}
