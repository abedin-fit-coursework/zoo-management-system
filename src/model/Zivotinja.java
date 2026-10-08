package model;

import java.time.LocalDate;

public class Zivotinja {
    private int id;
    private String latinskiNaziv;
    private String vrsta;
    private LocalDate datumDolaska;
    private int statusId;
    private String statusNaziv;
    private int kavezId;
    private String kavezLokacija;
    private int kategorijaId;
    private String kategorijaNaziv;

    public Zivotinja() {}

    public Zivotinja(String latinskiNaziv, String vrsta, LocalDate datumDolaska) {
        this.latinskiNaziv = latinskiNaziv;
        this.vrsta = vrsta;
        this.datumDolaska = datumDolaska;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getLatinskiNaziv() { return latinskiNaziv; } 
    public void setLatinskiNaziv(String latinskiNaziv) {        
        this.latinskiNaziv = latinskiNaziv;
    }

    public String getVrsta() { return vrsta; }
    public void setVrsta(String vrsta) { this.vrsta = vrsta; }

    public LocalDate getDatumDolaska() { return datumDolaska; }  
    public void setDatumDolaska(LocalDate datumDolaska) {        
        this.datumDolaska = datumDolaska;
    }

    public int getStatusId() { return statusId; }
    public void setStatusId(int statusId) { this.statusId = statusId; }

    public String getStatusNaziv() { return statusNaziv; }
    public void setStatusNaziv(String statusNaziv) {
        this.statusNaziv = statusNaziv;
    }

    public int getKavezId() { return kavezId; }
    public void setKavezId(int kavezId) { this.kavezId = kavezId; }

    public String getKavezLokacija() { return kavezLokacija; }
    public void setKavezLokacija(String kavezLokacija) {
        this.kavezLokacija = kavezLokacija;
    }

    public int getKategorijaId() { return kategorijaId; }
    public void setKategorijaId(int kategorijaId) {
        this.kategorijaId = kategorijaId;
    }

    public String getKategorijaNaziv() { return kategorijaNaziv; }
    public void setKategorijaNaziv(String kategorijaNaziv) {
        this.kategorijaNaziv = kategorijaNaziv;
    }

    @Override
    public String toString() {
        return vrsta + " (" + latinskiNaziv + ") - Kavez: " + kavezLokacija +
               ", Status: " + statusNaziv + ", Kategorija: " + kategorijaNaziv;
    }
}