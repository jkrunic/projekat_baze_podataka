package rs.raf.bp.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Klasa generisana uz pomoc AI alata (Claude, Anthropic).
 * Specifikacija projekta dozvoljava upotrebu AI alata za izradu izgleda GUI-ja.
 * Generisano na osnovu zahteva studenta koji opisuje strukturu forme i potrebne komponente.
 */
public class AdminView extends JFrame {

    // Tab 1: Pregled sesija
    private JTable tableSesije;
    private DefaultTableModel modelSesije;
    private JButton btnRefresh;

    // Tab 2: Izmena sesije
    private JComboBox<String> comboSesijaIds;
    private JTextField tfDatum;
    private JTextField tfPocetak;
    private JTextField tfZavrsetak;
    private JTextField tfBrojDataset;
    private JButton btnSaveSesija;
    private JLabel lblInfoSesija;

    // Tab 3: Brisanje laboratorije
    private JComboBox<String[]> comboLaboratorije;
    private JTextArea taLabDetalji;
    private JButton btnObrisiLab;
    private Consumer<Integer> onLaboratorijaSelected;

    public AdminView(String username) {
        setTitle("Admin panel - " + username);
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Pregled sesija", createPregledPanel());
        tabs.addTab("Izmena sesije", createIzmenaPanel());
        tabs.addTab("Brisanje laboratorije", createBrisanjePanel());
        add(tabs);
    }

    // ============================================================
    // TAB 1: Pregled zakazanih sesija i eksperimenata
    // ============================================================
    private JPanel createPregledPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lbl = new JLabel("Zakazane sesije i eksperimenti");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(lbl, BorderLayout.NORTH);

        String[] columns = {"ID sesije", "Datum", "Pocetak", "Zavrsetak",
                "Br. dataset", "Eksperiment", "Laboratorija", "Status"};
        modelSesije = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableSesije = new JTable(modelSesije);
        tableSesije.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scroll = new JScrollPane(tableSesije);
        panel.add(scroll, BorderLayout.CENTER);

