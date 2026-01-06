package me.passwordmanager.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CreateNewMenuController extends FxGuiController {

    @FXML
    public TextField nameField;
    @FXML
    public TextField directoryField;

    @FXML
    public void createButtonClick(ActionEvent actionEvent) {
    }

    @Override
    public String xmlFileName() {
        return "create-new.fxml";
    }

    @Override
    public String initialTitle() {
        return "Create new database";
    }

    @Override
    public int initialWidth() {
        return 320;
    }

    @Override
    public int initialHeight() {
        return 340;
    }
}
