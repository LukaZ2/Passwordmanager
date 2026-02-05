package me.passwordmanager.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import me.passwordmanager.MainApplication;
import me.passwordmanager.file.DatabaseFile;
import me.passwordmanager.file.exception.IncorrectFileTypeException;
import me.passwordmanager.ui.util.InfoGui;
import me.passwordmanager.ui.util.TextLimiter;

import java.io.FileNotFoundException;

/**
 * The Main menu controller.
 */
public class MainMenuController extends FxGuiController {

    @Override
    public String xmlFileName() {
        return "main-menu.fxml";
    }

    @Override
    public String initialTitle() {
        return "Main menu";
    }

    @FXML
    public Text title;
    @FXML
    public TextField fileField;

    @FXML
    public void initialize() {
        fileField.setText(recentPath);
        fileField.setTextFormatter(TextLimiter.limit(50));
    }

    private final String recentPath;

    public MainMenuController() {
        this("");
    }

    public MainMenuController(String recentPath) {
        this.recentPath = recentPath;
    }

    //Called when the choose file button is clicked, opens the operating system's file chooser
    public void chooseFileButtonClick(ActionEvent actionEvent) {
        var fileChooser = new FileChooser();
        fileChooser.setTitle("Open database");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Database file", "*." + MainApplication.DATABASE_FILE_EXTENSION));
        var file = fileChooser.showOpenDialog(MainApplication.getInstance().getStage());
        if(file == null) return;
        fileField.setText(file.getAbsolutePath());
    }

    //Called when the create new file button is clicked, opens create new file menu
    public void createNewFileButtonClick(ActionEvent actionEvent) {
        new CreateNewDatabaseMenuController().display(createModalSubstage());
    }

    //Called when the open selected button is clicked, tries to open the selected database
    public void openSelectedButtonClick(ActionEvent actionEvent) {
        var databaseFile = new DatabaseFile(fileField.getText());

        try {
            databaseFile.checkFilePath();
        } catch (FileNotFoundException e) {
            new InfoGui(String.format("\"%s\" does not exist", fileField.getText())).display(getStage());
            return;
        } catch (IncorrectFileTypeException e) {
            new InfoGui(String.format("\"%s\" is not a valid database file", fileField.getText())).display(getStage());
            return;
        }

        new PasswordPromptController(databaseFile).display(MainApplication.getInstance().createModalSubstage());
    }
}
