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

public class DoktorController {

    @FXML
    private TextField txtDoktorID, txtAd, txtSoyad, txtUzmanlikAlani, txtTcKimlikNo, txtTelefon, txtEPosta, txtAra;

    @FXML
    private TableView<Doktor> tableView;

    @FXML
    private TableColumn<Doktor, Integer> doktorID;

    @FXML
    private TableColumn<Doktor, String> ad;

    @FXML
    private TableColumn<Doktor, String> soyad;

    @FXML
    private TableColumn<Doktor, String> uzmanlikAlani;

    @FXML
    private TableColumn<Doktor, String> tcKimlikNo;

    @FXML
    private TableColumn<Doktor, String> telefon;

    @FXML
    private TableColumn<Doktor, String> ePosta;

    private ObservableList<Doktor> doktorListesi;

    private final String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=HastaneVeriTabani;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";

    public void initialize() {
        doktorID.setCellValueFactory(new PropertyValueFactory<>("doktorID"));
        ad.setCellValueFactory(new PropertyValueFactory<>("ad"));
        soyad.setCellValueFactory(new PropertyValueFactory<>("soyad"));
        uzmanlikAlani.setCellValueFactory(new PropertyValueFactory<>("uzmanlikAlani"));
        tcKimlikNo.setCellValueFactory(new PropertyValueFactory<>("tcKimlikNo"));
        telefon.setCellValueFactory(new PropertyValueFactory<>("telefon"));
        ePosta.setCellValueFactory(new PropertyValueFactory<>("ePosta"));

        doktorListesi = FXCollections.observableArrayList();
        tabloyuGuncelle();
    }

    @FXML
    private void doktorEkleButonu() {
        String ad = txtAd.getText();
        String soyad = txtSoyad.getText();
        String uzmanlikAlani = txtUzmanlikAlani.getText();
        String tcKimlikNo = txtTcKimlikNo.getText();
        String telefon = txtTelefon.getText();
        String ePosta = txtEPosta.getText();

        if (doktorEkle(ad, soyad, uzmanlikAlani, tcKimlikNo, telefon, ePosta)) {
            txtAd.clear();
            txtSoyad.clear();
            txtUzmanlikAlani.clear();
            txtTcKimlikNo.clear();
            txtTelefon.clear();
            txtEPosta.clear();
            tabloyuGuncelle();
        }
    }

    private boolean doktorEkle(String ad, String soyad, String uzmanlikAlani, String tcKimlikNo, String telefon, String ePosta) {
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
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Başarılı");
                alert.setHeaderText("Kayıt Eklendi");
                alert.setContentText("Doktor başarıyla eklendi!");
                alert.showAndWait();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @FXML
    private void doktorSilButonu() {
        String doktorIDText = txtDoktorID.getText();

        if (doktorIDText.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Uyarı");
            alert.setHeaderText("Geçersiz Giriş");
            alert.setContentText("Lütfen bir DoktorID giriniz.");
            alert.showAndWait();
            return;
        }

        try {
            int doktorID = Integer.parseInt(doktorIDText);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Silme İşlemi");
            alert.setHeaderText("Doktor Silme");
            alert.setContentText("DoktorID " + doktorID + " olan kaydı silmek istediğinize emin misiniz?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                String query = "DELETE FROM Doktorlar WHERE DoktorID = ?";
                try (Connection connection = DriverManager.getConnection(connectionUrl);
                     PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setInt(1, doktorID);
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Başarılı");
                        successAlert.setHeaderText("Silme İşlemi Tamamlandı");
                        successAlert.setContentText("Doktor başarıyla silindi!");
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
            alert.setContentText("Lütfen geçerli bir DoktorID giriniz.");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void doktorAraButonu() {
        try {
            int arananID = Integer.parseInt(txtDoktorID.getText());

            String query = "SELECT * FROM Doktorlar WHERE DoktorID = ?";
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, arananID);
                ResultSet resultSet = preparedStatement.executeQuery();

                ObservableList<Doktor> aramaSonuclari = FXCollections.observableArrayList();

                if (resultSet.next()) {
                    int doktorID = resultSet.getInt("DoktorID");
                    String ad = resultSet.getString("Ad");
                    String soyad = resultSet.getString("Soyad");
                    String uzmanlikAlani = resultSet.getString("UzmanlikAlani");
                    String tcKimlikNo = resultSet.getString("TcKimlikNo");
                    String telefon = resultSet.getString("Telefon");
                    String ePosta = resultSet.getString("EPosta");

                    Doktor doktor = new Doktor(doktorID, ad, soyad, uzmanlikAlani, tcKimlikNo, telefon, ePosta);
                    aramaSonuclari.add(doktor);
                }

                tableView.setItems(aramaSonuclari);
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hata");
            alert.setHeaderText("Geçersiz Giriş");
            alert.setContentText("Lütfen geçerli bir DoktorID giriniz.");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void doktorGuncelleButonu() {
        try {
            int doktorID = Integer.parseInt(txtDoktorID.getText());
            String ad = txtAd.getText();
            String soyad = txtSoyad.getText();
            String uzmanlikAlani = txtUzmanlikAlani.getText();
            String tcKimlikNo = txtTcKimlikNo.getText();
            String telefon = txtTelefon.getText();
            String ePosta = txtEPosta.getText();

            String query = "UPDATE Doktorlar SET Ad = ?, Soyad = ?, UzmanlikAlani = ?, TcKimlikNo = ?, Telefon = ?, EPosta = ? WHERE DoktorID = ?";
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, ad);
                preparedStatement.setString(2, soyad);
                preparedStatement.setString(3, uzmanlikAlani);
                preparedStatement.setString(4, tcKimlikNo);
                preparedStatement.setString(5, telefon);
                preparedStatement.setString(6, ePosta);
                preparedStatement.setInt(7, doktorID);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Başarılı");
                    alert.setHeaderText("Güncelleme Tamamlandı");
                    alert.setContentText("Doktor başarıyla güncellendi!");
                    alert.showAndWait();

                    tabloyuGuncelle();
                    temizle();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Başarısız");
                    alert.setHeaderText("Güncelleme Başarısız");
                    alert.setContentText("Belirtilen DoktorID bulunamadı.");
                    alert.showAndWait();
                }
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hata");
            alert.setHeaderText("Geçersiz Giriş");
            alert.setContentText("Lütfen geçerli bir DoktorID giriniz.");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void tabloyuGuncelle() {
        doktorListesi.clear();
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Doktorlar")) {

            while (resultSet.next()) {
                int doktorID = resultSet.getInt("DoktorID");
                String ad = resultSet.getString("Ad");
                String soyad = resultSet.getString("Soyad");
                String uzmanlikAlani = resultSet.getString("UzmanlikAlani");
                String tcKimlikNo = resultSet.getString("TcKimlikNo");
                String telefon = resultSet.getString("Telefon");
                String ePosta = resultSet.getString("EPosta");

                Doktor doktor = new Doktor(doktorID, ad, soyad, uzmanlikAlani, tcKimlikNo, telefon, ePosta);
                doktorListesi.add(doktor);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.setItems(doktorListesi);
    }

    private void temizle() {
        txtDoktorID.clear();
        txtAd.clear();
        txtSoyad.clear();
        txtUzmanlikAlani.clear();
        txtTcKimlikNo.clear();
        txtTelefon.clear();
        txtEPosta.clear();
    }
}
