package me.passwordmanager.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import me.passwordmanager.MainApplication;

import java.io.File;

public class MainMenuController extends FxGui {

    @Override
    public String xmlFileName() {
        return "main-menu.fxml";
    }

    @Override
    public String initialTitle() {
        return "Main menu";
    }

    @Override
    public int initialWidth() {
        return 320;
    }

    @Override
    public int initialHeight() {
        return 240;
    }

    @FXML
    public Text title;
    @FXML
    public TextField fileField = new TextField();

    public String test;

    @FXML
    public void initialize() {
        title.setText(test);
    }

    public MainMenuController() {}

    public MainMenuController(String test) {
        this.test = test;
    }

    public void chooseFileButtonClick(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open database");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Database file", "*." + MainApplication.DATABASE_FILE_EXTENSION));
        File file = fileChooser.showOpenDialog(MainApplication.getInstance().getStage());
        if(file == null) return;
        fileField.setText(file.getAbsolutePath());
    }

    public void createNewFileButtonClick(ActionEvent actionEvent) {
    }

    public void openSelectedButtonClick(ActionEvent actionEvent) {
    }
}
