package com.example.hastanetakipsistemi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class HastaKayitController {

    @FXML
    private TextField txtKayitID, txtHastaID, txtRandevuID, txtKayitTarihi, txtTani, txtTedaviID, txtRecete, txtDoktorNotlari, txtAra;

    @FXML
    private TableView<HastaKayit> tableView;

    @FXML
    private TableColumn<HastaKayit, Integer> kayitID;

    @FXML
    private TableColumn<HastaKayit, Integer> hastaID;

    @FXML
    private TableColumn<HastaKayit, Integer> randevuID;

    @FXML
    private TableColumn<HastaKayit, String> kayitTarihi;

    @FXML
    private TableColumn<HastaKayit, String> tani;

    @FXML
    private TableColumn<HastaKayit, Integer> tedaviID;

    @FXML
    private TableColumn<HastaKayit, String> recete;

    @FXML
    private TableColumn<HastaKayit, String> doktorNotlari;

    private ObservableList<HastaKayit> kayitListesi;

    private final String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=HastaneVeriTabani;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";

    public void initialize() {
        kayitID.setCellValueFactory(new PropertyValueFactory<>("kayitID"));
        hastaID.setCellValueFactory(new PropertyValueFactory<>("hastaID"));
        randevuID.setCellValueFactory(new PropertyValueFactory<>("randevuID"));
        kayitTarihi.setCellValueFactory(new PropertyValueFactory<>("kayitTarihi"));
        tani.setCellValueFactory(new PropertyValueFactory<>("tani"));
        tedaviID.setCellValueFactory(new PropertyValueFactory<>("tedaviID"));
        recete.setCellValueFactory(new PropertyValueFactory<>("recete"));
        doktorNotlari.setCellValueFactory(new PropertyValueFactory<>("doktorNotlari"));

        kayitListesi = FXCollections.observableArrayList();
        tabloyuGuncelle();
    }

    @FXML
    private void kayitEkleButonu() {
        try {
            int hastaID = Integer.parseInt(txtHastaID.getText());
            int randevuID = Integer.parseInt(txtRandevuID.getText());
            String kayitTarihi = txtKayitTarihi.getText();
            String tani = txtTani.getText();
            int tedaviID = Integer.parseInt(txtTedaviID.getText());
            String recete = txtRecete.getText();
            String doktorNotlari = txtDoktorNotlari.getText();

            String query = "INSERT INTO HastaKayitlari (HastaID, RandevuID, KayitTarihi, Tani, TedaviID, Recete, DoktorNotlari) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, hastaID);
                preparedStatement.setInt(2, randevuID);
                preparedStatement.setDate(3, java.sql.Date.valueOf(kayitTarihi));
                preparedStatement.setString(4, tani);
                preparedStatement.setInt(5, tedaviID);
                preparedStatement.setString(6, recete);
                preparedStatement.setString(7, doktorNotlari);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    mesajGoster(Alert.AlertType.INFORMATION, "Başarılı", "Hasta kaydı başarıyla eklendi!");
                    temizle();
                    tabloyuGuncelle();
                }
            }
        } catch (Exception e) {
            mesajGoster(Alert.AlertType.ERROR, "Hata", "Ekleme sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void kayitSilButonu() {
        try {
            int kayitID = Integer.parseInt(txtKayitID.getText());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Kayit ID " + kayitID + " olan kaydı silmek istediğinizden emin misiniz?", ButtonType.YES, ButtonType.NO);
            if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                String query = "DELETE FROM HastaKayitlari WHERE KayitID = ?";
                try (Connection connection = DriverManager.getConnection(connectionUrl);
                     PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setInt(1, kayitID);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        mesajGoster(Alert.AlertType.INFORMATION, "Başarılı", "Kayıt başarıyla silindi!");
                        temizle();
                        tabloyuGuncelle();
                    }
                }
            }
        } catch (NumberFormatException e) {
            mesajGoster(Alert.AlertType.WARNING, "Uyarı", "Geçerli bir Kayit ID giriniz.");
        } catch (SQLException e) {
            mesajGoster(Alert.AlertType.ERROR, "Hata", "Silme sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void kayitGuncelleButonu() {
        try {
            int kayitID = Integer.parseInt(txtKayitID.getText());
            int hastaID = Integer.parseInt(txtHastaID.getText());
            int randevuID = Integer.parseInt(txtRandevuID.getText());
            String kayitTarihi = txtKayitTarihi.getText();
            String tani = txtTani.getText();
            int tedaviID = Integer.parseInt(txtTedaviID.getText());
            String recete = txtRecete.getText();
            String doktorNotlari = txtDoktorNotlari.getText();

            String query = "UPDATE HastaKayitlari SET HastaID = ?, RandevuID = ?, KayitTarihi = ?, Tani = ?, TedaviID = ?, Recete = ?, DoktorNotlari = ? WHERE KayitID = ?";
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, hastaID);
                preparedStatement.setInt(2, randevuID);
                preparedStatement.setDate(3, java.sql.Date.valueOf(kayitTarihi));
                preparedStatement.setString(4, tani);
                preparedStatement.setInt(5, tedaviID);
                preparedStatement.setString(6, recete);
                preparedStatement.setString(7, doktorNotlari);
                preparedStatement.setInt(8, kayitID);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    mesajGoster(Alert.AlertType.INFORMATION, "Başarılı", "Kayıt başarıyla güncellendi!");
                    temizle();
                    tabloyuGuncelle();
                }
            }
        } catch (Exception e) {
            mesajGoster(Alert.AlertType.ERROR, "Hata", "Güncelleme sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void kayitAraButonu() {
        try {
            int kayitID = Integer.parseInt(txtKayitID.getText());

            String query = "SELECT * FROM HastaKayitlari WHERE KayitID = ?";
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, kayitID);
                ResultSet resultSet = preparedStatement.executeQuery();

                kayitListesi.clear();
                if (resultSet.next()) {
                    kayitListesi.add(new HastaKayit(
                            resultSet.getInt("KayitID"),
                            resultSet.getInt("HastaID"),
                            resultSet.getInt("RandevuID"),
                            resultSet.getDate("KayitTarihi"),
                            resultSet.getString("Tani"),
                            resultSet.getInt("TedaviID"),
                            resultSet.getString("Recete"),
                            resultSet.getString("DoktorNotlari")
                    ));
                }
                tableView.setItems(kayitListesi);
            }
        } catch (NumberFormatException e) {
            mesajGoster(Alert.AlertType.WARNING, "Uyarı", "Geçerli bir Kayit ID giriniz.");
        } catch (SQLException e) {
            mesajGoster(Alert.AlertType.ERROR, "Hata", "Arama sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    private void tabloyuGuncelle() {
        kayitListesi.clear();
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM HastaKayitlari")) {

            while (resultSet.next()) {
                kayitListesi.add(new HastaKayit(
                        resultSet.getInt("KayitID"),
                        resultSet.getInt("HastaID"),
                        resultSet.getInt("RandevuID"),
                        resultSet.getDate("KayitTarihi"),
                        resultSet.getString("Tani"),
                        resultSet.getInt("TedaviID"),
                        resultSet.getString("Recete"),
                        resultSet.getString("DoktorNotlari")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.setItems(kayitListesi);
    }

    private void temizle() {
        txtKayitID.clear();
        txtHastaID.clear();
        txtRandevuID.clear();
        txtKayitTarihi.clear();
        txtTani.clear();
        txtTedaviID.clear();
        txtRecete.clear();
        txtDoktorNotlari.clear();
    }

    private void mesajGoster(Alert.AlertType alertType, String baslik, String mesaj) {
        Alert alert = new Alert(alertType);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }
}
