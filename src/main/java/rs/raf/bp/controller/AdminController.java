package rs.raf.bp.controller;



import rs.raf.bp.model.LaboratorijaModel;
import rs.raf.bp.model.SesijaModel;
import rs.raf.bp.view.AdminView;

import java.util.List;

public class AdminController {

    private AdminView view;
    private SesijaModel sesijaModel;
    private LaboratorijaModel labModel;

    public AdminController(AdminView view, SesijaModel sesijaModel, LaboratorijaModel labModel) {
        this.view = view;
        this.sesijaModel = sesijaModel;
        this.labModel = labModel;

        //Postavi listenere
        this.view.addRefreshListener(e -> loadSesije());
        this.view.addSesijaSelectedListener(e -> loadSesijaDetalji());
        this.view.addSaveSesijaListener(e -> updateSesija());
        this.view.addObrisiLabListener(e -> deleteLaboratorija());
        this.view.setOnLaboratorijaSelected(labId -> {
            String[] detalji = labModel.getLaboratorijaDetalji(labId);
            view.prikaziDetalje(detalji);
        });

        //Ucitaj podatke pri otvaranju
        loadSesije();
        loadSesijaIds();
        loadLaboratorije();
    }

    //Tab 1: Pregled sesija

    private void loadSesije() {
        view.getTableModel().setRowCount(0);
        List<Object[]> sesije = sesijaModel.getSveSesije();
        for (Object[] row : sesije) {
            view.getTableModel().addRow(row);
        }
    }

    //Tab 2: Izmena sesije

    private void loadSesijaIds() {
        view.clearSesijaIds();
        List<Integer> ids = sesijaModel.getSesijaIds();
        for (int id : ids) {
            view.addSesijaId(String.valueOf(id));
        }
    }

    private void loadSesijaDetalji() {
        String selected = view.getSelectedSesijaId();
        if (selected == null) return;

        int id = Integer.parseInt(selected);
        Object[] detalji = sesijaModel.getSesijaDetalji(id);

        if (detalji != null) {
            view.setDatum((String) detalji[0]);
            view.setPocetak((String) detalji[1]);
            view.setZavrsetak((String) detalji[2]);
            view.setBrojDataset(String.valueOf(detalji[3]));
            view.setInfoSesija("Eksperiment: " + detalji[4] +
                    " | Laboratorija: " + detalji[5]);
        }
    }

    private void updateSesija() {
        String selected = view.getSelectedSesijaId();
        if (selected == null) return;

        String datum = view.getDatum();
        String pocetak = view.getPocetak();
        String zavrsetak = view.getZavrsetak();
        String brojStr = view.getBrojDataset();

        if (datum.isEmpty() || pocetak.isEmpty() || zavrsetak.isEmpty() || brojStr.isEmpty()) {
            view.showWarning("Sva polja moraju biti popunjena.");
            return;
        }

        try {
            int id = Integer.parseInt(selected);
            int brojDataset = Integer.parseInt(brojStr);

            if (sesijaModel.updateSesija(id, datum, pocetak, zavrsetak, brojDataset)) {
                view.showSuccess("Sesija uspesno azurirana.");
                loadSesije();
            } else {
                view.showError("Azuriranje nije uspelo.");
            }
        } catch (NumberFormatException e) {
            view.showWarning("Broj datasetova mora biti ceo broj.");
        }
    }

    //Tab 3: Brisanje laboratorije

    private void loadLaboratorije() {
        view.clearLaboratorije();
        List<String[]> labs = labModel.getSveLaboratorije();
        for (String[] lab : labs) {
            view.addLaboratorija(lab);
        }
    }

    private void deleteLaboratorija() {
        String[] selected = view.getSelectedLaboratorija();
        if (selected == null) return;

        int labId = Integer.parseInt(selected[0]);
        String labName = selected[1];

        //Provera da li u laboratoriji radi ijedan istrazivac
        int count = labModel.brojIstrazivacaULab(labId);
        if (count > 0) {
            view.showWarning("Laboratorija '" + labName + "' ne moze biti obrisana jer " +
                    "u njoj radi " + count + " istrazivac(a).");
            return;
        }

        //Potvrda
        if (!view.confirmDelete(labName)) return;

        // Brisanje
        String poruka = labModel.obrisiLaboratoriju(labId);
        if (poruka != null) {
            view.showSuccess(poruka);
            loadLaboratorije();
            loadSesije();
            loadSesijaIds();
        } else {
            view.showError("Greska pri brisanju laboratorije.");
        }
    }


}
