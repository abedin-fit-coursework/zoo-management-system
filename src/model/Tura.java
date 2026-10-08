package model;

import java.time.LocalDate;

public class Tura {
    private int id;
    private LocalDate datumTure;
    private int starateljId;
    private String starateljIme;
    private String starateljPrezime;

    public Tura() {}

    public Tura(LocalDate datumTure, int starateljId) {
        this.datumTure = datumTure;
        this.starateljId = starateljId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public LocalDate getDatumTure() { return datumTure; }
    public void setDatumTure(LocalDate datumTure) { 
        this.datumTure = datumTure; 
    }
    
    public int getStarateljId() { return starateljId; }
    public void setStarateljId(int starateljId) { 
        this.starateljId = starateljId; 
    }
    
    public String getStarateljIme() { return starateljIme; }
    public void setStarateljIme(String starateljIme) { 
        this.starateljIme = starateljIme; 
    }
    
    public String getStarateljPrezime() { return starateljPrezime; }
    public void setStarateljPrezime(String starateljPrezime) { 
        this.starateljPrezime = starateljPrezime; 
    }

    @Override
    public String toString() {
        return "Tura " + datumTure + " - Staratelj: " + 
               starateljIme + " " + starateljPrezime;
    }
}