package dao;

import db.DatabaseConnection;
import model.Zivotinja;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ZivotinjaDAO {
    private DatabaseConnection dbConn;

    public ZivotinjaDAO(DatabaseConnection dbConn) {
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

    public boolean existsZivotinja(String vrsta, String latinskiNaziv) {
        if (vrsta == null || latinskiNaziv == null) return false;
        return findZivotinjaID(vrsta.trim(), latinskiNaziv.trim()) != -1;
    }

    public int findStatusID(String statusNaziv) {
        Connection conn = dbConn.open();
        if (conn == null) return -1;

        String sql = "SELECT id FROM status_zivotinje WHERE naziv = '" + statusNaziv + "'";
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

    public int findKavezID(String lokacija) {
        Connection conn = dbConn.open();
        if (conn == null) return -1;

        String sql = "SELECT id FROM kavez WHERE lokacija = '" + lokacija + "'";
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

    public boolean hasPregledi(int zivotinjaId) {
        Connection conn = dbConn.open();
        if (conn == null) return false;

        String sql = "SELECT COUNT(*) AS broj FROM pregled WHERE zivotinja_id = " + zivotinjaId;
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

    public boolean hasTure(int zivotinjaId) {
        Connection conn = dbConn.open();
        if (conn == null) return false;

        String sql = "SELECT COUNT(*) AS broj FROM zivotinja_tura WHERE zivotinja_id = " + zivotinjaId;
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

    public boolean hasStaratelje(int zivotinjaId) {
        Connection conn = dbConn.open();
        if (conn == null) return false;

        String sql = "SELECT COUNT(*) AS broj FROM zivotinja_staratelj WHERE zivotinja_id = " + zivotinjaId;
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
    public boolean insertZivotinja(String latinskiNaziv, String vrsta, LocalDate datumDolaska,
                                   int statusId, int kavezId, int kategorijaId) {
        Connection conn = dbConn.open();
        if (conn == null) return false;

        String sql = "INSERT INTO zivotinja (latinski_naziv, vrsta, datum_dolaska, status_id, kavez_id, kategorija_id) VALUES('" +
                latinskiNaziv + "', '" + vrsta + "', '" + datumDolaska + "', " +
                statusId + ", " + kavezId + ", " + kategorijaId + ")";

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
    public List<Zivotinja> getAllZivotinje() {
        Connection conn = dbConn.open();
        List<Zivotinja> lista = new ArrayList<>();
        if (conn == null) return lista;

        String sql = "SELECT z.id, z.latinski_naziv, z.vrsta, z.datum_dolaska, " +
                     "sz.id AS status_id, sz.naziv AS status_naziv, " +
                     "k.id AS kavez_id, k.lokacija AS kavez_lokacija, " +
                     "kat.id AS kategorija_id, kat.naziv AS kategorija_naziv " +
                     "FROM zivotinja z " +
                     "JOIN status_zivotinje sz ON z.status_id = sz.id " +
                     "JOIN kavez k ON z.kavez_id = k.id " +
                     "JOIN kategorija kat ON z.kategorija_id = kat.id " +
                     "ORDER BY z.vrsta";

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Zivotinja z = new Zivotinja();
                z.setId(rs.getInt("id"));
                z.setLatinskiNaziv(rs.getString("latinski_naziv"));  
                z.setVrsta(rs.getString("vrsta"));
                z.setDatumDolaska(rs.getDate("datum_dolaska").toLocalDate());  

                z.setStatusId(rs.getInt("status_id"));
                z.setStatusNaziv(rs.getString("status_naziv"));

                z.setKavezId(rs.getInt("kavez_id"));
                z.setKavezLokacija(rs.getString("kavez_lokacija"));

                z.setKategorijaId(rs.getInt("kategorija_id"));
                z.setKategorijaNaziv(rs.getString("kategorija_naziv"));

                lista.add(z);
            }
        } catch (SQLException e) {
        } finally {
            dbConn.close(conn);
        }

        return lista;
    }

    // UPDATE
    public boolean updateZivotinja(int zivotinjaId, String latinskiNaziv, String vrsta, LocalDate datumDolaska,
                                   int statusId, int kavezId, int kategorijaId) {
        Connection conn = dbConn.open();
        if (conn == null) return false;

        String sql = "UPDATE zivotinja SET latinski_naziv = '" + latinskiNaziv +
                "', vrsta = '" + vrsta +
                "', datum_dolaska = '" + datumDolaska +
                "', status_id = " + statusId +
                ", kavez_id = " + kavezId +
                ", kategorija_id = " + kategorijaId +
                " WHERE id = " + zivotinjaId;

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
    public boolean deleteZivotinja(int zivotinjaId) {
        Connection conn = dbConn.open();
        if (conn == null) return false;

        String sql = "DELETE FROM zivotinja WHERE id = " + zivotinjaId;

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
