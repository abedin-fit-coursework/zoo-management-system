package model;

public class ZivotinjaTura {
    private int id;
    private int zivotinjaId;
    private String zivotinjaVrsta;
    private int turaId;
    private String turaDatum;

    public ZivotinjaTura() {}

    public ZivotinjaTura(int zivotinjaId, int turaId) {
        this.zivotinjaId = zivotinjaId;
        this.turaId = turaId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getZivotinjaId() { return zivotinjaId; }
    public void setZivotinjaId(int zivotinjaId) { 
        this.zivotinjaId = zivotinjaId; 
    }
    
    public String getZivotinjaVrsta() { return zivotinjaVrsta; }
    public void setZivotinjaVrsta(String zivotinjaVrsta) { 
        this.zivotinjaVrsta = zivotinjaVrsta; 
    }
    
    public int getTuraId() { return turaId; }
    public void setTuraId(int turaId) { this.turaId = turaId; }
    
    public String getTuraDatum() { return turaDatum; }
    public void setTuraDatum(String turaDatum) { 
        this.turaDatum = turaDatum; 
    }

    @Override
    public String toString() {
        return "Zivotinja: " + zivotinjaVrsta + " - Tura: " + turaDatum;
    }
}