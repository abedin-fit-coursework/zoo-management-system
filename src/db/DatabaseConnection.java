package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private String url;
    private String user;
    private String pass;

    public DatabaseConnection(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    public Connection open() {
        try {
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            System.out.println("Greska prilikom konektovanja na bazu!");
            // e.printStackTrace();
            return null;
        }
    }

    public boolean close(Connection c) {
    	if (c == null) return true;
        try {
            c.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Greska pri zatvaranju konekcije!");
            return false;
        }
    }
}