package dao;

import db.DatabaseConnection;
import model.Kategorija;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class KategorijaDAO {
    private DatabaseConnection dbConn;

    public KategorijaDAO(DatabaseConnection dbConn) {
        this.dbConn = dbConn;
    }

    // Pomocne metode 
    
    public int findKategorijaID(String naziv) {
        Connection conn = dbConn.open();
        if (conn == null) return -1;

        String sql = "SELECT id FROM kategorija WHERE naziv = '" + naziv + "'";
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

    public boolean existsByNaziv(String naziv) {
        if (naziv == null) return false;
        return findKategorijaID(naziv.trim()) != -1;
    }

    public boolean hasPodkategorije(int kategorijaId) {
        Connection conn = dbConn.open();
        if (conn == null) return false;

        String sql = "SELECT COUNT(*) AS broj FROM kategorija WHERE nadkategorija_id = " + kategorijaId;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) return rs.getInt("broj") > 0;
            return false;

        } catch (SQLException e) {
            return false;
        } finally {
            dbConn.close(conn);
        }
    }

    public boolean hasZivotinje(int kategorijaId) {
        Connection conn = dbConn.open();
        if (conn == null) return false;

        String sql = "SELECT COUNT(*) AS broj FROM zivotinja WHERE kategorija_id = " + kategorijaId;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) return rs.getInt("broj") > 0;
            return false;

        } catch (SQLException e) {
            return false;
        } finally {
            dbConn.close(conn);
        }
    }

    // CREATE
    public boolean insertKategorija(String naziv, Integer nadkategorijaId) {
        Connection conn = dbConn.open();
        if (conn == null) return false;

        String sql = "INSERT INTO kategorija (naziv, nadkategorija_id) VALUES('" +
                naziv + "', " + (nadkategorijaId == null ? "NULL" : nadkategorijaId) + ")";

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
    public List<Kategorija> getAllKategorije() {
        Connection conn = dbConn.open();
        List<Kategorija> kategorije = new ArrayList<>();
        if (conn == null) return kategorije;

        String sql = "SELECT k.id, k.naziv, k.nadkategorija_id, nk.naziv AS nadkategorija_naziv " +
                     "FROM kategorija k " +
                     "LEFT JOIN kategorija nk ON k.nadkategorija_id = nk.id " +
                     "ORDER BY k.naziv";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Kategorija k = new Kategorija();
                k.setId(rs.getInt("id"));
                k.setNaziv(rs.getString("naziv"));
                k.setNadkategorijaId((Integer) rs.getObject("nadkategorija_id"));
                k.setNadkategorijaNaziv(rs.getString("nadkategorija_naziv"));
                kategorije.add(k);
            }
        } catch (SQLException e) {
        } finally {
            dbConn.close(conn);
        }

        return kategorije;
    }

    // UPDATE
    public boolean updateKategorija(int kategorijaId, String noviNaziv, Integer novaNadkategorijaId) {
        Connection conn = dbConn.open();
        if (conn == null) return false;

        String sql = "UPDATE kategorija SET naziv = '" + noviNaziv + "', nadkategorija_id = " +
                (novaNadkategorijaId == null ? "NULL" : novaNadkategorijaId) +
                " WHERE id = " + kategorijaId;

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
    public boolean deleteKategorija(int kategorijaId) {
        Connection conn = dbConn.open();
        if (conn == null) return false;

        String sql = "DELETE FROM kategorija WHERE id = " + kategorijaId;

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
