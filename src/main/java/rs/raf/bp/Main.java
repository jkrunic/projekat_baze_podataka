package rs.raf.bp;

import rs.raf.bp.controller.LoginController;
import rs.raf.bp.model.DatabaseConnection;
import rs.raf.bp.model.UserFileManager;
import rs.raf.bp.view.LoginView;

import javax.swing.*;
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

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // koristi podrazumevani izgled
        }

        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            UserFileManager userModel = new UserFileManager();
            new LoginController(loginView, userModel);
            loginView.setVisible(true);
        });
    }
}