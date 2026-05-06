package rs.raf.bp.controller;

import rs.raf.bp.model.UserFileManager;
import rs.raf.bp.view.RegisterView;

public class RegisterController {

    private RegisterView view;
    private UserFileManager userModel;

    public RegisterController(RegisterView view, UserFileManager userModel) {
        this.view = view;
        this.userModel = userModel;

        this.view.addRegisterListener(e -> handleRegister());
        this.view.addCancelListener(e -> view.dispose());
    }

    private void handleRegister() {
        String username = view.getUsername();
        String password = view.getPassword();
        String passwordConfirm = view.getPasswordConfirm();

        if (username.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            view.showWarning("Popunite sva polja.");
            return;
        }
        if (!password.equals(passwordConfirm)) {
            view.showError("Lozinke se ne poklapaju.");
            return;
        }

        if (password.length() < 4) {
            view.showWarning("Lozinka mora imati najmanje 4 karaktera.");
            return;
        }

        if (username.contains(";") || password.contains(";")) {
            view.showError("Korisnicko ime i lozinka ne smeju sadrzati znak ';'.");
            return;
        }

        if (userModel.userExists(username)) {
            view.showError("Korisnik sa tim imenom vec postoji.");
            return;
        }

        if (userModel.register(username, password)) {
            view.showSuccess("Uspesna registracija! Mozete se prijaviti.");
            view.dispose();
        } else {
            view.showError("Greska prilikom registracije. Pokusajte ponovo.");
        }
    }
}