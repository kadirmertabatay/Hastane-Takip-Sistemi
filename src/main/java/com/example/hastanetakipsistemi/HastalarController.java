package com.example.hastanetakipsistemi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HastalarController {

    @FXML
    private TableView<Hasta> tableView;

    @FXML
    private TableColumn<Hasta, Integer> HastaID;

    @FXML
    private TableColumn<Hasta, String> Ad;

    @FXML
    private TableColumn<Hasta, String> Soyad;

    @FXML
    private TableColumn<Hasta, String> TCKimlikNo;

    @FXML
    private TableColumn<Hasta, Date> DogumTarihi;

    @FXML
    private TableColumn<Hasta, String> Cinsiyet;

    @FXML
    private TableColumn<Hasta, String> Adres;

    @FXML
    private TableColumn<Hasta, String> Telefon;

    @FXML
    private TableColumn<Hasta, String> EPosta;

    @FXML
    private TableColumn<Hasta, String> SigortaBilgisi;

    @FXML
    private TextField txtHastaID, txtAd, txtSoyad, txtTCKimlikNo, txtDogumTarihi, txtCinsiyet, txtAdres, txtTelefon, txtEPosta, txtSigortaBilgisi, txtAra;

    private ObservableList<Hasta> hastaListesi;

    private final String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=HastaneVeriTabani;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";

    public void initialize() {
        // Tablo sütunlarını Hasta sınıfının özelliklerine bağlama
        HastaID.setCellValueFactory(new PropertyValueFactory<>("id"));
        Ad.setCellValueFactory(new PropertyValueFactory<>("ad"));
        Soyad.setCellValueFactory(new PropertyValueFactory<>("soyad"));
        TCKimlikNo.setCellValueFactory(new PropertyValueFactory<>("tcKimlikNo"));
        DogumTarihi.setCellValueFactory(new PropertyValueFactory<>("dogumTarihi"));
        Cinsiyet.setCellValueFactory(new PropertyValueFactory<>("cinsiyet"));
        Adres.setCellValueFactory(new PropertyValueFactory<>("adres"));
        Telefon.setCellValueFactory(new PropertyValueFactory<>("telefon"));
        EPosta.setCellValueFactory(new PropertyValueFactory<>("eposta"));
        SigortaBilgisi.setCellValueFactory(new PropertyValueFactory<>("sigortaBilgisi"));

        // Verileri tabloya yükleme
        hastaListesi = FXCollections.observableArrayList();
        verileriGetir();
        tableView.setItems(hastaListesi);
    }

    private void verileriGetir() {
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             java.sql.Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Hastalar")) {

            hastaListesi.clear(); // Listeyi sıfırla
            while (resultSet.next()) {
                int id = resultSet.getInt("HastaID");
                String ad = resultSet.getString("Ad");
                String soyad = resultSet.getString("Soyad");
                String tcKimlikNo = resultSet.getString("TCKimlikNo");
                Date dogumTarihi = resultSet.getDate("DogumTarihi");
                String cinsiyet = resultSet.getString("Cinsiyet");
                String adres = resultSet.getString("Adres");
                String telefon = resultSet.getString("Telefon");
                String eposta = resultSet.getString("EPosta");
                String sigortaBilgisi = resultSet.getString("SigortaBilgisi");

                Hasta hasta = new Hasta(id, ad, soyad, tcKimlikNo, dogumTarihi, cinsiyet, adres, telefon, eposta, sigortaBilgisi);
                hastaListesi.add(hasta);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void hastaEkleButonu() {
        String ad = txtAd.getText();
        String soyad = txtSoyad.getText();
        String tcKimlikNo = txtTCKimlikNo.getText();
        String dogumTarihi = txtDogumTarihi.getText(); // Format: YYYY-MM-DD
        String cinsiyet = txtCinsiyet.getText();
        String adres = txtAdres.getText();
        String telefon = txtTelefon.getText();
        String eposta = txtEPosta.getText();
        String sigortaBilgisi = txtSigortaBilgisi.getText();

        String query = "INSERT INTO Hastalar (Ad, Soyad, TCKimlikNo, DogumTarihi, Cinsiyet, Adres, Telefon, EPosta, SigortaBilgisi) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, ad);
            preparedStatement.setString(2, soyad);
            preparedStatement.setString(3, tcKimlikNo);
            preparedStatement.setDate(4, java.sql.Date.valueOf(dogumTarihi));
            preparedStatement.setString(5, cinsiyet);
            preparedStatement.setString(6, adres);
            preparedStatement.setString(7, telefon);
            preparedStatement.setString(8, eposta);
            preparedStatement.setString(9, sigortaBilgisi);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                // Başarı mesajını GUI üzerinden göster
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Başarılı");
                alert.setHeaderText("Kayıt Eklendi");
                alert.setContentText("Hasta başarıyla eklendi!");
                alert.showAndWait();

                verileriGetir(); // Tabloyu güncelle
                temizle();
            }
        } catch (SQLException e) {
            // Hata mesajını GUI üzerinden göster
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hata");
            alert.setHeaderText("Veritabanı Hatası");
            alert.setContentText("Kayıt eklenirken bir hata oluştu: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }



    @FXML
    private void hastaAraButonu() {
        try {
            int arananID = Integer.parseInt(txtHastaID.getText());

            String query = "SELECT * FROM Hastalar WHERE HastaID = ?";
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, arananID);
                ResultSet resultSet = preparedStatement.executeQuery();

                ObservableList<Hasta> aramaSonuclari = FXCollections.observableArrayList();

                if (resultSet.next()) {
                    int id = resultSet.getInt("HastaID");
                    String ad = resultSet.getString("Ad");
                    String soyad = resultSet.getString("Soyad");
                    String tcKimlikNo = resultSet.getString("TCKimlikNo");
                    Date dogumTarihi = resultSet.getDate("DogumTarihi");
                    String cinsiyet = resultSet.getString("Cinsiyet");
                    String adres = resultSet.getString("Adres");
                    String telefon = resultSet.getString("Telefon");
                    String eposta = resultSet.getString("EPosta");
                    String sigortaBilgisi = resultSet.getString("SigortaBilgisi");

                    Hasta hasta = new Hasta(id, ad, soyad, tcKimlikNo, dogumTarihi, cinsiyet, adres, telefon, eposta, sigortaBilgisi);
                    aramaSonuclari.add(hasta);
                }

                tableView.setItems(aramaSonuclari);
            }
        } catch (NumberFormatException e) {
            System.err.println("Geçerli bir ID giriniz.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void hastaSilButonu() {
        // HastaID'yi TextField'dan al
        String hastaIDText = txtHastaID.getText();

        // ID kontrolü
        if (hastaIDText.isEmpty()) {
            System.out.println("Lütfen bir HastaID giriniz.");
            return;
        }

        try {
            int hastaID = Integer.parseInt(hastaIDText);

            // Kullanıcıdan onay al
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Silme İşlemi");
            alert.setHeaderText("Hasta Silme");
            alert.setContentText("HastaID " + hastaID + " olan kaydı silmek istediğinize emin misiniz?");

            // Onay kutusunun cevabını bekle
            if (alert.showAndWait().get() == ButtonType.OK) {
                // Silme sorgusunu hazırla ve çalıştır
                String query = "DELETE FROM Hastalar WHERE HastaID = ?";
                try (Connection connection = DriverManager.getConnection(connectionUrl);
                     PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setInt(1, hastaID);
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Hasta başarıyla silindi!");
                        verileriGetir(); // Tabloyu güncelle
                        temizle();
                    } else {
                        System.out.println("Silme işlemi başarısız. Belirtilen ID bulunamadı.");
                    }
                }
            } else {
                System.out.println("Silme işlemi iptal edildi.");
            }

        } catch (NumberFormatException e) {
            System.err.println("Geçerli bir HastaID giriniz.");
        } catch (SQLException e) {
            System.err.println("Silme işlemi sırasında bir hata oluştu: " + e.getMessage());
            if (e.getMessage().contains("REFERENCE constraint")) {
                System.out.println("Bu hasta, başka bir tabloda referanslanıyor. Lütfen ilişkili kayıtları kontrol edin.");
            }
        }
    }




    @FXML
    private void hastaGuncelleButonu() {
        try {
            int hastaID = Integer.parseInt(txtHastaID.getText());
            String ad = txtAd.getText();
            String soyad = txtSoyad.getText();
            String tcKimlikNo = txtTCKimlikNo.getText();
            String dogumTarihi = txtDogumTarihi.getText(); // Format: YYYY-MM-DD
            String cinsiyet = txtCinsiyet.getText();
            String adres = txtAdres.getText();
            String telefon = txtTelefon.getText();
            String eposta = txtEPosta.getText();
            String sigortaBilgisi = txtSigortaBilgisi.getText();

            String query = "UPDATE Hastalar SET Ad = ?, Soyad = ?, TCKimlikNo = ?, DogumTarihi = ?, Cinsiyet = ?, Adres = ?, Telefon = ?, EPosta = ?, SigortaBilgisi = ? WHERE HastaID = ?";
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, ad);
                preparedStatement.setString(2, soyad);
                preparedStatement.setString(3, tcKimlikNo);
                preparedStatement.setDate(4, java.sql.Date.valueOf(dogumTarihi));
                preparedStatement.setString(5, cinsiyet);
                preparedStatement.setString(6, adres);
                preparedStatement.setString(7, telefon);
                preparedStatement.setString(8, eposta);
                preparedStatement.setString(9, sigortaBilgisi);
                preparedStatement.setInt(10, hastaID);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // Güncelleme başarılı mesajını göster
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Başarılı");
                    alert.setHeaderText("Güncelleme Tamamlandı");
                    alert.setContentText("Hasta başarıyla güncellendi!");
                    alert.showAndWait();

                    verileriGetir(); // Tabloyu güncelle
                    temizle();
                } else {
                    // Güncelleme başarısız mesajını göster
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Başarısız");
                    alert.setHeaderText("Güncelleme Başarısız");
                    alert.setContentText("Belirtilen HastaID bulunamadı.");
                    alert.showAndWait();
                }
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hata");
            alert.setHeaderText("Geçersiz Giriş");
            alert.setContentText("Lütfen geçerli bir HastaID giriniz.");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void temizle() {
        txtHastaID.clear();
        txtAd.clear();
        txtSoyad.clear();
        txtTCKimlikNo.clear();
        txtDogumTarihi.clear();
        txtCinsiyet.clear();
        txtAdres.clear();
        txtTelefon.clear();
        txtEPosta.clear();
        txtSigortaBilgisi.clear();
    }
}
