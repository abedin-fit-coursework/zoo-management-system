package model;

import java.time.LocalDate;

public class Zaposleni {
    private int id;
    private String jmbg;
    private String ime;
    private String prezime;
    private LocalDate datumZaposlenja;

    public Zaposleni() {}

    public Zaposleni(String jmbg, String ime, String prezime, LocalDate datumZaposlenja) {
        this.jmbg = jmbg;
        this.ime = ime;
        this.prezime = prezime;
        this.datumZaposlenja = datumZaposlenja;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getJmbg() { return jmbg; }
    public void setJmbg(String jmbg) { this.jmbg = jmbg; }

    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }

    public String getPrezime() { return prezime; }
    public void setPrezime(String prezime) { this.prezime = prezime; }

    public LocalDate getDatumZaposlenja() { return datumZaposlenja; }
    public void setDatumZaposlenja(LocalDate datumZaposlenja) {
        this.datumZaposlenja = datumZaposlenja;
    }

    @Override
    public String toString() {
        return ime + " " + prezime + " (JMBG: " + jmbg + ")";
    }
}
