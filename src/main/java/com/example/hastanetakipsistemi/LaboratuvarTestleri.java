package com.example.hastanetakipsistemi;

import java.sql.Date;

public class LaboratuvarTestleri {
    private int testID;
    private int hastaID;
    private String testTuru;
    private String sonuclar;
    private Date testTarihiSaati;
    private int personelID;

    public LaboratuvarTestleri(int testID, int hastaID, String testTuru, String sonuclar, Date testTarihiSaati, int personelID) {
        this.testID = testID;
        this.hastaID = hastaID;
        this.testTuru = testTuru;
        this.sonuclar = sonuclar;
        this.testTarihiSaati = testTarihiSaati;
        this.personelID = personelID;
    }

    public int getTestID() {
        return testID;
    }

    public int getHastaID() {
        return hastaID;
    }

    public String getTestTuru() {
        return testTuru;
    }

    public String getSonuclar() {
        return sonuclar;
    }

    public Date getTestTarihiSaati() {
        return testTarihiSaati;
    }

    public int getPersonelID() {
        return personelID;
    }
}
