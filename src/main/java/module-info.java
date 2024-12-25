module com.example.hastanetakipsistemi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.hastanetakipsistemi to javafx.fxml;
    exports com.example.hastanetakipsistemi;
}