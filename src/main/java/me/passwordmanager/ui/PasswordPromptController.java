package me.passwordmanager.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import lombok.Getter;
import me.passwordmanager.MainApplication;
import me.passwordmanager.file.DatabaseFile;
import me.passwordmanager.file.exception.AtomicFileCorruptedException;
import me.passwordmanager.file.exception.AtomicFileLockedException;
import me.passwordmanager.file.exception.InvalidPasswordException;

import java.io.IOException;

public class PasswordPromptController extends FxGuiController {

    @FXML
    public Text title;
    @FXML
    public PasswordField passwordField;

    @Getter
    private DatabaseFile databaseFile;

    public PasswordPromptController(DatabaseFile databaseFile) {
        this.databaseFile = databaseFile;
    }

    @FXML
    public void onConfirm(ActionEvent actionEvent) {
        if(passwordField.getText().isEmpty()) return;
        try {
            getDatabaseFile().open(passwordField.getText());

            getStage().close();
            new DatabaseMenuController(getDatabaseFile()).display(MainApplication.getInstance().getStage());

            return;
        } catch (InvalidPasswordException e) {
            new InfoGui("Wrong password! Please try again.").display(getStage());
            return;
        } catch (AtomicFileLockedException e) {
            new InfoGui(String.format("The database file \"%s\" is already opened.", getDatabaseFile().getFileName())).display(getStage());
        } catch (AtomicFileCorruptedException e) {
            new InfoGui(String.format("The database file \"%s\" is corrupted.", getDatabaseFile().getFileName())).display(getStage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        getStage().close();
    }

    @Override
    public String xmlFileName() {
        return "password-prompt.fxml";
    }

    @Override
    public String initialTitle() {
        return "Enter password";
    }
}
