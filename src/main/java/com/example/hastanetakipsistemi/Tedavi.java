package com.example.hastanetakipsistemi;

public class Tedavi {
    private int tedaviID;
    private String ad;
    private String aciklama;
    private double ucret;

    public Tedavi(int tedaviID, String ad, String aciklama, double ucret) {
        this.tedaviID = tedaviID;
        this.ad = ad;
        this.aciklama = aciklama;
        this.ucret = ucret;
    }

    public int getTedaviID() { return tedaviID; }
    public String getAd() { return ad; }
    public String getAciklama() { return aciklama; }
    public double getUcret() { return ucret; }
}
