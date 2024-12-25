package com.example.hastanetakipsistemi;

import java.sql.Date;

public class Randevu {
    private int randevuID;
    private int hastaID;
    private int doktorID;
    private Date randevuTarihiSaati;
    private String durum;
    private String notlar;

    public Randevu(int randevuID, int hastaID, int doktorID, Date randevuTarihiSaati, String durum, String notlar) {
        this.randevuID = randevuID;
        this.hastaID = hastaID;
        this.doktorID = doktorID;
        this.randevuTarihiSaati = randevuTarihiSaati;
        this.durum = durum;
        this.notlar = notlar;
    }

    public int getRandevuID() { return randevuID; }
    public int getHastaID() { return hastaID; }
    public int getDoktorID() { return doktorID; }
    public Date getRandevuTarihiSaati() { return randevuTarihiSaati; }
    public String getDurum() { return durum; }
    public String getNotlar() { return notlar; }
}
