package com.example.hastanetakipsistemi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class TedaviController {

    @FXML
    private TableView<Tedavi> tableView;

    @FXML
    private TableColumn<Tedavi, Integer> tedaviID;

    @FXML
    private TableColumn<Tedavi, String> ad;

    @FXML
    private TableColumn<Tedavi, String> aciklama;

    @FXML
    private TableColumn<Tedavi, Double> ucret;

    @FXML
    private TextField txtTedaviID, txtAd, txtAciklama, txtUcret, txtAra;

    private ObservableList<Tedavi> tedaviListesi;

    private final String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=HastaneVeriTabani;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";

    public void initialize() {
        tedaviID.setCellValueFactory(new PropertyValueFactory<>("tedaviID"));
        ad.setCellValueFactory(new PropertyValueFactory<>("ad"));
        aciklama.setCellValueFactory(new PropertyValueFactory<>("aciklama"));
        ucret.setCellValueFactory(new PropertyValueFactory<>("ucret"));

        tedaviListesi = FXCollections.observableArrayList();
        tabloyuGuncelle();
    }

    @FXML
    private void tedaviEkleButonu() {
        try {
            String ad = txtAd.getText();
            String aciklama = txtAciklama.getText();
            double ucret = Double.parseDouble(txtUcret.getText());

            String query = "INSERT INTO Tedaviler (Ad, Aciklama, Ucret) VALUES (?, ?, ?)";
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, ad);
                preparedStatement.setString(2, aciklama);
                preparedStatement.setDouble(3, ucret);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    mesajGoster(Alert.AlertType.INFORMATION, "Başarılı", "Tedavi başarıyla eklendi!");
                    temizle();
                    tabloyuGuncelle();
                }
            }
        } catch (NumberFormatException e) {
            mesajGoster(Alert.AlertType.WARNING, "Uyarı", "Geçerli bir ücret giriniz.");
        } catch (SQLException e) {
            mesajGoster(Alert.AlertType.ERROR, "Hata", "Ekleme sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void tedaviSilButonu() {
        try {
            int tedaviID = Integer.parseInt(txtTedaviID.getText());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Tedavi ID " + tedaviID + " olan kaydı silmek istediğinizden emin misiniz?", ButtonType.YES, ButtonType.NO);
            if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                String query = "DELETE FROM Tedaviler WHERE TedaviID = ?";
                try (Connection connection = DriverManager.getConnection(connectionUrl);
                     PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setInt(1, tedaviID);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        mesajGoster(Alert.AlertType.INFORMATION, "Başarılı", "Tedavi başarıyla silindi!");
                        temizle();
                        tabloyuGuncelle();
                    }
                }
            }
        } catch (NumberFormatException e) {
            mesajGoster(Alert.AlertType.WARNING, "Uyarı", "Geçerli bir Tedavi ID giriniz.");
        } catch (SQLException e) {
            mesajGoster(Alert.AlertType.ERROR, "Hata", "Silme sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void tedaviGuncelleButonu() {
        try {
            int tedaviID = Integer.parseInt(txtTedaviID.getText());
            String ad = txtAd.getText();
            String aciklama = txtAciklama.getText();
            double ucret = Double.parseDouble(txtUcret.getText());

            String query = "UPDATE Tedaviler SET Ad = ?, Aciklama = ?, Ucret = ? WHERE TedaviID = ?";
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, ad);
                preparedStatement.setString(2, aciklama);
                preparedStatement.setDouble(3, ucret);
                preparedStatement.setInt(4, tedaviID);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    mesajGoster(Alert.AlertType.INFORMATION, "Başarılı", "Tedavi başarıyla güncellendi!");
                    temizle();
                    tabloyuGuncelle();
                }
            }
        } catch (NumberFormatException e) {
            mesajGoster(Alert.AlertType.WARNING, "Uyarı", "Geçerli bir ücret veya Tedavi ID giriniz.");
        } catch (SQLException e) {
            mesajGoster(Alert.AlertType.ERROR, "Hata", "Güncelleme sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void tedaviAraButonu() {
        try {
            int tedaviID = Integer.parseInt(txtTedaviID.getText());
            ObservableList<Tedavi> aramaSonuclari = FXCollections.observableArrayList();

            String query = "SELECT * FROM Tedaviler WHERE TedaviID = ?";
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, tedaviID);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("TedaviID");
                    String ad = resultSet.getString("Ad");
                    String aciklama = resultSet.getString("Aciklama");
                    double ucret = resultSet.getDouble("Ucret");

                    aramaSonuclari.add(new Tedavi(id, ad, aciklama, ucret));
                }

                tableView.setItems(aramaSonuclari);
            }
        } catch (NumberFormatException e) {
            mesajGoster(Alert.AlertType.WARNING, "Uyarı", "Geçerli bir Tedavi ID giriniz.");
        } catch (SQLException e) {
            mesajGoster(Alert.AlertType.ERROR, "Hata", "Arama sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    private void tabloyuGuncelle() {
        tedaviListesi.clear();
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Tedaviler")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("TedaviID");
                String ad = resultSet.getString("Ad");
                String aciklama = resultSet.getString("Aciklama");
                double ucret = resultSet.getDouble("Ucret");

                tedaviListesi.add(new Tedavi(id, ad, aciklama, ucret));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.setItems(tedaviListesi);
    }

    private void temizle() {
        txtTedaviID.clear();
        txtAd.clear();
        txtAciklama.clear();
        txtUcret.clear();
    }

    private void mesajGoster(Alert.AlertType alertType, String baslik, String mesaj) {
        Alert alert = new Alert(alertType);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }
}
