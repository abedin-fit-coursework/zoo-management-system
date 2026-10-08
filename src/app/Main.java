package app;

import dao.KategorijaDAO;
import dao.PregledDAO;
import dao.ZivotinjaDAO;
import dao.ZivotinjaTuraDAO;
import db.DatabaseConnection;
import model.Kategorija;
import model.Pregled;
import model.Tura;
import model.Zivotinja;
import model.ZivotinjaTura;
import reports.Izvjestaji;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        DatabaseConnection dbConn = new DatabaseConnection("jdbc:mysql://localhost:3306/zooloski_vrt", "root", "");

        KategorijaDAO kategorijaDAO = new KategorijaDAO(dbConn);
        ZivotinjaDAO zivotinjaDAO = new ZivotinjaDAO(dbConn);
        PregledDAO pregledDAO = new PregledDAO(dbConn);
        ZivotinjaTuraDAO zivotinjaTuraDAO = new ZivotinjaTuraDAO(dbConn);

        Izvjestaji izvjestaji = new Izvjestaji(dbConn);

        while (true) {
            System.out.println("\n=== ZOOLOSKI VRT - GLAVNI MENI ===");
            System.out.println("1) Kategorije (CRUD)");
            System.out.println("2) Zivotinje (CRUD)");
            System.out.println("3) Pregledi (CRUD)");
            System.out.println("4) Zivotinja-Tura (CRUD)");
            System.out.println("5) Izvjestaji");
            System.out.println("0) Izlaz");

            int izbor = readIntRange("Izaberite opciju: ", 0, 5);

            switch (izbor) {
                case 1: meniKategorije(kategorijaDAO); break;
                case 2: meniZivotinje(zivotinjaDAO); break;
                case 3: meniPregledi(pregledDAO, zivotinjaDAO); break;
                case 4: meniZivotinjaTura(zivotinjaTuraDAO); break;
                case 5: meniIzvjestaji(izvjestaji); break;
                case 0:
                    System.out.println("Hvala sto ste koristili program!");
                    return;
            }
        }
    }

    // MENI: KATEGORIJE

    private static void meniKategorije(KategorijaDAO kategorijaDAO) {
        while (true) {
            System.out.println("\n--- KATEGORIJE ---");
            System.out.println("1) Prikazi sve kategorije");
            System.out.println("2) Dodaj kategoriju");
            System.out.println("3) Izmijeni kategoriju");
            System.out.println("4) Obrisi kategoriju");
            System.out.println("0) Nazad");

            int izbor = readIntRange("Izaberite opciju: ", 0, 4);

            switch (izbor) {
                case 1: prikaziKategorije(kategorijaDAO); break;
                case 2: dodajKategoriju(kategorijaDAO); break;
                case 3: izmijeniKategoriju(kategorijaDAO); break;
                case 4: obrisiKategoriju(kategorijaDAO); break;
                case 0: return;
            }
        }
    }

    private static void prikaziKategorije(KategorijaDAO kategorijaDAO) {
        List<Kategorija> lista = kategorijaDAO.getAllKategorije();
        if (lista.isEmpty()) {
            System.out.println("Nema kategorija u bazi.");
            return;
        }
        System.out.println("\nKategorije:");
        for (Kategorija k : lista) {
            System.out.println("- " + k.toString());
        }
    }

    private static void dodajKategoriju(KategorijaDAO kategorijaDAO) {
        String naziv = readNonEmpty("Unesite naziv nove kategorije: ");

        if (kategorijaDAO.existsByNaziv(naziv)) {
            System.out.println("Kategorija sa tim nazivom vec postoji.");
            return;
        }

        Integer nadId = null;
        String imaNad = readYesNo("Da li kategorija ima nadkategoriju? (da/ne): ");

        if (imaNad.equalsIgnoreCase("da")) {
            while (true) {
                String nadNaziv = readNonEmpty("Unesite naziv nadkategorije: ");
                int id = kategorijaDAO.findKategorijaID(nadNaziv);
                if (id == -1) {
                    System.out.println("Nadkategorija nije pronadjena. Pokusajte ponovo.");
                } else {
                    nadId = id;
                    break;
                }
            }
        }

        boolean ok = kategorijaDAO.insertKategorija(naziv, nadId);
        if (ok) System.out.println("Kategorija je dodata.");
        else System.out.println("Greska pri dodavanju kategorije.");
    }

    private static void izmijeniKategoriju(KategorijaDAO kategorijaDAO) {
        prikaziKategorije(kategorijaDAO);

        int katId;
        String stariNaziv;
        while (true) {
            stariNaziv = readNonEmpty("Unesite naziv kategorije za izmjenu: ");
            katId = kategorijaDAO.findKategorijaID(stariNaziv);
            if (katId == -1) System.out.println("Kategorija nije pronadjena. Pokusajte ponovo.");
            else break;
        }

        String noviNaziv = readNonEmpty("Unesite novi naziv kategorije: ");
        if (kategorijaDAO.existsByNaziv(noviNaziv) && kategorijaDAO.findKategorijaID(noviNaziv) != katId) {
            System.out.println("Ne mozete postaviti taj naziv jer vec postoji druga kategorija sa tim nazivom.");
            return;
        }

        Integer novaNadId = null;
        while (true) {
            System.out.println("Unesite novu nadkategoriju (ENTER ako nema):");
            String nadNaziv = sc.nextLine().trim();

            if (nadNaziv.isEmpty()) {
                novaNadId = null;
                break;
            }

            int id = kategorijaDAO.findKategorijaID(nadNaziv);
            if (id == -1) {
                System.out.println("Nadkategorija nije pronadjena. Pokusajte ponovo.");
                continue;
            }

            if (id == katId) {
                System.out.println("Kategorija ne moze biti sama sebi nadkategorija. Pokusajte ponovo.");
                continue;
            }

            novaNadId = id;
            break;
        }

        boolean ok = kategorijaDAO.updateKategorija(katId, noviNaziv, novaNadId);
        if (ok) System.out.println("Kategorija je izmijenjena.");
        else System.out.println("Greska pri izmjeni kategorije.");
    }

    private static void obrisiKategoriju(KategorijaDAO kategorijaDAO) {
        prikaziKategorije(kategorijaDAO);

        int katId;
        while (true) {
            String naziv = readNonEmpty("Unesite naziv kategorije za brisanje: ");
            katId = kategorijaDAO.findKategorijaID(naziv);
            if (katId == -1) System.out.println("Kategorija nije pronadjena. Pokusajte ponovo.");
            else break;
        }

        if (kategorijaDAO.hasPodkategorije(katId)) {
            System.out.println("Ne mozete obrisati kategoriju jer ima podkategorije.");
            return;
        }
        if (kategorijaDAO.hasZivotinje(katId)) {
            System.out.println("Ne mozete obrisati kategoriju jer postoje zivotinje u toj kategoriji.");
            return;
        }

        String potvrda = readYesNo("Da li ste sigurni da zelite obrisati? (da/ne): ");
        if (potvrda.equalsIgnoreCase("ne")) return;

        boolean ok = kategorijaDAO.deleteKategorija(katId);
        if (ok) System.out.println("Kategorija je obrisana.");
        else System.out.println("Greska pri brisanju kategorije.");
    }

    // MENI: ZIVOTINJE

    private static void meniZivotinje(ZivotinjaDAO zivotinjaDAO) {
        while (true) {
            System.out.println("\n--- ZIVOTINJE ---");
            System.out.println("1) Prikazi sve zivotinje");
            System.out.println("2) Dodaj zivotinju");
            System.out.println("3) Izmijeni zivotinju");
            System.out.println("4) Obrisi zivotinju");
            System.out.println("0) Nazad");

            int izbor = readIntRange("Izaberite opciju: ", 0, 4);

            switch (izbor) {
                case 1: prikaziZivotinje(zivotinjaDAO); break;
                case 2: dodajZivotinju(zivotinjaDAO); break;
                case 3: izmijeniZivotinju(zivotinjaDAO); break;
                case 4: obrisiZivotinju(zivotinjaDAO); break;
                case 0: return;
            }
        }
    }

    private static void prikaziZivotinje(ZivotinjaDAO zivotinjaDAO) {
        List<Zivotinja> lista = zivotinjaDAO.getAllZivotinje();
        if (lista.isEmpty()) {
            System.out.println("Nema zivotinja u bazi.");
            return;
        }
        System.out.println("\nZivotinje:");
        for (Zivotinja z : lista) {
            System.out.println("- " + z.toString());
        }
    }

    private static void dodajZivotinju(ZivotinjaDAO zivotinjaDAO) {
        String vrsta = readNonEmpty("Unesite vrstu (npr. Lav): ");
        String lat = readNonEmpty("Unesite latinski naziv (npr. Panthera leo): ");

        if (zivotinjaDAO.existsZivotinja(vrsta, lat)) {
            System.out.println("Zivotinja sa tom vrstom i latinskim nazivom vec postoji.");
            return;
        }

        LocalDate datum = readDate("Unesite datum dolaska (YYYY-MM-DD): ");

        int statusId;
        while (true) {
            String statusNaziv = readNonEmpty("Unesite status (aktivna/u karantinu): ");
            statusId = zivotinjaDAO.findStatusID(statusNaziv);
            if (statusId == -1) System.out.println("Status nije pronadjen. Pokusajte ponovo.");
            else break;
        }

        int kavezId;
        while (true) {
            String lokacija = readNonEmpty("Unesite lokaciju kaveza (npr. Sektor A, red 1): ");
            kavezId = zivotinjaDAO.findKavezID(lokacija);
            if (kavezId == -1) System.out.println("Kavez nije pronadjen. Pokusajte ponovo.");
            else break;
        }

        int katId;
        while (true) {
            String katNaziv = readNonEmpty("Unesite naziv kategorije (npr. Mesožderi): ");
            katId = zivotinjaDAO.findKategorijaID(katNaziv);
            if (katId == -1) System.out.println("Kategorija nije pronadjena. Pokusajte ponovo.");
            else break;
        }

        boolean ok = zivotinjaDAO.insertZivotinja(lat, vrsta, datum, statusId, kavezId, katId);
        if (ok) System.out.println("Zivotinja je dodata.");
        else System.out.println("Greska pri dodavanju zivotinje.");
    }

    private static void izmijeniZivotinju(ZivotinjaDAO zivotinjaDAO) {
        prikaziZivotinje(zivotinjaDAO);

        int zivotinjaId;
        String staraVrsta;
        String stariLat;
        while (true) {
            staraVrsta = readNonEmpty("Unesite vrstu zivotinje za izmjenu: ");
            stariLat = readNonEmpty("Unesite latinski naziv zivotinje za izmjenu: ");
            zivotinjaId = zivotinjaDAO.findZivotinjaID(staraVrsta, stariLat);
            if (zivotinjaId == -1) System.out.println("Zivotinja nije pronadjena. Pokusajte ponovo.");
            else break;
        }

        String novaVrsta = readNonEmpty("Unesite novu vrstu: ");
        String noviLat = readNonEmpty("Unesite novi latinski naziv: ");

        if (!(novaVrsta.equalsIgnoreCase(staraVrsta) && noviLat.equalsIgnoreCase(stariLat))
                && zivotinjaDAO.existsZivotinja(novaVrsta, noviLat)) {
            System.out.println("Vec postoji zivotinja sa tom vrstom i latinskim nazivom. Izmjena prekinuta.");
            return;
        }

        LocalDate noviDatum = readDate("Unesite novi datum dolaska (YYYY-MM-DD): ");

        int statusId;
        while (true) {
            String statusNaziv = readNonEmpty("Unesite novi status (aktivna/u karantinu): ");
            statusId = zivotinjaDAO.findStatusID(statusNaziv);
            if (statusId == -1) System.out.println("Status nije pronadjen. Pokusajte ponovo.");
            else break;
        }

        int kavezId;
        while (true) {
            String lokacija = readNonEmpty("Unesite novu lokaciju kaveza: ");
            kavezId = zivotinjaDAO.findKavezID(lokacija);
            if (kavezId == -1) System.out.println("Kavez nije pronadjen. Pokusajte ponovo.");
            else break;
        }

        int katId;
        while (true) {
            String katNaziv = readNonEmpty("Unesite novu kategoriju: ");
            katId = zivotinjaDAO.findKategorijaID(katNaziv);
            if (katId == -1) System.out.println("Kategorija nije pronadjena. Pokusajte ponovo.");
            else break;
        }

        boolean ok = zivotinjaDAO.updateZivotinja(zivotinjaId, noviLat, novaVrsta, noviDatum, statusId, kavezId, katId);
        if (ok) System.out.println("Zivotinja je izmijenjena.");
        else System.out.println("Greska pri izmjeni zivotinje.");
    }

    private static void obrisiZivotinju(ZivotinjaDAO zivotinjaDAO) {
        prikaziZivotinje(zivotinjaDAO);

        int zivotinjaId;
        while (true) {
            String vrsta = readNonEmpty("Unesite vrstu zivotinje za brisanje: ");
            String lat = readNonEmpty("Unesite latinski naziv za brisanje: ");
            zivotinjaId = zivotinjaDAO.findZivotinjaID(vrsta, lat);
            if (zivotinjaId == -1) System.out.println("Zivotinja nije pronadjena. Pokusajte ponovo.");
            else break;
        }

        if (zivotinjaDAO.hasPregledi(zivotinjaId)) {
            System.out.println("Ne mozete obrisati zivotinju jer postoje pregledi za nju.");
            return;
        }
        if (zivotinjaDAO.hasTure(zivotinjaId)) {
            System.out.println("Ne mozete obrisati zivotinju jer je vezana za ture.");
            return;
        }
        if (zivotinjaDAO.hasStaratelje(zivotinjaId)) {
            System.out.println("Ne mozete obrisati zivotinju jer je vezana za staratelje.");
            return;
        }

        String potvrda = readYesNo("Da li ste sigurni da zelite obrisati? (da/ne): ");
        if (potvrda.equalsIgnoreCase("ne")) return;

        boolean ok = zivotinjaDAO.deleteZivotinja(zivotinjaId);
        if (ok) System.out.println("Zivotinja je obrisana.");
        else System.out.println("Greska pri brisanju zivotinje.");
    }

    // MENI: PREGLEDI

    private static void meniPregledi(PregledDAO pregledDAO, ZivotinjaDAO zivotinjaDAO) {
        while (true) {
            System.out.println("\n--- PREGLEDI ---");
            System.out.println("1) Prikazi sve preglede");
            System.out.println("2) Prikazi preglede za jednu zivotinju");
            System.out.println("3) Dodaj pregled");
            System.out.println("4) Izmijeni pregled (bez ID unosa)");
            System.out.println("5) Obrisi pregled (bez ID unosa)");
            System.out.println("0) Nazad");

            int izbor = readIntRange("Izaberite opciju: ", 0, 5);

            switch (izbor) {
                case 1: prikaziSvePreglede(pregledDAO); break;
                case 2: prikaziPregledeZaZivotinju(pregledDAO, zivotinjaDAO); break;
                case 3: dodajPregled(pregledDAO); break;
                case 4: izmijeniPregledBezID(pregledDAO, zivotinjaDAO); break;
                case 5: obrisiPregledBezID(pregledDAO, zivotinjaDAO); break;
                case 0: return;
            }
        }
    }

    private static void prikaziSvePreglede(PregledDAO pregledDAO) {
        List<Pregled> lista = pregledDAO.getAllPregledi();
        if (lista.isEmpty()) {
            System.out.println("Nema pregleda u bazi.");
            return;
        }
        System.out.println("\nPregledi:");
        for (Pregled p : lista) {
            System.out.println("- " + p.toString());
        }
    }

    private static void prikaziPregledeZaZivotinju(PregledDAO pregledDAO, ZivotinjaDAO zivotinjaDAO) {
        int zivotinjaId = ucitajZivotinjuIdPrekoNaziva(zivotinjaDAO);

        List<Pregled> lista = pregledDAO.getPreglediByZivotinjaId(zivotinjaId);
        if (lista.isEmpty()) {
            System.out.println("Nema pregleda za tu zivotinju.");
            return;
        }

        System.out.println("\nPregledi za zivotinju:");
        for (Pregled p : lista) {
            System.out.println("- " + p.toString());
        }
    }

    private static void dodajPregled(PregledDAO pregledDAO) {
        LocalDate datum = readDate("Unesite datum pregleda (YYYY-MM-DD): ");
        String rezultat = readNonEmpty("Unesite rezultat: ");
        String preporuke = readNonEmpty("Unesite preporuke: ");

        int zivotinjaId;
        while (true) {
            String vrsta = readNonEmpty("Unesite vrstu zivotinje: ");
            String lat = readNonEmpty("Unesite latinski naziv zivotinje: ");
            zivotinjaId = pregledDAO.findZivotinjaID(vrsta, lat);
            if (zivotinjaId == -1) System.out.println("Zivotinja nije pronadjena. Pokusajte ponovo.");
            else break;
        }

        int vetId;
        while (true) {
        	String jmbg = readJMBG("Unesite JMBG veterinara: ");
            vetId = pregledDAO.findVeterinarIDByJMBG(jmbg);
            if (vetId == -1) System.out.println("Veterinar nije pronadjen. Pokusajte ponovo.");
            else break;
        }

        boolean ok = pregledDAO.insertPregled(datum, rezultat, preporuke, zivotinjaId, vetId);
        if (ok) System.out.println("Pregled je dodat.");
        else System.out.println("Greska pri dodavanju pregleda.");
    }

    private static void izmijeniPregledBezID(PregledDAO pregledDAO, ZivotinjaDAO zivotinjaDAO) {
        int zivotinjaId = ucitajZivotinjuIdPrekoNaziva(zivotinjaDAO);

        List<Pregled> pregledi = pregledDAO.getPreglediByZivotinjaId(zivotinjaId);
        if (pregledi.isEmpty()) {
            System.out.println("Nema pregleda za tu zivotinju.");
            return;
        }

        System.out.println("\nIzaberite pregled (po rednom broju):");
        for (int i = 0; i < pregledi.size(); i++) {
            System.out.println((i + 1) + ") " + pregledi.get(i).toString());
        }

        int izbor = readIntRange("Unesite broj pregleda: ", 1, pregledi.size());
        int pregledId = pregledi.get(izbor - 1).getId(); 

        LocalDate datum = readDate("Unesite novi datum pregleda (YYYY-MM-DD): ");
        String rezultat = readNonEmpty("Unesite novi rezultat: ");
        String preporuke = readNonEmpty("Unesite nove preporuke: ");

        int noviZivotinjaId = zivotinjaId; 
        String promjenaZiv = readYesNo("Da li zelite promijeniti zivotinju za ovaj pregled? (da/ne): ");
        if (promjenaZiv.equalsIgnoreCase("da")) {
            noviZivotinjaId = ucitajZivotinjuIdPrekoNaziva(zivotinjaDAO);
        }

        int vetId;
        while (true) {
            String jmbg = readNonEmpty("Unesite JMBG veterinara: ");
            vetId = pregledDAO.findVeterinarIDByJMBG(jmbg);
            if (vetId == -1) System.out.println("Veterinar nije pronadjen. Pokusajte ponovo.");
            else break;
        }

        boolean ok = pregledDAO.updatePregled(pregledId, datum, rezultat, preporuke, noviZivotinjaId, vetId);
        if (ok) System.out.println("Pregled je izmijenjen.");
        else System.out.println("Greska pri izmjeni pregleda.");
    }

    private static void obrisiPregledBezID(PregledDAO pregledDAO, ZivotinjaDAO zivotinjaDAO) {
        int zivotinjaId = ucitajZivotinjuIdPrekoNaziva(zivotinjaDAO);

        List<Pregled> pregledi = pregledDAO.getPreglediByZivotinjaId(zivotinjaId);
        if (pregledi.isEmpty()) {
            System.out.println("Nema pregleda za tu zivotinju.");
            return;
        }

        System.out.println("\nIzaberite pregled za brisanje (po rednom broju):");
        for (int i = 0; i < pregledi.size(); i++) {
            System.out.println((i + 1) + ") " + pregledi.get(i).toString());
        }

        int izbor = readIntRange("Unesite broj pregleda: ", 1, pregledi.size());
        int pregledId = pregledi.get(izbor - 1).getId(); 

        String potvrda = readYesNo("Da li ste sigurni da zelite obrisati pregled? (da/ne): ");
        if (potvrda.equalsIgnoreCase("ne")) return;

        boolean ok = pregledDAO.deletePregled(pregledId);
        if (ok) System.out.println("Pregled je obrisan.");
        else System.out.println("Greska pri brisanju pregleda.");
    }

    // MENI: ZIVOTINJA_TURA

    private static void meniZivotinjaTura(ZivotinjaTuraDAO dao) {
        while (true) {
            System.out.println("\n--- ZIVOTINJA - TURA ---");
            System.out.println("1) Prikazi sve zivotinje na turama");
            System.out.println("2) Dodaj vezu (zivotinja-tura)");
            System.out.println("3) Izmijeni vezu");
            System.out.println("4) Obrisi vezu");
            System.out.println("5) Prikazi ture po datumu");
            System.out.println("0) Nazad");

            int izbor = readIntRange("Izaberite opciju: ", 0, 5);

            switch (izbor) {
                case 1: prikaziSveVeze(dao); break;
                case 2: dodajVezu(dao); break;
                case 3: izmijeniVezuBezID(dao); break;
                case 4: obrisiVezuBezID(dao); break;
                case 5: prikaziTurePoDatumu(dao); break;
                case 0: return;
            }
        }
    }

    private static void prikaziSveVeze(ZivotinjaTuraDAO dao) {
        List<ZivotinjaTura> lista = dao.getAllZivotinjaTura();
        if (lista.isEmpty()) {
            System.out.println("Nema veza zivotinja-tura u bazi.");
            return;
        }
        System.out.println("\nVeze:");
        for (ZivotinjaTura zt : lista) {
            System.out.println("- " + zt.toString());
        }
    }

    private static void dodajVezu(ZivotinjaTuraDAO dao) {
        int zivotinjaId = ucitajZivotinjuIdPrekoNaziva(dao);

        int turaId = ucitajTuraIdPoDatumu(dao);

        if (dao.existsVeza(zivotinjaId, turaId)) {
            System.out.println("Ova veza vec postoji.");
            return;
        }

        boolean ok = dao.insertZivotinjaTura(zivotinjaId, turaId);
        if (ok) System.out.println("Veza je dodata.");
        else System.out.println("Greska pri dodavanju veze.");
    }

    private static void izmijeniVezuBezID(ZivotinjaTuraDAO dao) {
        List<ZivotinjaTura> veze = dao.getAllZivotinjaTura();
        if (veze.isEmpty()) {
            System.out.println("Nema veza zivotinja-tura u bazi.");
            return;
        }

        System.out.println("\nIzaberite vezu za izmjenu (po rednom broju):");
        for (int i = 0; i < veze.size(); i++) {
            System.out.println((i + 1) + ") " + veze.get(i).toString());
        }

        int izbor = readIntRange("Unesite broj veze: ", 1, veze.size());
        int vezaId = veze.get(izbor - 1).getId(); 

        int noviZivotinjaId = ucitajZivotinjuIdPrekoNaziva(dao);
        int noviTuraId = ucitajTuraIdPoDatumu(dao);

        if (dao.existsVeza(noviZivotinjaId, noviTuraId)) {
            System.out.println("Ne mozete postaviti ovu kombinaciju jer vec postoji.");
            return;
        }

        boolean ok = dao.updateZivotinjaTura(vezaId, noviZivotinjaId, noviTuraId);
        if (ok) System.out.println("Veza je izmijenjena.");
        else System.out.println("Greska pri izmjeni veze.");
    }

    private static void obrisiVezuBezID(ZivotinjaTuraDAO dao) {
        List<ZivotinjaTura> veze = dao.getAllZivotinjaTura();
        if (veze.isEmpty()) {
            System.out.println("Nema veza zivotinja-tura u bazi.");
            return;
        }

        System.out.println("\nIzaberite vezu za brisanje (po rednom broju):");
        for (int i = 0; i < veze.size(); i++) {
            System.out.println((i + 1) + ") " + veze.get(i).toString());
        }

        int izbor = readIntRange("Unesite broj veze: ", 1, veze.size());
        int vezaId = veze.get(izbor - 1).getId(); 

        String potvrda = readYesNo("Da li ste sigurni da zelite obrisati vezu? (da/ne): ");
        if (potvrda.equalsIgnoreCase("ne")) return;

        boolean ok = dao.deleteZivotinjaTura(vezaId);
        if (ok) System.out.println("Veza je obrisana.");
        else System.out.println("Greska pri brisanju veze.");
    }

    private static void prikaziTurePoDatumu(ZivotinjaTuraDAO dao) {
        while (true) {
            String datum = readNonEmpty("Unesite datum ture (YYYY-MM-DD): ");
            if (!validateDateString(datum)) {
                System.out.println("Neispravan format datuma. Pokusajte ponovo.");
                continue;
            }

            List<Tura> ture = dao.getTureByDatum(datum);
            if (ture.isEmpty()) {
                System.out.println("Nema tura za taj datum.");
                return;
            }

            System.out.println("Ture na datum " + datum + ":");
            for (Tura t : ture) {
                System.out.println("- " + t.toString());
            }
            return;
        }
    }

    // MENI: IZVJESTAJI

    private static void meniIzvjestaji(Izvjestaji izvjestaji) {
        while (true) {
            System.out.println("\n--- IZVJESTAJI ---");
            System.out.println("1) Top staratelj po razlicitim kategorijama (specijalizacija)");
            System.out.println("2) Veterinari iznad prosjeka pregleda u periodu");
            System.out.println("3) Kavezi iznad prosjeka broja zivotinja (tip ili svi)");
            System.out.println("0) Nazad");

            int izbor = readIntRange("Izaberite opciju: ", 0, 3);

            switch (izbor) {
                case 1: izvjestaj1(izvjestaji); break;
                case 2: izvjestaj2(izvjestaji); break;
                case 3: izvjestaj3(izvjestaji); break;
                case 0: return;
            }
        }
    }

    private static void izvjestaj1(Izvjestaji izvjestaji) {
        String spec = readNonEmpty("Unesite specijalizaciju (npr. Ishrana): ");
        String rez = izvjestaji.izvjestajTopStarateljRazliciteKategorije(spec);
        if (rez == null) System.out.println("Nema podataka za tu specijalizaciju.");
        else System.out.println(rez);
    }

    private static void izvjestaj2(Izvjestaji izvjestaji) {
        LocalDate od = readDate("Unesite datum OD (YYYY-MM-DD): ");
        LocalDate d0;
        while (true) {
            LocalDate d1 = readDate("Unesite datum DO (YYYY-MM-DD): ");
            if (od.isAfter(d1)) {
                System.out.println("Datum OD ne moze biti poslije datuma DO. Pokusajte ponovo.");
            } else {
                d0 = d1;
                break;
            }
        }

        List<String> lista = izvjestaji.izvjestajVeterinariIznadProsjeka(od.toString(), d0.toString());
        if (lista.isEmpty()) System.out.println("Nema rezultata za dati period.");
        else for (String s : lista) System.out.println(s);
    }

    private static void izvjestaj3(Izvjestaji izvjestaji) {
        String tip;
        while (true) {
            System.out.println("Unesite tip kaveza (zatvoreni/otvoreni) ili pritisnite ENTER za sve:");
            tip = sc.nextLine().trim();
            if (tip.isEmpty()) {
                tip = null;
                break;
            }
            if (tip.equalsIgnoreCase("zatvoreni") || tip.equalsIgnoreCase("otvoreni")) {
                break;
            }
            System.out.println("Neispravan tip. Unesite 'zatvoreni', 'otvoreni' ili ENTER za sve.");
        }

        List<String> lista = izvjestaji.izvjestajKaveziIznadProsjeka(tip);
        if (lista.isEmpty()) System.out.println("Nema rezultata za dati kriterijum.");
        else for (String s : lista) System.out.println(s);
    }

    // POMOCNE METODE (unos bez ID)

    private static int ucitajZivotinjuIdPrekoNaziva(ZivotinjaDAO zivotinjaDAO) {
        int zivotinjaId;
        while (true) {
            String vrsta = readNonEmpty("Unesite vrstu zivotinje: ");
            String lat = readNonEmpty("Unesite latinski naziv zivotinje: ");
            zivotinjaId = zivotinjaDAO.findZivotinjaID(vrsta, lat);
            if (zivotinjaId == -1) System.out.println("Zivotinja nije pronadjena. Pokusajte ponovo.");
            else break;
        }
        return zivotinjaId;
    }

    private static int ucitajZivotinjuIdPrekoNaziva(ZivotinjaTuraDAO dao) {
        int zivotinjaId;
        while (true) {
            String vrsta = readNonEmpty("Unesite vrstu zivotinje: ");
            String lat = readNonEmpty("Unesite latinski naziv zivotinje: ");
            zivotinjaId = dao.findZivotinjaID(vrsta, lat);
            if (zivotinjaId == -1) System.out.println("Zivotinja nije pronadjena. Pokusajte ponovo.");
            else break;
        }
        return zivotinjaId;
    }

    private static int ucitajTuraIdPoDatumu(ZivotinjaTuraDAO dao) {
        while (true) {
            String datum = readNonEmpty("Unesite datum ture (YYYY-MM-DD): ");
            if (!validateDateString(datum)) {
                System.out.println("Neispravan format datuma. Pokusajte ponovo.");
                continue;
            }

            List<Tura> ture = dao.getTureByDatum(datum);
            if (ture.isEmpty()) {
                System.out.println("Nema tura za taj datum. Pokusajte ponovo.");
                continue;
            }

            System.out.println("Ture na datum " + datum + ":");
            for (int i = 0; i < ture.size(); i++) {
                System.out.println((i + 1) + ") " + ture.get(i).toString());
            }

            int izbor = readIntRange("Izaberite turu (broj sa liste): ", 1, ture.size());
            return ture.get(izbor - 1).getId(); 
        }
    }

    // POMOCNE METODE ZA UNOS

    private static String readNonEmpty(String poruka) {
        while (true) {
            System.out.print(poruka);
            String s = sc.nextLine();
            if (s != null && !s.trim().isEmpty()) return s.trim();
            System.out.println("Unos ne moze biti prazan. Pokusajte ponovo.");
        }
    }
    
    private static String readJMBG(String poruka) {
        while (true) {
            System.out.print(poruka);
            String s = sc.nextLine().trim();

            if (!s.matches("\\d{13}")) {
                System.out.println("JMBG mora imati tacno 13 cifara.");
                continue;
            }
            return s;
        }
    }

    private static int readIntRange(String poruka, int min, int max) {
        while (true) {
            System.out.print(poruka);
            String s = sc.nextLine();
            try {
                int x = Integer.parseInt(s.trim());
                if (x < min || x > max) {
                    System.out.println("Unesite broj u opsegu [" + min + "-" + max + "].");
                } else {
                    return x;
                }
            } catch (Exception e) {
                System.out.println("Neispravan unos. Unesite broj.");
            }
        }
    }

    private static String readYesNo(String poruka) {
        while (true) {
            System.out.print(poruka);
            String s = sc.nextLine().trim().toLowerCase();
            if (s.equals("da") || s.equals("ne")) return s;
            System.out.println("Molim unesite 'da' ili 'ne'.");
        }
    }

    private static LocalDate readDate(String poruka) {
        while (true) {
            System.out.print(poruka);
            String s = sc.nextLine().trim();
            try {
                return LocalDate.parse(s);
            } catch (DateTimeParseException e) {
                System.out.println("Neispravan datum. Format mora biti YYYY-MM-DD. Pokusajte ponovo.");
            }
        }
    }

    private static boolean validateDateString(String s) {
        try {
            LocalDate.parse(s.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
