package model;

public class Kategorija {
    private int id;
    private String naziv;
    private Integer nadkategorijaId;
    private String nadkategorijaNaziv;

    public Kategorija() {}

    public Kategorija(int id, String naziv, Integer nadkategorijaId) {
        this.id = id;
        this.naziv = naziv;
        this.nadkategorijaId = nadkategorijaId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) { this.naziv = naziv; }
    
    public Integer getNadkategorijaId() { return nadkategorijaId; }
    public void setNadkategorijaId(Integer nadkategorijaId) { 
        this.nadkategorijaId = nadkategorijaId; 
    }
    
    public String getNadkategorijaNaziv() { return nadkategorijaNaziv; }
    public void setNadkategorijaNaziv(String nadkategorijaNaziv) { 
        this.nadkategorijaNaziv = nadkategorijaNaziv; 
    }

    @Override
    public String toString() {
        if (nadkategorijaNaziv != null) {
            return naziv + " (nadkategorija: " + nadkategorijaNaziv + ")";
        }
        return naziv;
    }
}