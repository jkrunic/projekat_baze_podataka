package rs.raf.bp;

import rs.raf.bp.model.DatabaseConnection;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Konekcija sa bazom uspesno uspostavljena.");
            }
        } catch (Exception e) {
            System.out.println("Greska pri povezivanju sa bazom: " + e.getMessage());
        }
    }
}