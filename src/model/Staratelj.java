package model;

public class Staratelj extends Zaposleni {
    
	private String specijalizacija;

    public Staratelj() {
        super();
    }

    public String getSpecijalizacija() { 
        return specijalizacija; 
    }

    public void setSpecijalizacija(String specijalizacija) {
        this.specijalizacija = specijalizacija;
    }

    @Override
    public String toString() {
        return "Staratelj: " + super.toString() +
               " - Specijalizacija: " + specijalizacija;
    }
}
