package rs.raf.bp.controller;

import rs.raf.bp.model.LaboratorijaModel;
import rs.raf.bp.model.SesijaModel;
import rs.raf.bp.model.UserFileManager;
import rs.raf.bp.view.AdminView;
import rs.raf.bp.view.LoginView;
import rs.raf.bp.view.RegisterView;

public class LoginController {

    private LoginView view;
    private UserFileManager userModel;

    public LoginController(LoginView view, UserFileManager userModel) {
        this.view = view;
        this.userModel = userModel;

        this.view.addLoginListener(e -> handleLogin());
        this.view.addRegisterListener(e -> handleRegister());
    }

    private void handleLogin() {
        String username = view.getUsername();
        String password = view.getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            view.showWarning("Unesite korisnicko ime i lozinku.");
            return;
        }

        if (userModel.login(username, password)) {
            view.showSuccess("Uspesna prijava! Dobrodosli, " + username + ".");
            view.dispose();
            AdminView adminView = new AdminView(username);
            SesijaModel sesijaModel = new SesijaModel();
            LaboratorijaModel labModel = new LaboratorijaModel();
            new AdminController(adminView, sesijaModel, labModel);
            adminView.setVisible(true);
        } else {
            view.showError("Pogresno korisnicko ime ili lozinka.");
        }
    }

    private void handleRegister() {
        RegisterView registerView = new RegisterView();
        new RegisterController(registerView, userModel);
        registerView.setVisible(true);
    }
}