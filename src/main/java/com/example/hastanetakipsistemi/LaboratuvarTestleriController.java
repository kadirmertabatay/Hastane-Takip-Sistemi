package com.example.hastanetakipsistemi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class LaboratuvarTestleriController {

    @FXML
    private TextField txtTestID, txtHastaID, txtTestTuru, txtSonuclar, txtTestTarihiSaati, txtPersonelID, txtAra;

    @FXML
    private TableView<LaboratuvarTestleri> tableView;

    @FXML
    private TableColumn<LaboratuvarTestleri, Integer> testID;

    @FXML
    private TableColumn<LaboratuvarTestleri, Integer> hastaID;

    @FXML
    private TableColumn<LaboratuvarTestleri, String> testTuru;

    @FXML
    private TableColumn<LaboratuvarTestleri, String> sonuclar;

    @FXML
    private TableColumn<LaboratuvarTestleri, Date> testTarihiSaati;

    @FXML
    private TableColumn<LaboratuvarTestleri, Integer> personelID;

    private ObservableList<LaboratuvarTestleri> testListesi;

    private final String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=HastaneVeriTabani;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";

    public void initialize() {
        testID.setCellValueFactory(new PropertyValueFactory<>("testID"));
        hastaID.setCellValueFactory(new PropertyValueFactory<>("hastaID"));
        testTuru.setCellValueFactory(new PropertyValueFactory<>("testTuru"));
        sonuclar.setCellValueFactory(new PropertyValueFactory<>("sonuclar"));
        testTarihiSaati.setCellValueFactory(new PropertyValueFactory<>("testTarihiSaati"));
        personelID.setCellValueFactory(new PropertyValueFactory<>("personelID"));

        testListesi = FXCollections.observableArrayList();
        tabloyuGuncelle();
    }

    @FXML
    private void testEkleButonu() {
        try {
            int hastaID = Integer.parseInt(txtHastaID.getText());
            String testTuru = txtTestTuru.getText();
            String sonuclar = txtSonuclar.getText();
            String testTarihiSaati = txtTestTarihiSaati.getText();
            int personelID = Integer.parseInt(txtPersonelID.getText());

            String query = "INSERT INTO LaboratuvarTestleri (HastaID, TestTuru, Sonuclar, TestTarihiSaati, PersonelID) VALUES (?, ?, ?, ?, ?)";
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, hastaID);
                preparedStatement.setString(2, testTuru);
                preparedStatement.setString(3, sonuclar);
                preparedStatement.setDate(4, java.sql.Date.valueOf(testTarihiSaati));
                preparedStatement.setInt(5, personelID);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    mesajGoster(Alert.AlertType.INFORMATION, "Başarılı", "Laboratuvar testi başarıyla eklendi!");
                    temizle();
                    tabloyuGuncelle();
                }
            }
        } catch (Exception e) {
            mesajGoster(Alert.AlertType.ERROR, "Hata", "Ekleme sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void testSilButonu() {
        try {
            int testID = Integer.parseInt(txtTestID.getText());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Test ID " + testID + " olan kaydı silmek istediğinizden emin misiniz?", ButtonType.YES, ButtonType.NO);
            if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                String query = "DELETE FROM LaboratuvarTestleri WHERE TestID = ?";
                try (Connection connection = DriverManager.getConnection(connectionUrl);
                     PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setInt(1, testID);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        mesajGoster(Alert.AlertType.INFORMATION, "Başarılı", "Test başarıyla silindi!");
                        temizle();
                        tabloyuGuncelle();
                    }
                }
            }
        } catch (NumberFormatException e) {
            mesajGoster(Alert.AlertType.WARNING, "Uyarı", "Geçerli bir Test ID giriniz.");
        } catch (SQLException e) {
            mesajGoster(Alert.AlertType.ERROR, "Hata", "Silme sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void testGuncelleButonu() {
        try {
            int testID = Integer.parseInt(txtTestID.getText());
            int hastaID = Integer.parseInt(txtHastaID.getText());
            String testTuru = txtTestTuru.getText();
            String sonuclar = txtSonuclar.getText();
            String testTarihiSaati = txtTestTarihiSaati.getText();
            int personelID = Integer.parseInt(txtPersonelID.getText());

            String query = "UPDATE LaboratuvarTestleri SET HastaID = ?, TestTuru = ?, Sonuclar = ?, TestTarihiSaati = ?, PersonelID = ? WHERE TestID = ?";
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, hastaID);
                preparedStatement.setString(2, testTuru);
                preparedStatement.setString(3, sonuclar);
                preparedStatement.setDate(4, java.sql.Date.valueOf(testTarihiSaati));
                preparedStatement.setInt(5, personelID);
                preparedStatement.setInt(6, testID);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    mesajGoster(Alert.AlertType.INFORMATION, "Başarılı", "Test başarıyla güncellendi!");
                    temizle();
                    tabloyuGuncelle();
                }
            }
        } catch (Exception e) {
            mesajGoster(Alert.AlertType.ERROR, "Hata", "Güncelleme sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void testAraButonu() {
        try {
            int testID = Integer.parseInt(txtTestID.getText());

            String query = "SELECT * FROM LaboratuvarTestleri WHERE TestID = ?";
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, testID);
                ResultSet resultSet = preparedStatement.executeQuery();

                testListesi.clear();
                if (resultSet.next()) {
                    testListesi.add(new LaboratuvarTestleri(
                            resultSet.getInt("TestID"),
                            resultSet.getInt("HastaID"),
                            resultSet.getString("TestTuru"),
                            resultSet.getString("Sonuclar"),
                            resultSet.getDate("TestTarihiSaati"),
                            resultSet.getInt("PersonelID")
                    ));
                }
                tableView.setItems(testListesi);
            }
        } catch (NumberFormatException e) {
            mesajGoster(Alert.AlertType.WARNING, "Uyarı", "Geçerli bir Test ID giriniz.");
        } catch (SQLException e) {
            mesajGoster(Alert.AlertType.ERROR, "Hata", "Arama sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    private void tabloyuGuncelle() {
        testListesi.clear();
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM LaboratuvarTestleri")) {

            while (resultSet.next()) {
                testListesi.add(new LaboratuvarTestleri(
                        resultSet.getInt("TestID"),
                        resultSet.getInt("HastaID"),
                        resultSet.getString("TestTuru"),
                        resultSet.getString("Sonuclar"),
                        resultSet.getDate("TestTarihiSaati"),
                        resultSet.getInt("PersonelID")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.setItems(testListesi);
    }

    private void temizle() {
        txtTestID.clear();
        txtHastaID.clear();
        txtTestTuru.clear();
        txtSonuclar.clear();
        txtTestTarihiSaati.clear();
        txtPersonelID.clear();
    }

    private void mesajGoster(Alert.AlertType alertType, String baslik, String mesaj) {
        Alert alert = new Alert(alertType);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }
}
