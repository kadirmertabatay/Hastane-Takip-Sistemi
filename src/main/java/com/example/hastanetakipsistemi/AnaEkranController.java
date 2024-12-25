package com.example.hastanetakipsistemi;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AnaEkranController {


    public void ekran_goster(String ekran_isim) throws IOException{

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(ekran_isim));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
    public void hastalar_ekran() throws IOException {
        ekran_goster("hastalar.fxml");
    }

    public void bolumler_ekran() throws IOException {
        ekran_goster("Bolumler.fxml");
    }

    public void doktorlar() throws IOException {
        ekran_goster("Doktorlar.fxml");
    }

    public void faturalar() throws IOException {
        ekran_goster("Faturalar.fxml");
    }


    public void hasta_kayitlari() throws IOException {
        ekran_goster("HastaKayitlari.fxml");
    }

    public void lab() throws IOException {
        ekran_goster("LaboratuvarTestleri.fxml");
    }

    public void personel() throws IOException {
        ekran_goster("Personel.fxml");
    }

    public void randevular() throws IOException {
        ekran_goster("Randevular.fxml");
    }

    public void tedaviler() throws IOException {
        ekran_goster("Tedaviler.fxml");
    }

}
