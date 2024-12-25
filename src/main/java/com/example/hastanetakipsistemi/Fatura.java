package com.example.hastanetakipsistemi;

import java.sql.Date;

public class Fatura {
    private int faturaID;
    private int hastaID;
    private int kayitID;
    private Date faturaTarihi;
    private double toplamTutar;
    private String odemeDurumu;

    public Fatura(int faturaID, int hastaID, int kayitID, Date faturaTarihi, double toplamTutar, String odemeDurumu) {
        this.faturaID = faturaID;
        this.hastaID = hastaID;
        this.kayitID = kayitID;
        this.faturaTarihi = faturaTarihi;
        this.toplamTutar = toplamTutar;
        this.odemeDurumu = odemeDurumu;
    }

    public int getFaturaID() { return faturaID; }
    public int getHastaID() { return hastaID; }
    public int getKayitID() { return kayitID; }
    public Date getFaturaTarihi() { return faturaTarihi; }
    public double getToplamTutar() { return toplamTutar; }
    public String getOdemeDurumu() { return odemeDurumu; }
}
