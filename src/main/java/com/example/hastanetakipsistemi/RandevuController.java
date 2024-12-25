package com.example.hastanetakipsistemi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class RandevuController {

    @FXML
    private TableView<Randevu> tableView;

    @FXML
    private TableColumn<Randevu, Integer> randevuID;

    @FXML
    private TableColumn<Randevu, Integer> hastaID;

    @FXML
    private TableColumn<Randevu, Integer> doktorID;

    @FXML
    private TableColumn<Randevu, Date> randevuTarihiSaati;

    @FXML
    private TableColumn<Randevu, String> durum;

    @FXML
    private TableColumn<Randevu, String> notlar;

    @FXML
    private TextField txtRandevuID, txtHastaID, txtDoktorID, txtRandevuTarihiSaati, txtDurum, txtNotlar, txtAra;

    private ObservableList<Randevu> randevuListesi;

    private final String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=HastaneVeriTabani;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";

    public void initialize() {
        randevuID.setCellValueFactory(new PropertyValueFactory<>("randevuID"));
        hastaID.setCellValueFactory(new PropertyValueFactory<>("hastaID"));
        doktorID.setCellValueFactory(new PropertyValueFactory<>("doktorID"));
        randevuTarihiSaati.setCellValueFactory(new PropertyValueFactory<>("randevuTarihiSaati"));
        durum.setCellValueFactory(new PropertyValueFactory<>("durum"));
        notlar.setCellValueFactory(new PropertyValueFactory<>("notlar"));

        randevuListesi = FXCollections.observableArrayList();
        tabloyuGuncelle();
    }

    @FXML
    private void randevuEkleButonu() {
        try {
            int hastaID = Integer.parseInt(txtHastaID.getText());
            int doktorID = Integer.parseInt(txtDoktorID.getText());
            String randevuTarihiSaati = txtRandevuTarihiSaati.getText(); // Format: YYYY-MM-DD
            String durum = txtDurum.getText();
            String notlar = txtNotlar.getText();

            String query = "INSERT INTO Randevular (HastaID, DoktorID, RandevuTarihiSaati, Durum, Notlar) VALUES (?, ?, ?, ?, ?)";
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, hastaID);
                preparedStatement.setInt(2, doktorID);
                preparedStatement.setDate(3, java.sql.Date.valueOf(randevuTarihiSaati));
                preparedStatement.setString(4, durum);
                preparedStatement.setString(5, notlar);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    mesajGoster(Alert.AlertType.INFORMATION, "Başarılı", "Randevu başarıyla eklendi!");
                    temizle();
                    tabloyuGuncelle();
                }
            }
        } catch (SQLException | NumberFormatException e) {
            mesajGoster(Alert.AlertType.ERROR, "Hata", "Randevu eklenirken bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void randevuSilButonu() {
        try {
            int randevuID = Integer.parseInt(txtRandevuID.getText());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Randevu ID " + randevuID + " olan kaydı silmek istediğinizden emin misiniz?", ButtonType.YES, ButtonType.NO);
            if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                String query = "DELETE FROM Randevular WHERE RandevuID = ?";
                try (Connection connection = DriverManager.getConnection(connectionUrl);
                     PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setInt(1, randevuID);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        mesajGoster(Alert.AlertType.INFORMATION, "Başarılı", "Randevu başarıyla silindi!");
                        temizle();
                        tabloyuGuncelle();
                    }
                }
            }
        } catch (NumberFormatException e) {
            mesajGoster(Alert.AlertType.WARNING, "Uyarı", "Geçerli bir Randevu ID giriniz.");
        } catch (SQLException e) {
            mesajGoster(Alert.AlertType.ERROR, "Hata", "Silme sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void randevuGuncelleButonu() {
        try {
            int randevuID = Integer.parseInt(txtRandevuID.getText());
            int hastaID = Integer.parseInt(txtHastaID.getText());
            int doktorID = Integer.parseInt(txtDoktorID.getText());
            String randevuTarihiSaati = txtRandevuTarihiSaati.getText();
            String durum = txtDurum.getText();
            String notlar = txtNotlar.getText();

            String query = "UPDATE Randevular SET HastaID = ?, DoktorID = ?, RandevuTarihiSaati = ?, Durum = ?, Notlar = ? WHERE RandevuID = ?";
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, hastaID);
                preparedStatement.setInt(2, doktorID);
                preparedStatement.setDate(3, java.sql.Date.valueOf(randevuTarihiSaati));
                preparedStatement.setString(4, durum);
                preparedStatement.setString(5, notlar);
                preparedStatement.setInt(6, randevuID);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    mesajGoster(Alert.AlertType.INFORMATION, "Başarılı", "Randevu başarıyla güncellendi!");
                    temizle();
                    tabloyuGuncelle();
                }
            }
        } catch (SQLException | NumberFormatException e) {
            mesajGoster(Alert.AlertType.ERROR, "Hata", "Güncelleme sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void randevuAraButonu() {
        try {
            int randevuID = Integer.parseInt(txtRandevuID.getText());
            ObservableList<Randevu> aramaSonuclari = FXCollections.observableArrayList();

            String query = "SELECT * FROM Randevular WHERE RandevuID = ?";
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, randevuID);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("RandevuID");
                    int hastaID = resultSet.getInt("HastaID");
                    int doktorID = resultSet.getInt("DoktorID");
                    Date tarih = resultSet.getDate("RandevuTarihiSaati");
                    String durum = resultSet.getString("Durum");
                    String notlar = resultSet.getString("Notlar");

                    aramaSonuclari.add(new Randevu(id, hastaID, doktorID, tarih, durum, notlar));
                }

                tableView.setItems(aramaSonuclari);
            }
        } catch (NumberFormatException e) {
            mesajGoster(Alert.AlertType.WARNING, "Uyarı", "Geçerli bir Randevu ID giriniz.");
        } catch (SQLException e) {
            mesajGoster(Alert.AlertType.ERROR, "Hata", "Arama sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    private void tabloyuGuncelle() {
        randevuListesi.clear();
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Randevular")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("RandevuID");
                int hastaID = resultSet.getInt("HastaID");
                int doktorID = resultSet.getInt("DoktorID");
                Date tarih = resultSet.getDate("RandevuTarihiSaati");
                String durum = resultSet.getString("Durum");
                String notlar = resultSet.getString("Notlar");

                randevuListesi.add(new Randevu(id, hastaID, doktorID, tarih, durum, notlar));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.setItems(randevuListesi);
    }

    private void temizle() {
        txtRandevuID.clear();
        txtHastaID.clear();
        txtDoktorID.clear();
        txtRandevuTarihiSaati.clear();
        txtDurum.clear();
        txtNotlar.clear();
    }

    private void mesajGoster(Alert.AlertType alertType, String baslik, String mesaj) {
        Alert alert = new Alert(alertType);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }
}
