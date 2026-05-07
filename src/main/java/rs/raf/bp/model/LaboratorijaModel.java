package rs.raf.bp.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LaboratorijaModel {


    public List<Object[]> getSveLaboratorije() {
        String query =
                "SELECT id_laboratorija, naziv_laboratorije " +
                        "FROM laboratorija " +
                        "ORDER BY id_laboratorija ASC";

        List<Object[]> result = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Object[] row = new Object[2];
                row[0] = rs.getInt("id_laboratorija");
                row[1] = rs.getString("naziv_laboratorije");
                result.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }


    public Object[] getLaboratorijaDetalji(int idLab) {
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
                    return new Object[]{
                            rs.getString("naziv_laboratorije"),
                            rs.getString("opis_lokacije"),
                            rs.getInt("kapacitet_skl_rezerv"),
                            rs.getInt("broj_izvodjenja"),
                            rs.getInt("broj_alata"),
                            rs.getInt("broj_aktivnih")
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

            // Procedura vraca SELECT sa porukom kao result set
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
}