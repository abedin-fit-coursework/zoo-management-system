package dao;

import db.DatabaseConnection;
import model.Pregled;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PregledDAO {
    private DatabaseConnection dbConn;

    public PregledDAO(DatabaseConnection dbConn) {
        this.dbConn = dbConn;
    }

    // Pomocne metode 

    public int findZivotinjaID(String vrsta, String latinskiNaziv) {
        Connection conn = dbConn.open();
        if (conn == null) return -1;

        String sql = "SELECT id FROM zivotinja WHERE vrsta = '" + vrsta + "' AND latinski_naziv = '" + latinskiNaziv + "'";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) return rs.getInt("id");
            return -1;

        } catch (SQLException e) {
            return -1;
        } finally {
            dbConn.close(conn);
        }
    }

    public int findVeterinarIDByJMBG(String jmbg) {
        Connection conn = dbConn.open();
        if (conn == null) return -1;

        String sql = "SELECT v.zaposleni_id AS id " +
                     "FROM veterinar v " +
                     "JOIN zaposleni z ON v.zaposleni_id = z.id " +
                     "WHERE z.jmbg = '" + jmbg + "'";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) return rs.getInt("id");
            return -1;

        } catch (SQLException e) {
            return -1;
        } finally {
            dbConn.close(conn);
        }
    }

    // CREATE
    public boolean insertPregled(LocalDate datumPregleda, String rezultat, String preporuke,
                                 int zivotinjaId, int veterinarId) {
        Connection conn = dbConn.open();
        if (conn == null) return false;

        String sql = "INSERT INTO pregled (datum_pregleda, rezultat, preporuke, zivotinja_id, veterinar_id) VALUES('" +
                datumPregleda + "', '" + rezultat + "', '" + preporuke + "', " +
                zivotinjaId + ", " + veterinarId + ")";

        try {
            Statement st = conn.createStatement();
            st.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            dbConn.close(conn);
        }
    }

    // READ: pregledi za sve zivotinje  
    public List<Pregled> getAllPregledi() {
        Connection conn = dbConn.open();
        List<Pregled> lista = new ArrayList<>();
        if (conn == null) return lista;

        String sql = "SELECT p.id, p.datum_pregleda, p.rezultat, p.preporuke, " +
                     "p.zivotinja_id, z.vrsta AS zivotinja_vrsta, " +
                     "p.veterinar_id, zap.ime AS veterinar_ime, zap.prezime AS veterinar_prezime " +
                     "FROM pregled p " +
                     "JOIN zivotinja z ON p.zivotinja_id = z.id " +
                     "JOIN veterinar v ON p.veterinar_id = v.zaposleni_id " +
                     "JOIN zaposleni zap ON v.zaposleni_id = zap.id " +
                     "ORDER BY p.datum_pregleda DESC";

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Pregled p = new Pregled();
                p.setId(rs.getInt("id"));
                p.setDatumPregleda(rs.getDate("datum_pregleda").toLocalDate());
                p.setRezultat(rs.getString("rezultat"));
                p.setPreporuke(rs.getString("preporuke"));

                p.setZivotinjaId(rs.getInt("zivotinja_id"));
                p.setZivotinjaVrsta(rs.getString("zivotinja_vrsta"));

                p.setVeterinarId(rs.getInt("veterinar_id"));
                p.setVeterinarIme(rs.getString("veterinar_ime"));
                p.setVeterinarPrezime(rs.getString("veterinar_prezime"));

                lista.add(p);
            }
        } catch (SQLException e) {
        } finally {
            dbConn.close(conn);
        }

        return lista;
    }

    // READ: pregledi za jednu zivotinju 
    public List<Pregled> getPreglediByZivotinjaId(int zivotinjaId) {
        Connection conn = dbConn.open();
        List<Pregled> lista = new ArrayList<>();
        if (conn == null) return lista;

        String sql = "SELECT p.id, p.datum_pregleda, p.rezultat, p.preporuke, " +
                     "p.zivotinja_id, z.vrsta AS zivotinja_vrsta, " +
                     "p.veterinar_id, zap.ime AS veterinar_ime, zap.prezime AS veterinar_prezime " +
                     "FROM pregled p " +
                     "JOIN zivotinja z ON p.zivotinja_id = z.id " +
                     "JOIN veterinar v ON p.veterinar_id = v.zaposleni_id " +
                     "JOIN zaposleni zap ON v.zaposleni_id = zap.id " +
                     "WHERE p.zivotinja_id = " + zivotinjaId + " " +
                     "ORDER BY p.datum_pregleda DESC";

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Pregled p = new Pregled();
                p.setId(rs.getInt("id"));
                p.setDatumPregleda(rs.getDate("datum_pregleda").toLocalDate());
                p.setRezultat(rs.getString("rezultat"));
                p.setPreporuke(rs.getString("preporuke"));

                p.setZivotinjaId(rs.getInt("zivotinja_id"));
                p.setZivotinjaVrsta(rs.getString("zivotinja_vrsta"));

                p.setVeterinarId(rs.getInt("veterinar_id"));
                p.setVeterinarIme(rs.getString("veterinar_ime"));
                p.setVeterinarPrezime(rs.getString("veterinar_prezime"));

                lista.add(p);
            }
        } catch (SQLException e) {
        } finally {
            dbConn.close(conn);
        }

        return lista;
    }

    // UPDATE
    public boolean updatePregled(int pregledId, LocalDate datumPregleda, String rezultat, String preporuke,
                                 int zivotinjaId, int veterinarId) {
        Connection conn = dbConn.open();
        if (conn == null) return false;

        String sql = "UPDATE pregled SET datum_pregleda = '" + datumPregleda +
                "', rezultat = '" + rezultat +
                "', preporuke = '" + preporuke +
                "', zivotinja_id = " + zivotinjaId +
                ", veterinar_id = " + veterinarId +
                " WHERE id = " + pregledId;

        try {
            Statement st = conn.createStatement();
            return st.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            return false;
        } finally {
            dbConn.close(conn);
        }
    }

    // DELETE
    public boolean deletePregled(int pregledId) {
        Connection conn = dbConn.open();
        if (conn == null) return false;

        String sql = "DELETE FROM pregled WHERE id = " + pregledId;

        try {
            Statement st = conn.createStatement();
            return st.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            return false;
        } finally {
            dbConn.close(conn);
        }
    }
}
