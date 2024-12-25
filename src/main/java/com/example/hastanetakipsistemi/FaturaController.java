package com.example.hastanetakipsistemi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class FaturaController {

    @FXML
    private TextField txtFaturaID, txtHastaID, txtKayitID, txtFaturaTarihi, txtToplamTutar, txtOdemeDurumu, txtAra;

    @FXML
    private TableView<Fatura> tableView;

    @FXML
    private TableColumn<Fatura, Integer> faturaID;

    @FXML
    private TableColumn<Fatura, Integer> hastaID;

    @FXML
    private TableColumn<Fatura, Integer> kayitID;

    @FXML
    private TableColumn<Fatura, String> faturaTarihi;

    @FXML
    private TableColumn<Fatura, Double> toplamTutar;

    @FXML
    private TableColumn<Fatura, String> odemeDurumu;

    private ObservableList<Fatura> faturaListesi;

    private final String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=HastaneVeriTabani;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";

    public void initialize() {
        faturaID.setCellValueFactory(new PropertyValueFactory<>("faturaID"));
        hastaID.setCellValueFactory(new PropertyValueFactory<>("hastaID"));
        kayitID.setCellValueFactory(new PropertyValueFactory<>("kayitID"));
        faturaTarihi.setCellValueFactory(new PropertyValueFactory<>("faturaTarihi"));
        toplamTutar.setCellValueFactory(new PropertyValueFactory<>("toplamTutar"));
        odemeDurumu.setCellValueFactory(new PropertyValueFactory<>("odemeDurumu"));

        faturaListesi = FXCollections.observableArrayList();
        tabloyuGuncelle();
    }

    @FXML
    private void faturaEkleButonu() {
        try {
            int faturaID = Integer.parseInt(txtFaturaID.getText());
            int hastaID = Integer.parseInt(txtHastaID.getText());
            int kayitID = Integer.parseInt(txtKayitID.getText());
            String faturaTarihi = txtFaturaTarihi.getText();
            double toplamTutar = Double.parseDouble(txtToplamTutar.getText());
            String odemeDurumu = txtOdemeDurumu.getText();

            String query = "INSERT INTO Faturalar (FaturaID, HastaID, KayitID, FaturaTarihi, ToplamTutar, OdemeDurumu) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, faturaID);
                preparedStatement.setInt(2, hastaID);
                preparedStatement.setInt(3, kayitID);
                preparedStatement.setDate(4, java.sql.Date.valueOf(faturaTarihi));
                preparedStatement.setDouble(5, toplamTutar);
                preparedStatement.setString(6, odemeDurumu);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    mesajGoster(Alert.AlertType.INFORMATION, "Başarılı", "Fatura başarıyla eklendi!");
                    temizle();
                    tabloyuGuncelle();
                }
            }
        } catch (Exception e) {
            mesajGoster(Alert.AlertType.ERROR, "Hata", "Ekleme işlemi sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void faturaSilButonu() {
        try {
            int faturaID = Integer.parseInt(txtFaturaID.getText());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Fatura ID " + faturaID + " olan kaydı silmek istediğinizden emin misiniz?", ButtonType.YES, ButtonType.NO);
            if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                String query = "DELETE FROM Faturalar WHERE FaturaID = ?";
                try (Connection connection = DriverManager.getConnection(connectionUrl);
                     PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setInt(1, faturaID);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        mesajGoster(Alert.AlertType.INFORMATION, "Başarılı", "Fatura başarıyla silindi!");
                        temizle();
                        tabloyuGuncelle();
                    }
                }
            }
        } catch (NumberFormatException e) {
            mesajGoster(Alert.AlertType.WARNING, "Uyarı", "Geçerli bir Fatura ID giriniz.");
        } catch (SQLException e) {
            mesajGoster(Alert.AlertType.ERROR, "Hata", "Silme işlemi sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void faturaGuncelleButonu() {
        try {
            int faturaID = Integer.parseInt(txtFaturaID.getText());
            int hastaID = Integer.parseInt(txtHastaID.getText());
            int kayitID = Integer.parseInt(txtKayitID.getText());
            String faturaTarihi = txtFaturaTarihi.getText();
            double toplamTutar = Double.parseDouble(txtToplamTutar.getText());
            String odemeDurumu = txtOdemeDurumu.getText();

            String query = "UPDATE Faturalar SET HastaID = ?, KayitID = ?, FaturaTarihi = ?, ToplamTutar = ?, OdemeDurumu = ? WHERE FaturaID = ?";
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, hastaID);
                preparedStatement.setInt(2, kayitID);
                preparedStatement.setDate(3, java.sql.Date.valueOf(faturaTarihi));
                preparedStatement.setDouble(4, toplamTutar);
                preparedStatement.setString(5, odemeDurumu);
                preparedStatement.setInt(6, faturaID);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    mesajGoster(Alert.AlertType.INFORMATION, "Başarılı", "Fatura başarıyla güncellendi!");
                    temizle();
                    tabloyuGuncelle();
                }
            }
        } catch (NumberFormatException e) {
            mesajGoster(Alert.AlertType.WARNING, "Uyarı", "Lütfen tüm alanları doldurun!");
        } catch (SQLException e) {
            mesajGoster(Alert.AlertType.ERROR, "Hata", "Güncelleme işlemi sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void faturaAraButonu() {
        try {
            int faturaID = Integer.parseInt(txtFaturaID.getText());

            String query = "SELECT * FROM Faturalar WHERE FaturaID = ?";
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, faturaID);
                ResultSet resultSet = preparedStatement.executeQuery();

                faturaListesi.clear();
                if (resultSet.next()) {
                    faturaListesi.add(new Fatura(
                            resultSet.getInt("FaturaID"),
                            resultSet.getInt("HastaID"),
                            resultSet.getInt("KayitID"),
                            resultSet.getDate("FaturaTarihi"),
                            resultSet.getDouble("ToplamTutar"),
                            resultSet.getString("OdemeDurumu")
                    ));
                }
                tableView.setItems(faturaListesi);
            }
        } catch (NumberFormatException e) {
            mesajGoster(Alert.AlertType.WARNING, "Uyarı", "Geçerli bir Fatura ID giriniz.");
        } catch (SQLException e) {
            mesajGoster(Alert.AlertType.ERROR, "Hata", "Arama işlemi sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    private void tabloyuGuncelle() {
        faturaListesi.clear();
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Faturalar")) {

            while (resultSet.next()) {
                faturaListesi.add(new Fatura(
                        resultSet.getInt("FaturaID"),
                        resultSet.getInt("HastaID"),
                        resultSet.getInt("KayitID"),
                        resultSet.getDate("FaturaTarihi"),
                        resultSet.getDouble("ToplamTutar"),
                        resultSet.getString("OdemeDurumu")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.setItems(faturaListesi);
    }

    private void temizle() {
        txtFaturaID.clear();
        txtHastaID.clear();
        txtKayitID.clear();
        txtFaturaTarihi.clear();
        txtToplamTutar.clear();
        txtOdemeDurumu.clear();
    }

    private void mesajGoster(Alert.AlertType alertType, String baslik, String mesaj) {
        Alert alert = new Alert(alertType);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }
}