        btnRefresh = new JButton("Osvezi");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnRefresh);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ============================================================
    // TAB 2: Izmena podataka o sesiji
    // ============================================================
    private JPanel createIzmenaPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Izmena podataka o sesiji");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("ID sesije:"), gbc);
        comboSesijaIds = new JComboBox<>();
        gbc.gridx = 1;
        panel.add(comboSesijaIds, gbc);

        lblInfoSesija = new JLabel(" ");
        lblInfoSesija.setFont(new Font("SansSerif", Font.ITALIC, 12));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(lblInfoSesija, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Datum (YYYY-MM-DD):"), gbc);
        tfDatum = new JTextField(15);
        gbc.gridx = 1;
        panel.add(tfDatum, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Vreme pocetka (HH:MM:SS):"), gbc);
        tfPocetak = new JTextField(15);
        gbc.gridx = 1;
        panel.add(tfPocetak, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Vreme zavrsetka (HH:MM:SS):"), gbc);
        tfZavrsetak = new JTextField(15);
        gbc.gridx = 1;
        panel.add(tfZavrsetak, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Broj dataset:"), gbc);
        tfBrojDataset = new JTextField(15);
        gbc.gridx = 1;
        panel.add(tfBrojDataset, gbc);

        btnSaveSesija = new JButton("Sacuvaj izmene");
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnSaveSesija, gbc);


        return panel;
    }

    // ============================================================
    // TAB 3: Brisanje laboratorije
    // ============================================================
    private JPanel createBrisanjePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Brisanje laboratorije");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(lblTitle, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        JPanel topInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topInputPanel.add(new JLabel("Izaberi laboratoriju:"));
        comboLaboratorije = new JComboBox<>();

        comboLaboratorije.setPreferredSize(new Dimension(400, 25));
        comboLaboratorije.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                if (value instanceof String[] lab) {
                    value = lab[0] + " - " + lab[1];
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        topInputPanel.add(comboLaboratorije);
        centerPanel.add(topInputPanel, BorderLayout.NORTH);

        taLabDetalji = new JTextArea(8, 50);
        taLabDetalji.setEditable(false);
        taLabDetalji.setBorder(BorderFactory.createTitledBorder("Detalji laboratorije"));
        taLabDetalji.setFont(new Font("Monospaced", Font.PLAIN, 12));
        centerPanel.add(new JScrollPane(taLabDetalji), BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);

        btnObrisiLab = new JButton("Obrisi laboratoriju");
        btnObrisiLab.setForeground(Color.RED);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnObrisiLab);
        panel.add(bottomPanel, BorderLayout.SOUTH);


        comboLaboratorije.addActionListener(e -> {
            if (onLaboratorijaSelected != null) {
                String[] selected = (String[]) comboLaboratorije.getSelectedItem();
                if (selected != null) {
                    onLaboratorijaSelected.accept(Integer.parseInt(selected[0]));
                }
            }
        });

        return panel;
    }

    // ============================================================
    // PUBLIC METODE - interface za AdminController
    // ============================================================

    // --- Tab 1 ---
    public void setSesije(List<Object[]> sesije) {
        modelSesije.setRowCount(0);
        for (Object[] row : sesije) {
            modelSesije.addRow(row);
        }
    }

    public void addRefreshListener(ActionListener listener) {
        btnRefresh.addActionListener(listener);
    }

    public DefaultTableModel getTableModel() { return modelSesije; }

    // --- Tab 2 ---
    public void setSesijaIds(List<String> ids) {
        comboSesijaIds.removeAllItems();
        for (String id : ids) {
            comboSesijaIds.addItem(id);
        }
    }

    public void addSesijaId(String id) { comboSesijaIds.addItem(id); }

    public void clearSesijaIds() { comboSesijaIds.removeAllItems(); }

    public String getSelectedSesijaId() {
        return (String) comboSesijaIds.getSelectedItem();
    }

    public void setSesijaDetalji(String datum, String pocetak, String zavrsetak, String brojDataset) {
        tfDatum.setText(datum);
        tfPocetak.setText(pocetak);
        tfZavrsetak.setText(zavrsetak);
        tfBrojDataset.setText(brojDataset);
    }

    public String getDatum() {
        return tfDatum.getText().trim();
    }

    public String getPocetak() {
        return tfPocetak.getText().trim();
    }

    public String getZavrsetak() {
        return tfZavrsetak.getText().trim();
    }

    public String getBrojDataset() {
        return tfBrojDataset.getText().trim();
    }

    public void setDatum(String val)       { tfDatum.setText(val); }
    public void setPocetak(String val)     { tfPocetak.setText(val); }
    public void setZavrsetak(String val)   { tfZavrsetak.setText(val); }
    public void setBrojDataset(String val) { tfBrojDataset.setText(val); }
    public void setInfoSesija(String text) { lblInfoSesija.setText(text); }

    public void addSesijaSelectedListener(ActionListener listener) {
        comboSesijaIds.addActionListener(listener);
    }

    public void addSaveSesijaListener(ActionListener listener) {
        btnSaveSesija.addActionListener(listener);
    }

    // --- Tab 3 ---
    public void setOnLaboratorijaSelected(Consumer<Integer> listener) {
        this.onLaboratorijaSelected = listener;
    }

    public void prikaziDetalje(String[] detalji) {
        if (detalji == null) {
            taLabDetalji.setText("Nema podataka.");
            return;
        }
        taLabDetalji.setText(
                "Naziv:            " + detalji[0] + "\n" +
                        "Lokacija:         " + detalji[1] + "\n" +
                        "Kapacitet:        " + detalji[2] + "\n" +
                        "Izvodjenja:       " + detalji[3] + "\n" +
                        "Alata:            " + detalji[4] + "\n" +
                        "Aktivnih:         " + detalji[5]
        );
    }

    public String[] getSelectedLaboratorija() {
        return (String[]) comboLaboratorije.getSelectedItem();
    }

    public void clearLaboratorije() { comboLaboratorije.removeAllItems(); }
    public void addLaboratorija(String[] lab) { comboLaboratorije.addItem(lab); }


    public void setLabDetalji(String detalji) {
        taLabDetalji.setText(detalji);
    }

    public void addLabSelectedListener(ActionListener listener) {
        comboLaboratorije.addActionListener(listener);
    }

    public void addObrisiLabListener(ActionListener listener) {
        btnObrisiLab.addActionListener(listener);
    }

    // --- Univerzalni dijalozi ---
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Greska", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Uspeh", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Upozorenje", JOptionPane.WARNING_MESSAGE);
    }

    public boolean showConfirm(String message) {
        int response = JOptionPane.showConfirmDialog(this, message, "Potvrda",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        return response == JOptionPane.YES_OPTION;
    }

    public boolean confirmDelete(String labName) {
        int result = JOptionPane.showConfirmDialog(this,
                "Da li ste sigurni da zelite da obrisete laboratoriju '" + labName + "'?",
                "Potvrda brisanja", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

}