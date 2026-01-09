package me.passwordmanager.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class CreateNewEntryMenuController extends FxGuiController {
    @Override
    public String xmlFileName() {
        return "create-new-entry.fxml";
    }

    @Override
    public String initialTitle() {
        return "Create new database entry";
    }

    @FXML
    public void onClickCreate(ActionEvent actionEvent) {
    }
}
