package me.passwordmanager.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import lombok.Getter;
import me.passwordmanager.MainApplication;

public class PasswordPromptController extends FxGuiController {

    @FXML
    private Text title;
    @FXML
    @Getter
    private PasswordField passwordField;

    @FXML
    private void onConfirm(ActionEvent actionEvent) {
        if(passwordField.getText().isEmpty()) return;
        MainApplication.getInstance().getStage().close();
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
