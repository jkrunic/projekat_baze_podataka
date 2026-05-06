package rs.raf.bp.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Klasa generisana uz pomoc AI alata (Claude, Anthropic).
 * Specifikacija projekta dozvoljava upotrebu AI alata za izradu izgleda GUI-ja.
 * Generisano na osnovu zahteva studenta koji opisuje strukturu forme i potrebne komponente.
 */

public class RegisterView extends JFrame {

    private JTextField tfUsername;
    private JPasswordField tfPassword;
    private JPasswordField tfPasswordConfirm;
    private JButton btnRegister;
    private JButton btnCancel;

    public RegisterView() {
        setTitle("Registracija - Administrator");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Naslov
        JLabel lblTitle = new JLabel("Registracija novog korisnika", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        // Korisnicko ime
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Korisnicko ime:"), gbc);
        tfUsername = new JTextField(15);
        gbc.gridx = 1;
        panel.add(tfUsername, gbc);

        // Lozinka
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Lozinka:"), gbc);
        tfPassword = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(tfPassword, gbc);

        // Potvrda lozinke
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Potvrda lozinke:"), gbc);
        tfPasswordConfirm = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(tfPasswordConfirm, gbc);

        // Dugmad
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnRegister = new JButton("Registruj se");
        btnCancel = new JButton("Otkazi");
        btnPanel.add(btnRegister);
        btnPanel.add(btnCancel);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        add(panel);
    }

    // Getteri za podatke iz forme

    public String getUsername() {
        return tfUsername.getText().trim();
    }

    public String getPassword() {
        return new String(tfPassword.getPassword());
    }

    public String getPasswordConfirm() {
        return new String(tfPasswordConfirm.getPassword());
    }

    // Listeneri koje controller postavlja

    public void addRegisterListener(ActionListener listener) {
        btnRegister.addActionListener(listener);
    }

    public void addCancelListener(ActionListener listener) {
        btnCancel.addActionListener(listener);
    }

    // Prikaz poruka

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Greska", JOptionPane.ERROR_MESSAGE);
    }

    public void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Upozorenje", JOptionPane.WARNING_MESSAGE);
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Uspeh", JOptionPane.INFORMATION_MESSAGE);
    }
}