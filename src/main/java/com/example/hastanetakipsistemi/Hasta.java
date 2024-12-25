package com.example.hastanetakipsistemi;

import java.sql.Date;

public class Hasta {
    private int id;
    private String ad;
    private String soyad;
    private String tcKimlikNo;
    private Date dogumTarihi;
    private String cinsiyet;
    private String adres;
    private String telefon;
    private String eposta;
    private String sigortaBilgisi;

    public Hasta(int id, String ad, String soyad, String tcKimlikNo, Date dogumTarihi, String cinsiyet, String adres, String telefon, String eposta, String sigortaBilgisi) {
        this.id = id;
        this.ad = ad;
        this.soyad = soyad;
        this.tcKimlikNo = tcKimlikNo;
        this.dogumTarihi = dogumTarihi;
        this.cinsiyet = cinsiyet;
        this.adres = adres;
        this.telefon = telefon;
        this.eposta = eposta;
        this.sigortaBilgisi = sigortaBilgisi;
    }

    public int getId() {
        return id;
    }

    public String getAd() {
        return ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public String getTcKimlikNo() {
        return tcKimlikNo;
    }

    public Date getDogumTarihi() {
        return dogumTarihi;
    }

    public String getCinsiyet() {
        return cinsiyet;
    }

    public String getAdres() {
        return adres;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getEposta() {
        return eposta;
    }

    public String getSigortaBilgisi() {
        return sigortaBilgisi;
    }
}
