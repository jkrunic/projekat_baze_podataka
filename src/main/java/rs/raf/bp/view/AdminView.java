package rs.raf.bp.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class AdminView extends JFrame {

    //Tab 1: Pregled sesija
    private JTable tableSesije;
    private DefaultTableModel modelSesije;
    private JButton btnRefresh;


    public AdminView(String username) {
        setTitle("Admin panel - " + username);
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Pregled sesija", createPregledPanel());
        add(tabs);
    }

    // TAB 1: Pregled zakazanih sesija i eksperimenata
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

}
