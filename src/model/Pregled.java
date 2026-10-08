package model;

import java.time.LocalDate;

public class Pregled {
    private int id;
    private LocalDate datumPregleda;  
    private String rezultat;
    private String preporuke;
    private int zivotinjaId;
    private String zivotinjaVrsta;      
    private int veterinarId;
    private String veterinarIme;        
    private String veterinarPrezime;    

    public Pregled() {}

    public Pregled(LocalDate datumPregleda, String rezultat, String preporuke) {
        this.datumPregleda = datumPregleda;
        this.rezultat = rezultat;
        this.preporuke = preporuke;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getDatumPregleda() { return datumPregleda; }
    public void setDatumPregleda(LocalDate datumPregleda) {
        this.datumPregleda = datumPregleda;
    }

    public String getRezultat() { return rezultat; }
    public void setRezultat(String rezultat) { this.rezultat = rezultat; }

    public String getPreporuke() { return preporuke; }
    public void setPreporuke(String preporuke) { this.preporuke = preporuke; }

    public int getZivotinjaId() { return zivotinjaId; }
    public void setZivotinjaId(int zivotinjaId) {
        this.zivotinjaId = zivotinjaId;
    }

    public String getZivotinjaVrsta() { return zivotinjaVrsta; }
    public void setZivotinjaVrsta(String zivotinjaVrsta) {
        this.zivotinjaVrsta = zivotinjaVrsta;
    }

    public int getVeterinarId() { return veterinarId; }
    public void setVeterinarId(int veterinarId) {
        this.veterinarId = veterinarId;
    }

    public String getVeterinarIme() { return veterinarIme; }
    public void setVeterinarIme(String veterinarIme) {
        this.veterinarIme = veterinarIme;
    }

    public String getVeterinarPrezime() { return veterinarPrezime; }
    public void setVeterinarPrezime(String veterinarPrezime) {
        this.veterinarPrezime = veterinarPrezime;
    }

    @Override
    public String toString() {
        return "Pregled " + datumPregleda + " - " + zivotinjaVrsta +
               " (Veterinar: " + veterinarIme + " " + veterinarPrezime +
               ") - Rezultat: " + rezultat;
    }
}