package dao;

import db.DatabaseConnection;
import model.Tura;
import model.ZivotinjaTura;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ZivotinjaTuraDAO {
    private DatabaseConnection dbConn;

    public ZivotinjaTuraDAO(DatabaseConnection dbConn) {
        this.dbConn = dbConn;
    }

    // Pomocne metode 
    
    public int findZivotinjaID(String vrsta, String latinskiNaziv) {
        Connection conn = dbConn.open();
        if (conn == null) return -1;

        String sql = "SELECT id FROM zivotinja WHERE vrsta = '" + vrsta + 
                     "' AND latinski_naziv = '" + latinskiNaziv + "'";
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

    public List<Tura> getTureByDatum(String datumTure) {
        Connection conn = dbConn.open();
        List<Tura> ture = new ArrayList<>();
        if (conn == null) return ture;

        String sql = "SELECT t.id, t.datum_ture, t.staratelj_id, " +
                     "z.ime AS staratelj_ime, z.prezime AS staratelj_prezime " +
                     "FROM tura t " +
                     "JOIN staratelj s ON t.staratelj_id = s.zaposleni_id " +
                     "JOIN zaposleni z ON s.zaposleni_id = z.id " +
                     "WHERE t.datum_ture = '" + datumTure + "' " +
                     "ORDER BY z.prezime, z.ime";

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Tura tura = new Tura();
                tura.setId(rs.getInt("id"));
                tura.setDatumTure(rs.getDate("datum_ture").toLocalDate());
                tura.setStarateljId(rs.getInt("staratelj_id"));
                tura.setStarateljIme(rs.getString("staratelj_ime"));
                tura.setStarateljPrezime(rs.getString("staratelj_prezime"));
                ture.add(tura);
            }
        } catch (SQLException e) {
        } finally {
            dbConn.close(conn);
        }

        return ture;
    }

    public boolean existsVeza(int zivotinjaId, int turaId) {
        Connection conn = dbConn.open();
        if (conn == null) return false;

        String sql = "SELECT id FROM zivotinja_tura WHERE zivotinja_id = " + 
                     zivotinjaId + " AND tura_id = " + turaId;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            return rs.next();
        } catch (SQLException e) {
            return false;
        } finally {
            dbConn.close(conn);
        }
    }

    // CREATE
    public boolean insertZivotinjaTura(int zivotinjaId, int turaId) {
        Connection conn = dbConn.open();
        if (conn == null) return false;

        String sql = "INSERT INTO zivotinja_tura (zivotinja_id, tura_id) VALUES(" + 
                     zivotinjaId + ", " + turaId + ")";
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

    // READ 
    public List<ZivotinjaTura> getAllZivotinjaTura() {
        Connection conn = dbConn.open();
        List<ZivotinjaTura> lista = new ArrayList<>();
        if (conn == null) return lista;

        String sql = "SELECT zt.id, zt.zivotinja_id, z.vrsta AS zivotinja_vrsta, " +
                     "zt.tura_id, t.datum_ture AS tura_datum " +
                     "FROM zivotinja_tura zt " +
                     "JOIN zivotinja z ON zt.zivotinja_id = z.id " +
                     "JOIN tura t ON zt.tura_id = t.id " +
                     "ORDER BY t.datum_ture, z.vrsta";

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                ZivotinjaTura zt = new ZivotinjaTura();
                zt.setId(rs.getInt("id"));
                zt.setZivotinjaId(rs.getInt("zivotinja_id"));
                zt.setZivotinjaVrsta(rs.getString("zivotinja_vrsta"));
                zt.setTuraId(rs.getInt("tura_id"));
                zt.setTuraDatum(rs.getString("tura_datum"));
                lista.add(zt);
            }
        } catch (SQLException e) {
        } finally {
            dbConn.close(conn);
        }

        return lista;
    }

    // UPDATE 
    public boolean updateZivotinjaTura(int vezaId, int noviZivotinjaId, int noviTuraId) {
        Connection conn = dbConn.open();
        if (conn == null) return false;

        String sql = "UPDATE zivotinja_tura SET zivotinja_id = " + noviZivotinjaId +
                     ", tura_id = " + noviTuraId +
                     " WHERE id = " + vezaId;

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
    public boolean deleteZivotinjaTura(int vezaId) {
        Connection conn = dbConn.open();
        if (conn == null) return false;

        String sql = "DELETE FROM zivotinja_tura WHERE id = " + vezaId;

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