package rs.raf.bp.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LaboratorijaModel {

    public List<String[]> getSveLaboratorije() {
        String query =
                "SELECT id_laboratorija, naziv_laboratorije " +
                        "FROM laboratorija " +
                        "ORDER BY id_laboratorija ASC";

        List<String[]> result = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String[] row = new String[2];
                row[0] = String.valueOf(rs.getInt("id_laboratorija"));
                row[1] = rs.getString("naziv_laboratorije");
                result.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public String[] getLaboratorijaDetalji(int idLab) {
        String query =
                "SELECT l.naziv_laboratorije, l.opis_lokacije, l.kapacitet_skl_rezerv, " +
                        "(SELECT COUNT(*) FROM izvodjenje i WHERE i.id_laboratorija = l.id_laboratorija) AS broj_izvodjenja, " +
                        "(SELECT COUNT(*) FROM alat a WHERE a.id_laboratorija = l.id_laboratorija) AS broj_alata, " +
                        "(SELECT COUNT(*) FROM izvodjenje i WHERE i.id_laboratorija = l.id_laboratorija " +
                        "  AND i.status_izvodjenja IN ('planirano', 'zapoceto')) AS broj_aktivnih " +
                        "FROM laboratorija l " +
                        "WHERE l.id_laboratorija = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idLab);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new String[]{
                            rs.getString("naziv_laboratorije"),
                            rs.getString("opis_lokacije"),
                            String.valueOf(rs.getInt("kapacitet_skl_rezerv")),
                            String.valueOf(rs.getInt("broj_izvodjenja")),
                            String.valueOf(rs.getInt("broj_alata")),
                            String.valueOf(rs.getInt("broj_aktivnih"))
                    };
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public String obrisiLaboratoriju(int idLab) {
        String call = "{CALL obrisi_laboratoriju(?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cs = conn.prepareCall(call)) {

            cs.setInt(1, idLab);

            boolean hasResultSet = cs.execute();
            if (hasResultSet) {
                try (ResultSet rs = cs.getResultSet()) {
                    if (rs.next()) {
                        return rs.getString("poruka");
                    }
                }
            }
            return "Procedura je izvrsena, ali nije vratila poruku.";
        } catch (SQLException e) {
            throw new RuntimeException("Greska prilikom brisanja laboratorije: " + e.getMessage(), e);
        }
    }

    public int brojIstrazivacaULab(int idLab) {
        String query = "SELECT COUNT(*) FROM tim_izvodjaca t " +
                "JOIN izvodjenje i ON t.id_izvodjenje = i.id_izvodjenje " +
                "WHERE i.id_laboratorija = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idLab);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }


}