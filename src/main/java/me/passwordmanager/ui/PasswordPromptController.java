package me.passwordmanager.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class PasswordPromptController extends FxGui {

    @FXML
    public Text title;

    @FXML
    public void onAction(ActionEvent actionEvent) {
    }

    @Override
    public String xmlFileName() {
        return "password-prompt.fxml";
    }

    @Override
    public String initialTitle() {
        return "Enter password";
    }

    @Override
    public int initialWidth() {
        return 200;
    }

    @Override
    public int initialHeight() {
        return 80;
    }
}
