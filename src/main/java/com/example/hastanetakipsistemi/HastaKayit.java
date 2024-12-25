package com.example.hastanetakipsistemi;

import java.sql.Date;

public class HastaKayit {
    private int kayitID;
    private int hastaID;
    private int randevuID;
    private Date kayitTarihi;
    private String tani;
    private int tedaviID;
    private String recete;
    private String doktorNotlari;

    public HastaKayit(int kayitID, int hastaID, int randevuID, Date kayitTarihi, String tani, int tedaviID, String recete, String doktorNotlari) {
        this.kayitID = kayitID;
        this.hastaID = hastaID;
        this.randevuID = randevuID;
        this.kayitTarihi = kayitTarihi;
        this.tani = tani;
        this.tedaviID = tedaviID;
        this.recete = recete;
        this.doktorNotlari = doktorNotlari;
    }

    public int getKayitID() { return kayitID; }
    public int getHastaID() { return hastaID; }
    public int getRandevuID() { return randevuID; }
    public Date getKayitTarihi() { return kayitTarihi; }
    public String getTani() { return tani; }
    public int getTedaviID() { return tedaviID; }
    public String getRecete() { return recete; }
    public String getDoktorNotlari() { return doktorNotlari; }
}
