package rs.raf.bp.controller;


import rs.raf.bp.model.UserFileManager;
import rs.raf.bp.view.LoginView;

public class LoginController {

    private LoginView view;
    private UserFileManager userModel;

    public LoginController(LoginView view, UserFileManager userModel) {
        this.view = view;
        this.userModel = userModel;

        this.view.addLoginListener(e -> handleLogin());
        //Dovrsi RegisterListener
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
            //Dovrsi adminView i AdminController
        } else {
            view.showError("Pogresno korisnicko ime ili lozinka.");
        }
    }

}
