package me.passwordmanager.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import me.passwordmanager.MainApplication;
import me.passwordmanager.file.DatabaseFile;
import me.passwordmanager.file.FileUtils;
import me.passwordmanager.file.exception.IncorrectFileTypeException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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
    public Text msg;
    @FXML
    public Text title;
    @FXML
    public TextField fileField;

    @FXML
    public void initialize() {
        msg.setVisible(false);
    }

    public void setMessage(String text) {
        msg.setText(text.replace("%t", fileField.getText()));
        msg.setFill(Color.RED);
        msg.setVisible(true);
    }

    public MainMenuController() {}

    public void chooseFileButtonClick(ActionEvent actionEvent) {
        var fileChooser = new FileChooser();
        fileChooser.setTitle("Open database");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Database file", "*." + MainApplication.DATABASE_FILE_EXTENSION));
        var file = fileChooser.showOpenDialog(MainApplication.getInstance().getStage());
        if(file == null) return;
        fileField.setText(file.getAbsolutePath());
    }

    public void createNewFileButtonClick(ActionEvent actionEvent) {
    }

    public void openSelectedButtonClick(ActionEvent actionEvent) {
        var databaseFile = new DatabaseFile(fileField.getText());

        try {
            databaseFile.checkFilePath();
        } catch (FileNotFoundException e) {
            setMessage("\"%t\" does not exist");
            return;
        } catch (IncorrectFileTypeException e) {
            setMessage("\"%t\" is not a valid database file");
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        //TODO: Password prompt + open
        var passwordPromptGui = new PasswordPromptController();
        MainApplication.getInstance().getGuiHandler().openGui(passwordPromptGui);

    }
}
