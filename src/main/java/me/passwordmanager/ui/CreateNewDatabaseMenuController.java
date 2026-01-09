package me.passwordmanager.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import me.passwordmanager.MainApplication;
import me.passwordmanager.file.DatabaseFile;
import me.passwordmanager.file.exception.FileCreateException;

import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.regex.Pattern;

public class CreateNewDatabaseMenuController extends FxGuiController {

    public static final Pattern ALLOWED_FILENAME = Pattern.compile("[a-zA-Z0-9\\-_ ]+");
    public static final int MINIMUM_PASSWORD_LENGTH = 8;

    @FXML
    public TextField nameField;
    @FXML
    public TextField directoryField;
    @FXML
    public TextField passwordField;
    @FXML
    public TextField confirmPasswordField;

    @FXML
    public void createButtonClick(ActionEvent actionEvent) {
        if(!ALLOWED_FILENAME.matcher(nameField.getText()).find()) {
            new InfoGui("Invalid filename!").display(getStage());
            return;
        }
        var directory = new File(directoryField.getText());
        if(!directory.exists()) {
            new InfoGui(String.format("Directory \"%s\" does not exist!", directoryField.getText())).display(getStage());
            return;
        }
        if(!directory.isDirectory()) {
            new InfoGui(String.format("\"%s\" is not a directory!", directoryField.getText())).display(getStage());
            return;
        }
        if(passwordField.getText().length() < MINIMUM_PASSWORD_LENGTH) {
            new InfoGui(String.format("The password has to be at least %d long!", MINIMUM_PASSWORD_LENGTH)).display(getStage());
            return;
        }
        if(!passwordField.getText().equals(confirmPasswordField.getText())) {
            new InfoGui("The passwords do not match!").display(getStage());
            return;
        }

        DatabaseFile databaseFile;
        try {
            databaseFile = DatabaseFile.createNewDatabase(directoryField.getText(), nameField.getText(), passwordField.getText());
        } catch (FileCreateException e) {
            new InfoGui(e.getMessage()).display(getStage());
            return;
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException |
                 IOException e) {
            throw new RuntimeException(e);
        }
        getStage().close();

        new DatabaseMenuController(databaseFile).display(MainApplication.getInstance().getStage());
    }

    @Override
    public String xmlFileName() {
        return "create-new-database.fxml";
    }

    @Override
    public String initialTitle() {
        return "Create new database";
    }
}
