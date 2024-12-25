package com.example.hastanetakipsistemi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class PersonelController {

    @FXML
    private TableView<Personel> tableView;

    @FXML
    private TableColumn<Personel, Integer> personelID;

    @FXML
    private TableColumn<Personel, String> ad;

    @FXML
    private TableColumn<Personel, String> soyad;

    @FXML
    private TableColumn<Personel, String> gorev;

    @FXML
    private TableColumn<Personel, String> telefon;

    @FXML
    private TableColumn<Personel, Integer> bolumID;

    @FXML
    private TextField txtPersonelID, txtAd, txtSoyad, txtGorev, txtTelefon, txtBolumID, txtAra;

    private ObservableList<Personel> personelListesi;

    private final String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=HastaneVeriTabani;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";

    public void initialize() {
        personelID.setCellValueFactory(new PropertyValueFactory<>("personelID"));
        ad.setCellValueFactory(new PropertyValueFactory<>("ad"));
        soyad.setCellValueFactory(new PropertyValueFactory<>("soyad"));
        gorev.setCellValueFactory(new PropertyValueFactory<>("gorev"));
        telefon.setCellValueFactory(new PropertyValueFactory<>("telefon"));
        bolumID.setCellValueFactory(new PropertyValueFactory<>("bolumID"));

        personelListesi = FXCollections.observableArrayList();
        tabloyuGuncelle();
    }

    @FXML
    private void personelEkleButonu() {
        try {
            String ad = txtAd.getText();
            String soyad = txtSoyad.getText();
            String gorev = txtGorev.getText();
            String telefon = txtTelefon.getText();
            int bolumID = Integer.parseInt(txtBolumID.getText());

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
                    mesajGoster(Alert.AlertType.INFORMATION, "Başarılı", "Personel başarıyla eklendi!");
                    temizle();
                    tabloyuGuncelle();
                }
            }
        } catch (SQLException | NumberFormatException e) {
            mesajGoster(Alert.AlertType.ERROR, "Hata", "Personel eklenirken bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void personelSilButonu() {
        try {
            int personelID = Integer.parseInt(txtPersonelID.getText());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Personel ID " + personelID + " olan kaydı silmek istediğinizden emin misiniz?", ButtonType.YES, ButtonType.NO);
            if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                String query = "DELETE FROM Personel WHERE PersonelID = ?";
                try (Connection connection = DriverManager.getConnection(connectionUrl);
                     PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setInt(1, personelID);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        mesajGoster(Alert.AlertType.INFORMATION, "Başarılı", "Personel başarıyla silindi!");
                        temizle();
                        tabloyuGuncelle();
                    }
                }
            }
        } catch (NumberFormatException e) {
            mesajGoster(Alert.AlertType.WARNING, "Uyarı", "Geçerli bir Personel ID giriniz.");
        } catch (SQLException e) {
            mesajGoster(Alert.AlertType.ERROR, "Hata", "Silme sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void personelGuncelleButonu() {
        try {
            int personelID = Integer.parseInt(txtPersonelID.getText());
            String ad = txtAd.getText();
            String soyad = txtSoyad.getText();
            String gorev = txtGorev.getText();
            String telefon = txtTelefon.getText();
            int bolumID = Integer.parseInt(txtBolumID.getText());

            String query = "UPDATE Personel SET Ad = ?, Soyad = ?, Gorev = ?, Telefon = ?, BolumID = ? WHERE PersonelID = ?";
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, ad);
                preparedStatement.setString(2, soyad);
                preparedStatement.setString(3, gorev);
                preparedStatement.setString(4, telefon);
                preparedStatement.setInt(5, bolumID);
                preparedStatement.setInt(6, personelID);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    mesajGoster(Alert.AlertType.INFORMATION, "Başarılı", "Personel başarıyla güncellendi!");
                    temizle();
                    tabloyuGuncelle();
                }
            }
        } catch (SQLException | NumberFormatException e) {
            mesajGoster(Alert.AlertType.ERROR, "Hata", "Güncelleme sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void personelAraButonu() {
        try {
            int personelID = Integer.parseInt(txtPersonelID.getText());
            ObservableList<Personel> aramaSonuclari = FXCollections.observableArrayList();

            String query = "SELECT * FROM Personel WHERE PersonelID = ?";
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, personelID);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("PersonelID");
                    String ad = resultSet.getString("Ad");
                    String soyad = resultSet.getString("Soyad");
                    String gorev = resultSet.getString("Gorev");
                    String telefon = resultSet.getString("Telefon");
                    int bolumID = resultSet.getInt("BolumID");

                    aramaSonuclari.add(new Personel(id, ad, soyad, gorev, telefon, bolumID));
                }

                tableView.setItems(aramaSonuclari);
            }
        } catch (NumberFormatException e) {
            mesajGoster(Alert.AlertType.WARNING, "Uyarı", "Geçerli bir Personel ID giriniz.");
        } catch (SQLException e) {
            mesajGoster(Alert.AlertType.ERROR, "Hata", "Arama sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    private void tabloyuGuncelle() {
        personelListesi.clear();
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Personel")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("PersonelID");
                String ad = resultSet.getString("Ad");
                String soyad = resultSet.getString("Soyad");
                String gorev = resultSet.getString("Gorev");
                String telefon = resultSet.getString("Telefon");
                int bolumID = resultSet.getInt("BolumID");

                personelListesi.add(new Personel(id, ad, soyad, gorev, telefon, bolumID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.setItems(personelListesi);
    }

    private void temizle() {
        txtPersonelID.clear();
        txtAd.clear();
        txtSoyad.clear();
        txtGorev.clear();
        txtTelefon.clear();
        txtBolumID.clear();
    }

    private void mesajGoster(Alert.AlertType alertType, String baslik, String mesaj) {
        Alert alert = new Alert(alertType);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }
}
