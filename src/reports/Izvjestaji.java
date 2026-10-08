package reports;

import db.DatabaseConnection;
import model.Staratelj;
import model.Veterinar;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Izvjestaji {

	private DatabaseConnection dbConn;

	public Izvjestaji(DatabaseConnection dbConn) {
		this.dbConn = dbConn;
	}

	// I Izvjestaj: Za korisnicki definisanu specijalizaciju, prikazati staratelja
	// koji brine o zivotinjama iz najvise razlicitih kategorija
	public String izvjestajTopStarateljRazliciteKategorije(String specijalizacija) {

		Connection conn = dbConn.open();
		if (conn == null)
			return null;

		String sql = "SELECT s.zaposleni_id AS staratelj_id, z.jmbg, z.ime, z.prezime, s.specijalizacija, "
				+ "COUNT(DISTINCT zi.kategorija_id) AS broj_razlicitih_kategorija " + "FROM staratelj s "
				+ "JOIN zaposleni z ON z.id = s.zaposleni_id "
				+ "JOIN zivotinja_staratelj zs ON zs.staratelj_id = s.zaposleni_id "
				+ "JOIN zivotinja zi ON zi.id = zs.zivotinja_id " + "WHERE s.specijalizacija = '" + specijalizacija
				+ "' " + "GROUP BY s.zaposleni_id, z.jmbg, z.ime, z.prezime, s.specijalizacija "
				+ "ORDER BY broj_razlicitih_kategorija DESC " + "LIMIT 1";

		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			if (rs.next()) {
				Staratelj s = new Staratelj();
				s.setId(rs.getInt("staratelj_id"));
				s.setJmbg(rs.getString("jmbg"));
				s.setIme(rs.getString("ime"));
				s.setPrezime(rs.getString("prezime"));
				s.setSpecijalizacija(rs.getString("specijalizacija"));

				int brojKat = rs.getInt("broj_razlicitih_kategorija");

				return s.toString() + " | Broj razlicitih kategorija: " + brojKat;
			}

			return null;

		} catch (SQLException e) {
			return null;
		} finally {
			dbConn.close(conn);
		}
	}

	// II Izvjestaj: Prikazati veterinare ciji je broj pregleda u korisnicki
	// definisanom periodu
	// veci od prosjecnog broja pregleda svih veterinara u tom periodu,
	// sortirano u opadajućem redoslijedu.
	public List<String> izvjestajVeterinariIznadProsjeka(String datumOd, String datumDo) {

		Connection conn = dbConn.open();
		List<String> rezultat = new ArrayList<>();
		if (conn == null)
			return rezultat;

		String sql = "SELECT v.zaposleni_id AS veterinar_id, z.jmbg, z.ime, z.prezime, "
				+ "COUNT(p.id) AS broj_pregleda " + "FROM veterinar v " + "JOIN zaposleni z ON v.zaposleni_id = z.id "
				+ "LEFT JOIN pregled p ON p.veterinar_id = v.zaposleni_id " + "AND p.datum_pregleda BETWEEN '" + datumOd
				+ "' AND '" + datumDo + "' " + "GROUP BY v.zaposleni_id, z.jmbg, z.ime, z.prezime "
				+ "HAVING broj_pregleda > ( " + "SELECT AVG(t.broj_pregleda) " + "FROM ( "
				+ "SELECT v2.zaposleni_id, COUNT(p2.id) AS broj_pregleda " + "FROM veterinar v2 "
				+ "LEFT JOIN pregled p2 ON p2.veterinar_id = v2.zaposleni_id " + "AND p2.datum_pregleda BETWEEN '"
				+ datumOd + "' AND '" + datumDo + "' " + "GROUP BY v2.zaposleni_id " + ") AS t " + ") "
				+ "ORDER BY broj_pregleda DESC";

		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				Veterinar v = new Veterinar();
				v.setId(rs.getInt("veterinar_id"));
				v.setJmbg(rs.getString("jmbg"));
				v.setIme(rs.getString("ime"));
				v.setPrezime(rs.getString("prezime"));
				int brojPregleda = rs.getInt("broj_pregleda");
				rezultat.add(v.toString() + " | Broj pregleda: " + brojPregleda);
			}
		} catch (SQLException e) {
		} finally {
			dbConn.close(conn);
		}

		return rezultat;
	}

	// III Izvjestaj: Kavezi sa brojem životinja većim od prosjeka za proslijedjeni
	// tip kaveza (ili za sve kaveze)
	public List<String> izvjestajKaveziIznadProsjeka(String tipKaveza) {

		Connection conn = dbConn.open();
		List<String> rezultat = new ArrayList<>();
		if (conn == null)
			return rezultat;

		String sql = "SELECT k.id, k.lokacija, tk.naziv AS tip_naziv, " + "COUNT(z.id) AS broj_zivotinja "
				+ "FROM kavez k " + "JOIN tip_kaveza tk ON k.tip_kaveza_id = tk.id "
				+ "LEFT JOIN zivotinja z ON z.kavez_id = k.id ";

		if (tipKaveza != null && !tipKaveza.isEmpty()) {
			sql += "WHERE tk.naziv = '" + tipKaveza + "' ";
		}

		sql += "GROUP BY k.id, k.lokacija, tk.naziv " + "HAVING broj_zivotinja > ( " + "SELECT AVG(temp.broj) "
				+ "FROM ( " + "SELECT k2.id, COUNT(z2.id) AS broj " + "FROM kavez k2 "
				+ "JOIN tip_kaveza tk2 ON k2.tip_kaveza_id = tk2.id "
				+ "LEFT JOIN zivotinja z2 ON z2.kavez_id = k2.id ";

		if (tipKaveza != null && !tipKaveza.isEmpty()) {
			sql += "WHERE tk2.naziv = '" + tipKaveza + "' ";
		}

		sql += "GROUP BY k2.id " + ") AS temp " + ") " + "ORDER BY broj_zivotinja DESC";

		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				String lokacija = rs.getString("lokacija");
				String tipNaziv = rs.getString("tip_naziv");
				int brojZivotinja = rs.getInt("broj_zivotinja");

				rezultat.add("Kavez: " + lokacija + " | Tip: " + tipNaziv + " | Broj životinja: " + brojZivotinja);
			}
		} catch (SQLException e) {
		} finally {
			dbConn.close(conn);
		}

		return rezultat;
	}

}
